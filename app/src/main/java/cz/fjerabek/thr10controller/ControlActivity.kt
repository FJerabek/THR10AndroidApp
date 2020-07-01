package cz.fjerabek.thr10controller

import android.bluetooth.BluetoothDevice
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.observe
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.tabs.TabLayoutMediator
import cz.fjerabek.thr10controller.bluetooth.BluetoothService
import cz.fjerabek.thr10controller.data.Preset
import cz.fjerabek.thr10controller.data.message.bluetooth.IBtMessageHandler
import cz.fjerabek.thr10controller.data.message.bluetooth.IBtMessageSender
import cz.fjerabek.thr10controller.data.message.bluetooth.*
import cz.fjerabek.thr10controller.data.message.bluetooth.IBtMessageReceiver
import cz.fjerabek.thr10controller.databinding.ActivityControlBinding
import cz.fjerabek.thr10controller.ui.PresetAdapter
import cz.fjerabek.thr10controller.ui.fragments.*
import kotlinx.android.synthetic.main.activity_control.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import me.aflak.bluetooth.interfaces.DeviceCallback
import timber.log.Timber
import kotlin.concurrent.timer

private val pageTitles = listOf("Main panel", "Compressor", "Delay", "Effect", "Gate", "Reverb")

class ControlActivity : FragmentActivity() {

    private lateinit var bluetoothService: BluetoothService
    private lateinit var binding: ActivityControlBinding
    private lateinit var presetAdapter: PresetAdapter

    private val viewModel: PresetViewModel by viewModels()
    private val json = Json(JsonConfiguration.Stable)
    private val messageHandler = object :
        IBtMessageHandler {
        override fun handlePresetsStatusMessage(message: BtPresetsMessage) {
            viewModel.presets.postValue(message.presets as ArrayList<Preset>)

        }

        override fun handleChangeMessage(message: BtChangeMessage) {
            viewModel.activePreset.value!!.processChangeMessage(message.change)

            //Call value change on observers
            viewModel.activePreset.postValue(viewModel.activePreset.value)
        }

        override fun handleUartStatusMessage(message: BtUartStatusMessage) {
            viewModel.uartStatus.postValue(message.status)
        }

        override fun handlePresetChangeMessage(message: BtPresetChangeMessage) {
            Timber.tag("Bluetooth").d("Preset change received: ${message}")
        }

        override fun handleDumpMessage(message: BtPresetMessage) {
            viewModel.activePreset.postValue(message.preset)
        }

        override fun handleFwVersionMessage(message: BtFirmwareStatusMessage) {
            viewModel.fwVersion.postValue(message.firmware)
        }

    }

    private val deviceCallback = object : DeviceCallback {
        override fun onDeviceDisconnected(device: BluetoothDevice?, message: String?) {
            finishActivity(0)
        }

        override fun onDeviceConnected(device: BluetoothDevice?) {
        }

        override fun onConnectError(device: BluetoothDevice?, message: String?) {
            Timber.tag("Bluetooth").e("Bluetooth connection error")
        }

        override fun onMessage(message: ByteArray?) {
            message?.let {
                messageReceiver.receiveMessage(json.parse(BtMessageSerializer, String(it)))
            }
        }

        override fun onError(errorCode: Int) {
            Timber.tag("Bluetooth").e("Device error. Error code: $errorCode")
        }
    }

    private var serviceConnection = object : ServiceConnection {
        override fun onServiceDisconnected(p0: ComponentName?) {
            finishActivity(0)
        }

        override fun onServiceConnected(p0: ComponentName?, binder: IBinder?) {
            binder as BluetoothService.Binder
            bluetoothService = binder.getService()

            bluetoothService.send(
                json.stringify(
                    BtMessageSerializer,
                    BtRequestMessage(EMessageType.GET_PRESETS)
                ).plus("\n")
            )
            bluetoothService.setDeviceCallback(deviceCallback)

            timer("Bluetooth_uart_status_request_timer", false, 0, 2000) {
                messageSender.sendMessage(BtRequestMessage(EMessageType.UART_STATUS))
            }

            messageSender.sendMessage(BtRequestMessage(EMessageType.FIRMWARE_REQUEST))
        }

    }

