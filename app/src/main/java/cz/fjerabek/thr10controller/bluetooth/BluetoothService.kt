package cz.fjerabek.thr10controller.bluetooth

import android.app.Service
import android.bluetooth.BluetoothDevice
import android.content.Intent
import android.os.IBinder
import android.util.Log
import me.aflak.bluetooth.Bluetooth
import me.aflak.bluetooth.interfaces.BluetoothCallback
import me.aflak.bluetooth.interfaces.DeviceCallback
import me.aflak.bluetooth.interfaces.DiscoveryCallback
import timber.log.Timber

/**
 * Android service managing bluetooth and its connections
 */
class BluetoothService : Service() {
    private lateinit var bluetooth: Bluetooth
    val pairedDevices: MutableList<BluetoothDevice> get() = bluetooth.pairedDevices


    override fun onCreate() {
        super.onCreate()
        Timber.d("onStart: Bluetooth service")
        bluetooth = Bluetooth(this)
        bluetooth.onStart()
    }

    override fun onDestroy() {
        super.onDestroy()
        bluetooth.onStop()
    }

    /**
     * Sets bluetooth device [callback]
     * @param callback bluetooth device callback
     */
    fun setDeviceCallback(callback: DeviceCallback) = bluetooth.setDeviceCallback(callback)

    /**
     * Sets bluetooth discovery [callback]
     * @param callback bluetooth discovery callback
     */
    fun setDiscoveryCallback(callback: DiscoveryCallback) = bluetooth.setDiscoveryCallback(callback)

    /**
     * Starts bluetooth device discovery
     */
    fun startScan() = bluetooth.startScanning()

    /**
     * Connects to device
     * @param device device to connect to
     */
    fun deviceConnect(device: BluetoothDevice) = bluetooth.connectToDevice(device)

    /**
     * Disconnects from device
     */
    fun deviceDisconnect() = bluetooth.disconnect()

    /**
     * Sends string message to connected device
     * @param message string message to send
     */
    fun send(message: String) {
        bluetooth.send(message)
    }

    override fun onBind(intent: Intent): IBinder = Binder()

    override fun onUnbind(intent: Intent?): Boolean {
        bluetooth.removeDeviceCallback()
        bluetooth.removeDiscoveryCallback()
        return super.onUnbind(intent)
    }

    inner class Binder : android.os.Binder() {
        fun getService(): BluetoothService = this@BluetoothService
    }
}
