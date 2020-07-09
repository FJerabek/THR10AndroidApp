package cz.fjerabek.thr10controller.ui

import android.content.Context
import android.util.AttributeSet
import android.view.View
import cz.fjerabek.thr10controller.R
import cz.fjerabek.thr10controller.data.enums.IControlProperty


class CustomKnob(context: Context, attributeSet: AttributeSet) :
    Knob(context, attributeSet) {
    var property: IControlProperty? = null
        set(value) {
            field = value
            maxValue = property?.getMaximumValue() ?: 100
            minValue = property?.getMinimumValue() ?: 0
        }

    var valueChangeListener: (value: Int, knob: CustomKnob) -> Unit = { _: Int, _: CustomKnob ->}


    init {
        onValueChangeListener = {
            valueChangeListener(it, this@CustomKnob)
        }
    }
}