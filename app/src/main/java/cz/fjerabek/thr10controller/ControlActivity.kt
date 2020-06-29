package cz.fjerabek.thr10controller

import android.bluetooth.BluetoothDevice
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.tabs.TabLayoutMediator
import cz.fjerabek.thr10controller.bluetooth.BluetoothService
import cz.fjerabek.thr10controller.data.message.MessageSender
import cz.fjerabek.thr10controller.data.message.bluetooth.BtMessage
import cz.fjerabek.thr10controller.data.message.bluetooth.BtMessageSerializer
import cz.fjerabek.thr10controller.data.message.bluetooth.BtRequestMessage
import cz.fjerabek.thr10controller.data.message.bluetooth.EMessageType
import cz.fjerabek.thr10controller.data.message.serial.MessageHandler
import cz.fjerabek.thr10controller.databinding.ActivityControlBinding
import cz.fjerabek.thr10controller.ui.MainPanelFragment
import kotlinx.android.synthetic.main.activity_control.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import me.aflak.bluetooth.interfaces.DeviceCallback
import timber.log.Timber

private const val NUM_PAGES = 2
private val pageTitles = listOf("Main panel", "Compressor", "Delay", "Effect", "Gate", "Reverb")

class ControlActivity : FragmentActivity(), MessageSender, MessageHandler {

    private lateinit var bluetoothService : BluetoothService
    private lateinit var binding : ActivityControlBinding

    private val json = Json(JsonConfiguration.Default)

    private val deviceCallback = object : DeviceCallback {
        override fun onDeviceDisconnected(device: BluetoothDevice?, message: String?) {
            finishActivity(0)
        }

        override fun onDeviceConnected(device: BluetoothDevice?) {
        }

        override fun onConnectError(device: BluetoothDevice?, message: String?) {
        }

        override fun onMessage(message: ByteArray?) {
            message?.let {
                receiveMessage(json.parse(BtMessageSerializer, String(it)))
            }
        }

        override fun onError(errorCode: Int) {
            Timber.e("Device error. Error code: $errorCode")
        }
    }

    private var serviceConnection = object : ServiceConnection {
        override fun onServiceDisconnected(p0: ComponentName?) {

        }

        override fun onServiceConnected(p0: ComponentName?, binder: IBinder?) {
            binder as BluetoothService.Binder
            bluetoothService = binder.getService()

            bluetoothService.send(json.stringify(BtMessageSerializer, BtRequestMessage(EMessageType.GET_PRESETS)).plus("\n"))

            /* Set bluetooth service */
            bluetoothService.setDeviceCallback(deviceCallback)

        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_control)
        binding.viewPager.adapter = ViewPagerAdapter(this)
        TabLayoutMediator(binding.tabs, binding.viewPager) {
                tab, position -> tab.text = pageTitles[position]
        }.attach()

        Intent(this, BluetoothService::class.java).also { intent ->
            bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
        }

        topAppBar.outlineProvider = null
        topBar.outlineProvider = null


        val sheetBehavior = BottomSheetBehavior.from(binding.contentLayout)
        sheetBehavior.isFitToContents = false
        sheetBehavior.isHideable = false //prevents the boottom sheet from completely hiding off the screen
        sheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
    }



    override fun onDestroy() {
        super.onDestroy()
        unbindService(serviceConnection)
    }

    inner class ViewPagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

        override fun getItemCount(): Int {
            return NUM_PAGES
        }

        override fun createFragment(position: Int): Fragment = when(position) {
            1 -> MainPanelFragment(this@ControlActivity)
            else -> MainPanelFragment(this@ControlActivity)
        }

    }

    override fun sendMessage(message: BtMessage) {
        bluetoothService.send(json.stringify(BtMessageSerializer, message).plus("\n"))
    }

    override fun receiveMessage(message: BtMessage) {
        when (message.type) {
            EMessageType.PRESETS_STATUS -> Timber.d("Presets message received: ${message}}")
            EMessageType.CHANGE -> Timber.d("Change message received: ${message}")
            EMessageType.UART_STATUS -> Timber.d("Uart status message received: ${message}")
            EMessageType.PRESET_CHANGE -> Timber.d("Preset change received: ${message}")
            else -> Timber.e("Unsupported message received ${message.type}")
        }
    }
}