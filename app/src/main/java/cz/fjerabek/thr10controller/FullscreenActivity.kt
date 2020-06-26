package cz.fjerabek.thr10controller

import android.bluetooth.BluetoothDevice
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import cz.fjerabek.thr10.bluetooth.messages.BtMessageSerializer
import cz.fjerabek.thr10.bluetooth.messages.BtRequestMessage
import cz.fjerabek.thr10.bluetooth.messages.EMessageType
import cz.fjerabek.thr10controller.databinding.ActivityFullscreenBinding
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import me.aflak.bluetooth.Bluetooth
import me.aflak.bluetooth.interfaces.BluetoothCallback
import me.aflak.bluetooth.interfaces.DeviceCallback
import me.aflak.bluetooth.interfaces.DiscoveryCallback
import java.util.*
import kotlin.collections.ArrayList


/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
class FullscreenActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFullscreenBinding
    private lateinit var bluetooth : Bluetooth
    private lateinit var btListAdapter : BluetoothDeviceAdapter

    private var btDevices = ArrayList<BluetoothDevice>()


    override fun onStart() {
        super.onStart()

        bluetooth.onStart()

        btDevices.addAll(bluetooth.pairedDevices)
        btListAdapter.notifyDataSetChanged()


        if(!bluetooth.isEnabled) {
            bluetooth.showEnableDialog(this)
        }
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        bluetooth.onActivityResult(requestCode, resultCode)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        btListAdapter = BluetoothDeviceAdapter(this, btDevices)

        bluetooth = Bluetooth(this)

        bluetooth.setDeviceCallback(object : DeviceCallback {
            override fun onDeviceDisconnected(device: BluetoothDevice?, message: String?) {
                Log.d("Bluetooth", "Device disconnected  $message")
            }

            override fun onDeviceConnected(device: BluetoothDevice?) {
                /*
                Sending test dump message
                 */
//                val json = Json(JsonConfiguration.Stable)
//                bluetooth.send(json.stringify(BtMessageSerializer, BtRequestMessage(EMessageType.DUMP_REQUEST)))
            }

            override fun onConnectError(device: BluetoothDevice?, message: String?) {
                Log.e("Bluetooth", "Device connection error  $message")
            }

            override fun onMessage(message: ByteArray?) {
                Log.e("Bluetooth", "Bluetooth message  $message")
            }

            override fun onError(errorCode: Int) {
                Log.e("Bluetooth", "Device error. Error code: $errorCode")
            }
        })

        bluetooth.setDiscoveryCallback(object : DiscoveryCallback {
            override fun onDevicePaired(device: BluetoothDevice?) {}
            override fun onDeviceUnpaired(device: BluetoothDevice?) {}

            override fun onDiscoveryStarted() {
                Log.d("Bluetooth", "Scan started")
                binding.bluetoothProgressBar.isIndeterminate = true
                btDevices.clear()
                btListAdapter.notifyDataSetChanged()
            }

            override fun onError(errorCode: Int) {
                Log.e("Bluetooth", "Bluetooth discovery error error code: $errorCode")
            }

            override fun onDiscoveryFinished() {
                binding.bluetoothScanButton.isEnabled = true
                binding.bluetoothProgressBar.isIndeterminate = false
                Log.d("Bluetooth", "Scan stopped")
            }

            override fun onDeviceFound(device: BluetoothDevice?) {
                device?.let {
                    btDevices.add(it)
                }
                btListAdapter.notifyDataSetChanged()
                println(btListAdapter.devices)

                Log.d("Bluetooth", "Device found: ${device?.name}   ${device?.address}")
            }

        })

        binding = DataBindingUtil.setContentView(this, R.layout.activity_fullscreen)
        binding.bluetoothDeviceList.adapter = btListAdapter
        binding.bluetoothDeviceList.setOnItemClickListener { adapterView, _, i, _ ->
            val adapter = adapterView.adapter as BluetoothDeviceAdapter


            bluetooth.connectToDevice(adapter.devices[i])
        }

        binding.btnCallbacks = object : ButtonCallbacks {
            override fun bluetoothScan() {
                bluetooth.startScanning()
                Log.d("Bluetooth", "Scan initiated")
                binding.bluetoothScanButton.isEnabled = false

            }

        }
    }

    interface ButtonCallbacks {
        fun bluetoothScan()
    }
    companion object {
    }
}