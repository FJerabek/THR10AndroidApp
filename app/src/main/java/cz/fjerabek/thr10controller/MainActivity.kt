package cz.fjerabek.thr10controller

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.media.MediaRouter2
import android.os.Bundle
import android.os.IBinder
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.android.material.snackbar.Snackbar
import cz.fjerabek.thr.data.bluetooth.FwVersionRq
import cz.fjerabek.thr.data.bluetooth.IBluetoothMessage
import cz.fjerabek.thr10controller.bluetooth.BluetoothService
import cz.fjerabek.thr10controller.databinding.ActivityMainBinding
import cz.fjerabek.thr10controller.databinding.BluetoothListRowLayoutBinding
import cz.fjerabek.thr10controller.ui.adapters.BluetoothDeviceAdapter
import cz.fjerabek.thr10controller.ui.adapters.BluetoothDeviceWrapper
import cz.fjerabek.thr10controller.viewmodels.MainActivityViewModel
import kotlinx.coroutines.*
import timber.log.Timber
import kotlin.concurrent.timer


/**
 * Bluetooth connection selection chooser
 */
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var btListAdapter: BluetoothDeviceAdapter
    private var bluetoothService: BluetoothService? = null
    private val viewModel: MainActivityViewModel by viewModels()
    private var connectingDeviceBinding: BluetoothListRowLayoutBinding? = null

    private var serviceConnection = object : ServiceConnection {
        override fun onServiceDisconnected(p0: ComponentName?) {
            Timber.d("Bluetooth service disconnected")

        }

        override fun onServiceConnected(p0: ComponentName?, binder: IBinder?) {
            binder as BluetoothService.Binder
            bluetoothService = binder.getService()

            if (bluetoothService?.connected == true) {
                startControlActivity()
            }


            viewModel.devices.value =
                bluetoothService!!.pairedDevices.map { BluetoothDeviceWrapper(it, true) }

            btListAdapter.notifyDataSetChanged()

            bluetoothService!!.onDeviceConnected = ::deviceConnected
            bluetoothService!!.onDeviceDisconnected = ::deviceDisconnect

        }

    }

    private fun startControlActivity() {
        val intent = Intent(this, ControlActivity::class.java)
        startActivity(intent)
        finishAffinity()
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
        binding.lifecycleOwner = this

        viewModel.devices.observe(this) { //Todo: Live data should not need to be observed if databinding is used
            btListAdapter.devices = it
            btListAdapter.notifyDataSetChanged()
        }


        btListAdapter = BluetoothDeviceAdapter(this)
        binding.bluetoothDeviceList.setOnItemClickListener { _, view, position, _ ->
            connectingDeviceBinding = DataBindingUtil.bind(view)

            setAllDevicesEnabled(false)
            connectingDeviceBinding?.loading = true
            GlobalScope.launch {
                bluetoothService?.deviceConnect(btListAdapter.devices[position].device)
            }
        }

        binding.bluetoothDeviceList.adapter = btListAdapter
    }

    private fun deviceConnected(connectedDevice: BluetoothDevice) {
        Timber.d("Device connected")
        runBlocking {
            withContext(Dispatchers.Main) {
                connectingDeviceBinding?.loading = false
            }
        }
        setAllDevicesEnabled(true)
        startControlActivity()
    }

    private fun deviceDisconnect(e: Exception, connectedDevice: BluetoothDevice) {
        Timber.d(e)
        Snackbar.make(binding.root, R.string.connection_error, Snackbar.LENGTH_LONG).show()
        runOnUiThread {
            connectingDeviceBinding?.loading = false
        }
        setAllDevicesEnabled(true)
    }

    private fun setAllDevicesEnabled(enabled: Boolean) {
        viewModel.devices.value?.forEach {
            it.enabled = enabled
        }
    }

}