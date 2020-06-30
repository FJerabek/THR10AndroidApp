package cz.fjerabek.thr10controller

import android.bluetooth.BluetoothDevice
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.net.PlatformVpnProfile
import android.os.Bundle
import android.os.IBinder
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.tabs.TabLayoutMediator
import cz.fjerabek.thr10controller.bluetooth.BluetoothService
import cz.fjerabek.thr10controller.data.Preset
import cz.fjerabek.thr10controller.data.controls.MainPanel
import cz.fjerabek.thr10controller.data.enums.mainpanel.EAmpType
import cz.fjerabek.thr10controller.data.message.MessageHandler
import cz.fjerabek.thr10controller.data.message.MessageSender
import cz.fjerabek.thr10controller.data.message.bluetooth.*
import cz.fjerabek.thr10controller.data.message.serial.MessageReceiver
import cz.fjerabek.thr10controller.databinding.ActivityControlBinding
import cz.fjerabek.thr10controller.ui.PresetAdapter
import cz.fjerabek.thr10controller.ui.fragments.*
import kotlinx.android.synthetic.main.activity_control.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import me.aflak.bluetooth.interfaces.DeviceCallback
import timber.log.Timber
import java.util.*
import kotlin.collections.ArrayList

private val pageTitles = listOf("Main panel", "Compressor", "Delay", "Effect", "Gate", "Reverb")

class ControlActivity : FragmentActivity(), MessageSender, MessageReceiver {

    private lateinit var bluetoothService : BluetoothService
    private lateinit var binding : ActivityControlBinding
    private lateinit var presetAdapter: PresetAdapter

    private var presets : List<Preset> = ArrayList()
    private val json = Json(JsonConfiguration.Stable)

    private val messageHandler = object : MessageHandler {
        override fun handlePresetsStatusMessage(message: BtPresetsMessage) {
            presetAdapter.presets = message.presets
            Timber.d("Presets received: ${presets}")
            runOnUiThread {
                presetAdapter.notifyDataSetChanged()
            }

        }

        override fun handleChangeMessage(message: BtChangeMessage) {
            Timber.d("Change message received: ${message}")
        }

        override fun handleUartStatusMessage(message: BtUartStatusMessage) {
            Timber.d("Uart status message received: ${message}")
        }

        override fun handlePresetChangeMessage(message: BtPresetChangeMessage) {
            Timber.d("Preset change received: ${message}")
        }

    }

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
        binding.lifecycleOwner = this

//        Intent(this, BluetoothService::class.java).also { intent ->
//            bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
//        }

        topAppBar.outlineProvider = null
        appBarLayout.outlineProvider = null

        presets = arrayListOf(
            Preset("Test1", MainPanel(EAmpType.ACO, 10, 10, 10, 10, 10, null)),
            Preset("Test2", MainPanel(EAmpType.ACO, 10, 10, 10, 10, 10, null)),
            Preset("Test3", MainPanel(EAmpType.ACO, 10, 10, 10, 10, 10, null)),
            Preset("Test4", MainPanel(EAmpType.ACO, 10, 10, 10, 10, 10, null)),
            Preset("Test5", MainPanel(EAmpType.ACO, 10, 10, 10, 10, 10, null))

        )

        presetAdapter = PresetAdapter(this, presets)

        binding.presetList.adapter = presetAdapter

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
            return pageTitles.size
        }

        override fun createFragment(position: Int): Fragment = when(position) {
            0 -> MainPanelFragment.getInstance(this@ControlActivity)
            1 -> CompressorFragment.getInstance(this@ControlActivity)
            2 -> DelayFragment.getInstance(this@ControlActivity)
            3 -> ReverbFragment.getInstance(this@ControlActivity)
            4-> EffectFragment.getInstance(this@ControlActivity)
            5 -> GateFragment.getInstance(this@ControlActivity)
            else -> error("Invalid fragment position")
        }

    }

    override fun sendMessage(message: BtMessage) {
        bluetoothService.send(json.stringify(BtMessageSerializer, message).plus("\n"))
    }

    override fun receiveMessage(message: BtMessage) {
        when (message.type) {
            EMessageType.PRESETS_STATUS -> messageHandler.handlePresetsStatusMessage(message as BtPresetsMessage)
            EMessageType.CHANGE -> messageHandler.handleChangeMessage(message as BtChangeMessage)
            EMessageType.UART_STATUS -> messageHandler.handleUartStatusMessage(message as BtUartStatusMessage)
            EMessageType.PRESET_CHANGE -> messageHandler.handlePresetChangeMessage(message as BtPresetChangeMessage)
            else -> Timber.e("Unsupported message received ${message.type}")
        }
    }
}