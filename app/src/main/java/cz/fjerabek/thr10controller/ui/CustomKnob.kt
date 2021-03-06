package cz.fjerabek.thr10controller.ui

import android.content.Context
import android.util.AttributeSet
import cz.fjerabek.thr.data.controls.TypeConverter
import cz.fjerabek.thr.data.enums.IControlProperty
import cz.fjerabek.thr.data.midi.PresetMessage


class CustomKnob(context: Context, attributeSet: AttributeSet) :
    Knob(context, attributeSet) {
    var property: IControlProperty? = null
        set(value) {
            field = value
            maxValue = property?.getMaximumValue() ?: 100
            minValue = property?.getMinimumValue() ?: 0
            updateKnobValue(preset, value)
        }

    var preset: PresetMessage? = null
        set(newVal) {
            field = newVal
            if(!swipe)
                updateKnobValue(newVal, property)
        }

    var propertyValueChangeListener: (Byte, Int) -> Unit = { _: Byte, _: Int -> }

    private fun updateKnobValue(presetMessage: PresetMessage?, property: IControlProperty?) {
        animateValue(
            if (presetMessage != null && property != null) {
                TypeConverter.convert(
                    presetMessage.getByControlPropertyId(property.propertyId) ?: property.getMinimumValue()
                )
            } else {
                0
            }
        )
    }


    init {
        onValueChangeListener = {

            property?.let { it1 -> propertyValueChangeListener(it1.propertyId, value) }
        }
    }
}