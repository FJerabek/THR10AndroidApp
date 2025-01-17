package cz.fjerabek.thr10controller

import android.bluetooth.BluetoothDevice
import android.content.*
import android.os.Bundle
import android.os.IBinder
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.Transformation
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator
import cz.fjerabek.thr.data.bluetooth.*
import cz.fjerabek.thr.data.enums.effect.EEffect
import cz.fjerabek.thr.data.midi.ChangeMessage
import cz.fjerabek.thr.data.midi.PresetMessage
import cz.fjerabek.thr.data.uart.FWVersionMessage
import cz.fjerabek.thr.data.uart.StatusMessage
import cz.fjerabek.thr10controller.bluetooth.BluetoothService
import cz.fjerabek.thr10controller.databinding.ActivityControlBinding
import cz.fjerabek.thr10controller.databinding.AlertEditDialogBinding
import cz.fjerabek.thr10controller.parser.JsonParser
import cz.fjerabek.thr10controller.ui.adapters.ItemMoveCallback
import cz.fjerabek.thr10controller.ui.adapters.PresetAdapter
import cz.fjerabek.thr10controller.ui.fragments.*
import cz.fjerabek.thr10controller.viewmodels.ControlActivityViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import timber.log.Timber
import java.util.*
import kotlin.concurrent.timer

/**
 * Activity providing GUI with combo controls
 */
class ControlActivity : FragmentActivity() {

    /**
     * Configuration titles
     */
    val pageTitles = listOf(
        applicationContext.getString(R.string.main_panel),
        applicationContext.getString(R.string.compressor),
        applicationContext.getString(R.string.delay),
        applicationContext.getString(R.string.reverb),
        applicationContext.getString(R.string.effect),
        applicationContext.getString(R.string.gate)
    )

    /**
     * Bluetooth communication service
     */
    private var bluetoothService: BluetoothService? = null

    /**
     * UI bindings
     */
    private lateinit var binding: ActivityControlBinding

    /**
     * User presets adapter
     */
    private lateinit var presetAdapter: PresetAdapter

    /**
     * Timer for requesting device info
     */
    private var infoTimer: Timer? = null

    /**
     * Control view model
     */
    private val viewModel: ControlActivityViewModel by viewModels()

