package cz.fjerabek.thr10controller.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import androidx.databinding.DataBindingUtil
import cz.fjerabek.thr10controller.R
import cz.fjerabek.thr10controller.data.Preset
import cz.fjerabek.thr10controller.databinding.PresetListRowLayoutBinding

class PresetAdapter(
    context: Context,
    var presets : List<Preset>
) : BaseAdapter() {
    private val inflater =
        context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getCount(): Int = presets.size

    override fun getItem(pos: Int): Preset? = presets[pos]

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: View =
            convertView ?: inflater.inflate(R.layout.preset_list_row_layout, parent, false)

        val binding: PresetListRowLayoutBinding? = DataBindingUtil.bind(view)
        binding?.name = presets[position].name
        return view
    }
}