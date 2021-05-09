package cz.fjerabek.thr10controller.ui

import android.content.Context
import android.util.AttributeSet
import cz.fjerabek.thr.data.controls.TypeConverter
import cz.fjerabek.thr.data.enums.IControlProperty
import cz.fjerabek.thr.data.midi.PresetMessage


/**
 * Custom UI knob implementation
 * @param context appliaction context
 * @param attributeSet xml attribute set
 */
class CustomKnob(context: Context, attributeSet: AttributeSet) :
    Knob(context, attributeSet) {
    /**
     * Control property that this knob controls
     */
    var property: IControlProperty? = null
        set(value) {
            field = value
            maxValue = property?.getMaximumValue() ?: 100
            minValue = property?.getMinimumValue() ?: 0
            updateKnobValue(preset, value)
        }

    /**
     * Preset for control value modification
     */
    var preset: PresetMessage? = null
        set(newVal) {
            field = newVal
            if(!swipe)
                updateKnobValue(newVal, property)
        }

    /**
     * Value change callback
     */
    var propertyValueChangeListener: (Byte, Int) -> Unit = { _: Byte, _: Int -> }

    /**
     * Updates knob value
     * @param presetMessage preset message
     * @param property modified property
     */
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