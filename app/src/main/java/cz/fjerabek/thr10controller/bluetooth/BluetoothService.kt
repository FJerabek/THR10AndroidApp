package cz.fjerabek.thr10controller.bluetooth

import android.app.Service
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.Intent
import android.os.IBinder
import cz.fjerabek.thr.data.bluetooth.IBluetoothMessage
import cz.fjerabek.thr10controller.parser.JsonParser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.IOException
import java.io.InputStream
import java.util.*

/**
 * Android service managing bluetooth and its connections
 */
class BluetoothService : Service() {
    /**
     * UUID for RFCOMM service
     */
    private val serviceUuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb")

    /**
     * Bluetooth adapter
     */
    private val bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()

    /**
     * Connected device or null
     */

    private var connectedDevice: BluetoothDevice? = null

    /**
     * Bluetooth socket to connected device or null
     */
    private var socket: BluetoothSocket? = null


    /**
     * Device connected callback
     */
    var onDeviceConnected: ((device: BluetoothDevice) -> Unit)? = null

    /**
     * Message received callback
     */
    var onMessageReceived: ((IBluetoothMessage) -> Unit)? = null

    /**
     * Device disconnected callback
     */
    var onDeviceDisconnected: ((e: Exception, device: BluetoothDevice) -> Unit)? = null

    /**
     * Set of paired devices
     */
    val pairedDevices: Set<BluetoothDevice> get() = bluetoothAdapter.bondedDevices

    /**
     * Is device connected
     */
    val connected: Boolean
        get() = (socket?.isConnected ?: false)

    /**
     * Connects to device
     * @param device device to connect to
     */
    suspend fun deviceConnect(device: BluetoothDevice) {
        withContext(Dispatchers.IO) {
            socket = device.createRfcommSocketToServiceRecord(serviceUuid)
            connectedDevice = device
            socket?.let {
                try {
                    it.connect()
                    onDeviceConnected?.invoke(connectedDevice!!)
                } catch (e: IOException) {
                    Timber.e(e)
                    onDeviceDisconnected?.invoke(e, connectedDevice!!)
                    socket = null
                }
                startReader(it.inputStream)
            }
        }
    }

    /**
     * Sends string message to connected device
     * @param message string message to send
     */
    private suspend fun send(message: String) {
        withContext(Dispatchers.IO) {
            socket?.outputStream?.write(message.encodeToByteArray())
        }
    }

    /**
     * Sends bluetooth message to connected device
     * @param message message to send
     */
    suspend fun send(message: IBluetoothMessage) {
        try {
            send(JsonParser.serialize(message) + "\n")
        } catch (e: IOException) {
            onDeviceDisconnected?.invoke(e, connectedDevice!!)
            socket = null
        }
    }

    /**
     * Starts receiver on new thread
     * @param stream to receive from
     */
    private suspend fun startReader(stream: InputStream) {
        withContext(Dispatchers.IO) {
            val builder = StringBuilder()
            while (true) {
                try {
                    val available = stream.available()
                    if(available == 0) continue
                    val read = ByteArray(available)
                    stream.read(read, 0, available)
                    builder.append(read.decodeToString())
                    val lastMessageDelimiter = builder.lastIndexOf('\n')
                    if(lastMessageDelimiter == -1) continue
                    val messages = builder.substring(0..lastMessageDelimiter)
                    builder.deleteRange(0, lastMessageDelimiter + 1)
                    val split = messages.split('\n').filterNot { it.trim().isEmpty() }

                    split.forEach {
                        parseMessage(it)
                    }

                } catch (e: IOException) {
                    Timber.e(e)
                    onDeviceDisconnected?.invoke(e, connectedDevice!!)
                    socket = null
                    return@withContext
                }
            }
        }

    }

    /**
     * Parses message from string and calls message received callback
     * @param receivedString message received in string
     */
    private fun parseMessage(receivedString: String) {
        receivedString.split('\n').forEach messageLoop@{ //Todo: Some better message splitting mechanism
            if (it.trim().isEmpty()) return@messageLoop
            val message = JsonParser.deserialize(it)
            onMessageReceived?.invoke(message)
        }
    }

    /**
     * Method called on service bind
     * @param intent binding intent
     */
    override fun onBind(intent: Intent): IBinder = Binder()

    /**
     * Method called on service unbind
     * @param intent binding intent
     */
    override fun onUnbind(intent: Intent?): Boolean {
        return super.onUnbind(intent)
    }

    /**
     * Service binder
     */
    inner class Binder : android.os.Binder() {
        /**
         * Return service instance
         * @return bluetooth service instance
         */
        fun getService(): BluetoothService = this@BluetoothService
    }
}