    /**
     * Connection to bluetooth service
     */
    private var serviceConnection = object : ServiceConnection {
        override fun onServiceDisconnected(p0: ComponentName?) {
            finishActivity(0)
        }

        override fun onServiceConnected(p0: ComponentName?, binder: IBinder?) {
            binder as BluetoothService.Binder
            bluetoothService = binder.getService()
            bluetoothService?.onDeviceDisconnected = ::deviceDisconnected
            bluetoothService?.onMessageReceived = ::messageReceived

            runBlocking(Dispatchers.IO) {
                bluetoothService?.send(FwVersionRq())
                bluetoothService?.send(PresetsRq())
                bluetoothService?.send(CurrentPresetRq())
                bluetoothService?.send(CurrentPresetIndexRq())
                bluetoothService?.send(ConnectedRq())
            }

            infoTimer = timer("Test message timer", false, period = 5000) {
                runBlocking(Dispatchers.IO) {
                    bluetoothService?.send(HwStatusRq())
                }
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_control)
        binding.viewPager.adapter = ViewPagerAdapter(this)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        bindBluetoothService()

//         Add tabs to control panel
        TabLayoutMediator(binding.tabs, binding.viewPager) { tab, position ->
            tab.text = pageTitles[position]
        }.attach()

        viewModel.changeMessageCallback.value = ::sendChangeMessage
        viewModel.presets.observe(this, ::onPresetsModified)
        viewModel.presetChanged.observe(this, ::onPresetModifiedChange)

        val layoutManager = LinearLayoutManager(this)
        binding.presetList.layoutManager = layoutManager
        binding.presetList.addItemDecoration(
            DividerItemDecoration(
                this,
                layoutManager.orientation
            )
        )

        presetAdapter = PresetAdapter(this, viewModel.presets, binding.root)
        presetAdapter.onItemSelectedListener = ::onPresetSelected
        presetAdapter.onItemLongClick = ::onPresetLongClick

        val touchHelper = ItemTouchHelper(ItemMoveCallback(presetAdapter))
        touchHelper.attachToRecyclerView(binding.presetList)
        presetAdapter.onDragRequest = {
            touchHelper.startDrag(it)
        }

        binding.presetList.adapter = presetAdapter
//
        val sheetBehavior = BottomSheetBehavior.from(binding.contentLayout)
        sheetBehavior.isFitToContents = false
        sheetBehavior.isHideable =
            false //prevents the bottom sheet from completely hiding off the screen
        sheetBehavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED
    }

    /**
     * Preset long click callback
     * @param preset preset that was clickced
     * @param position preset position
     */
    private fun onPresetLongClick(preset: PresetMessage, position: Int) {
        val binding = AlertEditDialogBinding.inflate(layoutInflater)
        MaterialAlertDialogBuilder(this)
            .setView(binding.root)
            .setTitle(R.string.new_preset_name)
            .setCancelable(true)
            .setPositiveButton(R.string.positive_button) { _: DialogInterface, _: Int ->
                preset.name = binding.presetName.text.toString()
                viewModel.presets.value = viewModel.presets.value
            }
            .show()
    }

    /**
     * Preset selected callback
     * @param presetMessage selected preset
     * @param i preset index
     */
    private fun onPresetSelected(
        presetMessage: PresetMessage,
        i: Int
    ) {
        viewModel.presetChanged.value = false
        viewModel.activePresetIndex.value = i
        viewModel.activePreset.value = presetMessage.duplicate()
        sendPresetChangeMessage(i)
    }

    /**
     * Preset list modified callback
     * @param it modified preset list
     */
    private fun onPresetsModified(it: MutableList<PresetMessage>) {
        runBlocking(Dispatchers.IO) {

            bluetoothService?.send(SetPresetsRq(it))
            val index = it.indexOf(viewModel.activePreset.value)
            runOnUiThread {
                if (index == -1) {
                    viewModel.presetChanged.value = false
                }
                viewModel.activePresetIndex.value = index
            }
            bluetoothService?.send(
                PresetSelect(
                    index
                )
            )
        }
    }

    /**
     * Animates margin end value
     * @param view view to animate
     * @param value new value
     */
    private fun animateMarginEnd(view: View, value: Int) {
        val params = view.layoutParams as ViewGroup.MarginLayoutParams
        val start = params.marginEnd
        val a = object : Animation() {
            override fun applyTransformation(interpolatedTime: Float, t: Transformation?) {
                val params = view.layoutParams as ViewGroup.MarginLayoutParams
                params.marginEnd = start + ((value - start) * interpolatedTime).toInt()
                view.layoutParams = params
            }
        }
        a.duration = 500
        view.startAnimation(a)
    }

    /**
     * Preset modified value changed callback
     * @param it new preset modified value
     */
    private fun onPresetModifiedChange(it: Boolean) {
        if (it) {
            binding.topAppBar.menu.add("Revert")
                .setIcon(R.drawable.ic_baseline_undo_24)
                .setOnMenuItemClickListener {
                    revertPreset()
                    true
                }.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM)

            binding.topAppBar.menu.add("Save").setIcon(R.drawable.ic_baseline_save_24)
                .setOnMenuItemClickListener {
                    savePreset()
                    true
                }.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM)
        } else {
            binding.topAppBar.menu.clear()
        }
    }

    /**
     * Preset save click callback
     */
    private fun savePreset() {
        viewModel.presetChanged.value = false
        viewModel.presets.value?.set(
            viewModel.activePresetIndex.value!!, viewModel.activePreset.value!!.duplicate()
        )
        sendSetPresetsMessage(viewModel.presets.value!!)
    }

    /**
     * Preset revert click callback
     */
    private fun revertPreset() {
        viewModel.presetChanged.value = false
        viewModel.activePreset.value =
            viewModel.presets.value?.get(viewModel.activePresetIndex.value!!)!!.duplicate()
    }

    /**
     * Binds bluetooth service
     */
    private fun bindBluetoothService() {
        Intent(this, BluetoothService::class.java).also { intent ->
            bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        infoTimer?.cancel()
        unbindService(serviceConnection)
    }

    /**
     * On bluetooth device disconnected callback
     */
    private fun deviceDisconnected(e: Exception, connectedDevice: BluetoothDevice) {
        Timber.e(e)
        Snackbar.make(binding.root, getString(R.string.device_disconnected), Snackbar.LENGTH_LONG)
            .show()
        infoTimer?.cancel()
        val intent = Intent(this, DeviceSelectActivity::class.java)
        startActivity(intent)
        finishAffinity()
    }


    /**
     * On Bluetooth message callback
     * @param message received bluetooth message
     */
    private fun messageReceived(message: IBluetoothMessage) {
        when (message) {
            is FWVersionMessage -> {
                runOnUiThread {
                    viewModel.fwVersion.value = message
                }
            }

            is StatusMessage -> {
                runOnUiThread {
                    viewModel.hwStatus.value = message
                }
            }

            is PresetsResponse -> {
                runOnUiThread {
                    viewModel.presets.value = message.presets.toMutableList()
                }
            }

            is PresetMessage -> {
                runOnUiThread {
                    viewModel.activePreset.value = message
                }
            }

            is ChangeMessage -> {
                if (message.property == EEffect.TYPE.propertyId) {
                    Timber.d("Effect change: ${message.value}")
                }
                runOnUiThread {
                    viewModel.activePresetIndex.value = -1
                    viewModel.activePreset.value?.setByControlPropertyId(
                        message.property,
                        message.value
                    )
                    viewModel.activePreset.value =
                        viewModel.activePreset.value //Call live data observers
                }
            }

            is Connected -> {
                runOnUiThread {
                    viewModel.connected.value = message.connected
                }
            }

            is PresetSelect -> {
                runOnUiThread {
                    if (message.index != -1) {
                        viewModel.presetChanged.value = false
                        viewModel.activePresetIndex.value = message.index
                        viewModel.activePreset.value =
                            viewModel.presets.value?.get(message.index)
                        Snackbar.make(
                            binding.viewPager,
                            resources.getString(
                                R.string.preset_change_message,
                                viewModel.activePreset.value?.name
                            ),
                            Snackbar.LENGTH_SHORT
                        ).show()
                    }
                }
            }

            else -> Timber.d(JsonParser.serialize(message))

        }
    }

    /**
     * Sends change message
     * @param property ID of property that was changed
     * @param value new value
     */
    private fun sendChangeMessage(property: Byte, value: Int) {
        val message = ChangeMessage(property, value)
        if (!viewModel.presetChanged.value!! && viewModel.activePresetIndex.value != -1) {
            viewModel.presetChanged.value = true
        }
        viewModel.activePreset.value?.setByControlPropertyId(property, value)
        viewModel.activePreset.value = viewModel.activePreset.value
        runBlocking(Dispatchers.IO) {
            bluetoothService?.send(message)
        }
    }

    /**
     * Sends preset change message
     * @param index index of selected preset
     */
    private fun sendPresetChangeMessage(index: Int) {
        val message = PresetSelect(index)
        runBlocking(Dispatchers.IO) {
            bluetoothService?.send(message)
        }
    }

    /**
     * Sends preset list message
     * @param presets preset to send
     */
    private fun sendSetPresetsMessage(presets: List<PresetMessage>) {
        runBlocking(Dispatchers.IO) {
            bluetoothService?.send(SetPresetsRq(presets))
        }
    }

    /**
     * View pager adapter for config categories
     * @param fragmentActivity activity
     */
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