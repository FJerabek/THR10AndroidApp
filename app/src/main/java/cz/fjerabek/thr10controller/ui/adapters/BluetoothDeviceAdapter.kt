package cz.fjerabek.thr10controller.ui.adapters

import android.bluetooth.BluetoothDevice
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ListView
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import cz.fjerabek.thr10controller.R
import cz.fjerabek.thr10controller.databinding.BluetoothListRowLayoutBinding

/**
 * Bluetooth device gui wrapper
 * @param device bluetooth device
 * @param enabled is GUI element associated with bluetooth device enabled
 */
data class BluetoothDeviceWrapper(val device: BluetoothDevice, var enabled: Boolean)


/**
 * List view adapter for bluetooth devices
 * @param context application context
 * @property devices initial bluetooth devices
 */
class BluetoothDeviceAdapter(
    context: Context,
    var devices: List<BluetoothDeviceWrapper> = emptyList(),
) : BaseAdapter() {

    /**
     * Layout inflater
     */
    private val inflater =
        context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getCount(): Int = devices.size

    override fun getItem(pos: Int): BluetoothDeviceWrapper = devices[pos]

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    override fun isEnabled(position: Int) = getItem(position).enabled

    override fun areAllItemsEnabled() = devices.all { it.enabled }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: View =
            convertView ?: inflater.inflate(R.layout.bluetooth_list_row_layout, parent, false)

        val binding: BluetoothListRowLayoutBinding? = DataBindingUtil.bind(view)
        binding?.name = devices[position].device.name
        binding?.address = devices[position].device.address
        return view
    }
}