    private val messageSender : IBtMessageSender = object :
        IBtMessageSender {
        override fun sendMessage(message: BtMessage) {
            bluetoothService.send(json.stringify(BtMessageSerializer, message).plus("\n"))
        }
    }

    private val messageReceiver : IBtMessageReceiver = object :
        IBtMessageReceiver {
        override fun receiveMessage(message: BtMessage) {
            when (message.type) {
                EMessageType.PRESETS_STATUS -> messageHandler.handlePresetsStatusMessage(message as BtPresetsMessage)
                EMessageType.CHANGE -> messageHandler.handleChangeMessage(message as BtChangeMessage)
                EMessageType.UART_STATUS -> messageHandler.handleUartStatusMessage(message as BtUartStatusMessage)
                EMessageType.PRESET_CHANGE -> messageHandler.handlePresetChangeMessage(message as BtPresetChangeMessage)
                EMessageType.DUMP_RESPONSE -> messageHandler.handleDumpMessage(message as BtPresetMessage)
                EMessageType.FIRMWARE_RESPONSE -> messageHandler.handleFwVersionMessage(message as BtFirmwareStatusMessage)
                else -> Timber.e("Unsupported message received ${message.type}")
            }
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.plant(Timber.DebugTree())

        // Remove appbar shadow
        binding = DataBindingUtil.setContentView(this, R.layout.activity_control)
        binding.viewPager.adapter = ViewPagerAdapter(this)
        TabLayoutMediator(binding.tabs, binding.viewPager) { tab, position ->
            tab.text = pageTitles[position]
        }.attach()
        binding.lifecycleOwner = this

        bindBluetoothService()

        topAppBar.outlineProvider = null
        appBarLayout.outlineProvider = null

        presetAdapter = PresetAdapter(this, ArrayList())

        binding.presetList.adapter = presetAdapter

        val sheetBehavior = BottomSheetBehavior.from(binding.contentLayout)
        sheetBehavior.isFitToContents = false
        sheetBehavior.isHideable =
            false //prevents the bottom sheet from completely hiding off the screen
        sheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED

        viewModel.sender = messageSender

        binding.presetList.setOnItemClickListener { _, _, index, _ ->
            run {
                viewModel.activePreset.value = viewModel.presets.value!![index]
            }
        }

        binding.noPresetSelector.setOnClickListener {
            messageSender.sendMessage(BtRequestMessage(EMessageType.DUMP_REQUEST))
        }

        setViewModelObservers()
    }


    private fun setViewModelObservers() {
        viewModel.presets.observe(this) {
            presetAdapter.presets = it
            presetAdapter.notifyDataSetChanged()
        }

        viewModel.uartStatus.observe(this) {
            binding.uartStatus = it
        }

        viewModel.fwVersion.observe(this) {
            binding.firmwareVersion = it
        }
    }

    private fun bindBluetoothService() {
        Intent(this, BluetoothService::class.java).also { intent ->
            bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        bluetoothService.deviceDisconnect()
        unbindService(serviceConnection)
    }

    inner class ViewPagerAdapter(fragmentActivity: FragmentActivity) :
        FragmentStateAdapter(fragmentActivity) {

        override fun getItemCount(): Int {
            return pageTitles.size
        }

        override fun createFragment(position: Int): Fragment = when (position) {

            0 -> MainPanelFragment.getInstance()
            1 -> CompressorFragment.getInstance()
            2 -> DelayFragment.getInstance()
            3 -> ReverbFragment.getInstance()
            4 -> EffectFragment.getInstance()
            5 -> GateFragment.getInstance()
            else -> error("Invalid fragment position")
        }

    }
}