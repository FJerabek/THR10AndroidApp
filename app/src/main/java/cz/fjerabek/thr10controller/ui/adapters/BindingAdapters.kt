package cz.fjerabek.thr10controller.ui.adapters

import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.widget.AppCompatSpinner
import androidx.databinding.BindingAdapter
import androidx.databinding.BindingConversion
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textview.MaterialTextView
import cz.fjerabek.thr.data.midi.PresetMessage
import cz.fjerabek.thr.data.uart.ECharging
import cz.fjerabek.thr.data.uart.FWVersionMessage
import cz.fjerabek.thr10controller.R

/**
 * Custom text binding adapter formatting FW version message to string
 * @param message FWVersionMessage with version information
 */
@BindingAdapter("android:text")
fun MaterialTextView.setText(message: MutableLiveData<FWVersionMessage>?) {
    text = "ver. ${message?.value?.major}.${message?.value?.minor}.${message?.value?.patch}"
}

/**
 * Boolean to visibility converter
 * @param visible if view should be visible
 * @return View.VISIBLE or Vew.GONE
 */
@BindingConversion
fun booleanToVisibility(visible: Boolean) =
    if (visible) View.VISIBLE else View.GONE


/**
 * Drawable binding for ECharging type setting battery drawable according to charge state
 * @param charging charging state
 */
@BindingAdapter("drawable")
fun MaterialTextView.chargingStateToDrawable(charging: ECharging?) {
    val drawable = when(charging) {
        ECharging.DISCHARGING -> R.drawable.ic_battery_24
        ECharging.CHARGING -> R.drawable.ic_battery_charging_24
        ECharging.FULL -> R.drawable.ic_battery_24
        null -> R.drawable.ic_battery_24
    }
    setCompoundDrawablesWithIntrinsicBounds(drawable,0,0,0)
}

/**
 * Sets items to adapter of listView
 * @param items items to set
 */
@Suppress("UNCHECKED_CAST") // No way to check types inside List<Any> at runtime
@BindingAdapter("data")
fun ListView.setPresetData(items: List<Any>?) {
    items?.let { list ->
        when (val adapter = adapter) {
            is BluetoothDeviceAdapter -> adapter.run {
                devices = list as List<BluetoothDeviceWrapper>
                notifyDataSetChanged()
            }
        }
    }
}


/**
 * Adapter for setting presets on to adapter on recyclerView
 * @param items new presets
 */
@BindingAdapter("data")
fun RecyclerView.setPresetData(items: MutableLiveData<MutableList<PresetMessage>>) {
    val adapter = adapter
    if(adapter is PresetAdapter) {
        if(items.value == adapter.data) return
        adapter.data = items
        adapter.notifyDataSetChanged()
    }
}

/**
 * Binding for selecting item in recyclerView
 * @param index index of selected item
 */
@BindingAdapter("selectedItem")
fun RecyclerView.selectedItem(index: Int) {
    val adapter = adapter
    if(adapter is PresetAdapter) {
        adapter.selectedItem = index
    }
}

/**
 * Binding for setting callback on preset change
 * @param callback on preset change callback
 */
@BindingAdapter("presetChangeCallback")
fun RecyclerView.setPresetChangeCallback(callback : ((PresetMessage, Int)->Unit)?) {
    val adapter = adapter
    if(adapter is PresetAdapter) {
        adapter.onItemSelectedListener = callback
    }
}

/**
 * Binding for setting callback on item selected
 * @param callback on item selected callback
 */
@BindingAdapter("onItemSelected")
fun AppCompatSpinner.setOnItemSelectedCallback(callback: ((Any) -> Unit)?) {
    callback?.let {
        onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (tag != position)
                    it(getItemAtPosition(position))
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}

        }
    }
}

/**
 * Binding for selecting new value in AppCompatSpinner
 * @param value selected item
 */
@Suppress("UNCHECKED_CAST") // No way to check types inside List<Any> at runtime
@BindingAdapter("newValue")
fun AppCompatSpinner.setNewValue(value: Any?) {
    adapter?.let {
        (adapter as ArrayAdapter<Any>).getPosition(value).let {
            setSelection(it)
            tag = it
        }
    }
}


/**
 * Binding for setting selection entries in appCompatSpinner
 * @param value new selection entries
 *
 */
@BindingAdapter("entries")
fun AppCompatSpinner.entities(value: List<Any>?) {
    value?.let {
        adapter = ArrayAdapter(context.applicationContext, android.R.layout.simple_spinner_dropdown_item, it)
    }
}