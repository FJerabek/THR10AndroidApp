package cz.fjerabek.thr10controller.ui

import android.content.Context
import android.graphics.Canvas
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import cz.fjerabek.thr10controller.R
import cz.fjerabek.thr10controller.data.enums.IControlProperty
import cz.fjerabek.thr10controller.databinding.KnobLayoutBinding
import it.beppi.knoblibrary.Knob

class CustomKnob(property : IControlProperty, name : String, context: Context, root : ViewGroup?) : View(context) {
    var stateChangedCallback : Knob.OnStateChanged? = null

    init {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.knob_layout,root, false)
        val binding = DataBindingUtil.bind<KnobLayoutBinding>(view)
        binding?.knob?.numberOfStates = property.getMaximumValue() - property.getMinimumValue()
        binding?.name = name
    }
}
