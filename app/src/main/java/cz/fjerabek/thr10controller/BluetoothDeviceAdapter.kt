package cz.fjerabek.thr10controller

import android.bluetooth.BluetoothDevice
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.BaseAdapter
import androidx.databinding.DataBindingUtil
import cz.fjerabek.thr10controller.databinding.BluetoothListRowLayoutBinding

class BluetoothDeviceAdapter(
    context: Context,
    var devices : ArrayList<BluetoothDevice>
) : ArrayAdapter<BluetoothDevice>(context, R.layout.bluetooth_list_row_layout) {
    val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getCount(): Int = devices.size

    override fun getItem(pos: Int): BluetoothDevice? = devices[pos]

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view : View = convertView ?: inflater.inflate(R.layout.bluetooth_list_row_layout, parent, false)

        val binding : BluetoothListRowLayoutBinding? = DataBindingUtil.bind(view)
        binding?.name = devices[position].name
        binding?.address = devices[position].address

        return view
    }
}