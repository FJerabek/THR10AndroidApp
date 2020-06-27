package cz.fjerabek.thr10controller

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import cz.fjerabek.thr10controller.bluetooth.BluetoothDeviceAdapter
import cz.fjerabek.thr10controller.bluetooth.BluetoothService
import cz.fjerabek.thr10controller.databinding.ActivityMainBinding
import me.aflak.bluetooth.interfaces.DeviceCallback
import me.aflak.bluetooth.interfaces.DiscoveryCallback


/**
 * Bluetooth connection selection chooser
 */
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private lateinit var btListAdapter: BluetoothDeviceAdapter

    private lateinit var bluetoothService: BluetoothService

    private var btDevices = ArrayList<BluetoothDevice>()

    private var serviceConnection = object : ServiceConnection {
        override fun onServiceDisconnected(p0: ComponentName?) {

        }

        override fun onServiceConnected(p0: ComponentName?, binder: IBinder?) {
            binder as BluetoothService.Binder
            bluetoothService = binder.getService()
            btDevices.addAll(bluetoothService.pairedDevices)
            btListAdapter.notifyDataSetChanged()

            /* Set bluetooth service */
            bluetoothService.setDeviceCallback(object : DeviceCallback {
                override fun onDeviceDisconnected(device: BluetoothDevice?, message: String?) {
                    Log.d("Bluetooth", "Device disconnected  $message")
                }

                override fun onDeviceConnected(device: BluetoothDevice?) {
                    /*
                    Switch to control activity
                     */
                    val intent = Intent(this@MainActivity, ControlActivity::class.java)
                    startActivity(intent)
                }

                override fun onConnectError(device: BluetoothDevice?, message: String?) {
                    Toast.makeText(this@MainActivity, "Error connecting to device", Toast.LENGTH_SHORT).show()
                }

                override fun onMessage(message: ByteArray?) {
                    Log.e("Bluetooth", "Bluetooth message  $message")
                }

                override fun onError(errorCode: Int) {
                    Log.e("Bluetooth", "Device error. Error code: $errorCode")
                }
            })

            bluetoothService.setDiscoveryCallback(object : DiscoveryCallback {
                override fun onDevicePaired(device: BluetoothDevice?) {}
                override fun onDeviceUnpaired(device: BluetoothDevice?) {}

                override fun onDiscoveryStarted() {
                    Log.d("Bluetooth", "Scan started")
                    binding.bluetoothProgressBar.isIndeterminate = true
                    binding.bluetoothProgressBar.visibility = View.VISIBLE
                    btDevices.clear()
                    btDevices.addAll(bluetoothService.pairedDevices)
                    btListAdapter.notifyDataSetChanged()
                }

                override fun onError(errorCode: Int) {
                    Log.e("Bluetooth", "Bluetooth discovery error error code: $errorCode")
                }

                override fun onDiscoveryFinished() {
                    binding.bluetoothScanButton.isEnabled = true
                    binding.bluetoothProgressBar.isIndeterminate = false
                    binding.bluetoothProgressBar.visibility = View.GONE
                }

                override fun onDeviceFound(device: BluetoothDevice?) {
                    device?.let {
                        btDevices.forEach { paired ->
                            if(it.address == paired.address) {
                                Log.d("Bluetooth", "Device: ${it.name} is already in paired devices")
                                return
                            }
                        }
                        btDevices.add(it)
                    }
                    btListAdapter.notifyDataSetChanged()
                }

            })
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        this.unbindService(serviceConnection)
    }

    override fun onStart() {
        super.onStart()

        val mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        mBluetoothAdapter?.let {
            if (!it.isEnabled) {
                it.enable()
            }
        }

        Intent(this, BluetoothService::class.java).also { intent ->
            bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)


        btListAdapter = BluetoothDeviceAdapter(this, btDevices)

        binding.bluetoothDeviceList.adapter = btListAdapter
        binding.bluetoothDeviceList.setOnItemClickListener { adapterView, _, i, _ ->
            /* Called bluetooth device is selected */
            val adapter = adapterView.adapter as BluetoothDeviceAdapter
            bluetoothService.deviceConnect(adapter.devices[i])

        }

        binding.btnCallbacks = object : ButtonCallbacks {
            override fun bluetoothScan() {
                bluetoothService.startScan()
                binding.bluetoothScanButton.isEnabled = false

            }

        }
    }

    /**
     * Ui button callbacks
     */
    interface ButtonCallbacks {
        /**
         * Gets called when "bluetooth scan devices" button is pressed
         */
        fun bluetoothScan()
    }
}