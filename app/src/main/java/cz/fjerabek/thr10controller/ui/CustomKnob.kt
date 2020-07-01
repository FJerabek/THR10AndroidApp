package cz.fjerabek.thr10controller.ui

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import cz.fjerabek.thr10controller.R
import cz.fjerabek.thr10controller.data.enums.IControlProperty
import cz.fjerabek.thr10controller.databinding.KnobLayoutBinding
import kotlinx.coroutines.CancellableContinuation
import timber.log.Timber


class CustomKnob(context: Context, attributeSet: AttributeSet) :
    ConstraintLayout(context, attributeSet) {
    var property: IControlProperty? = null
        set(value) {
            field = value
            binding.knob.numberOfStates =
                (property?.getMaximumValue() ?: 100) - (property?.getMinimumValue() ?: 0)
        }

    var textSize: Float
        get() = binding.knobValue.textSize
        set(value) {
            binding.knobValue.textSize = value
        }

    var value: Int
        set(value) {
            //Do not call listener on change from code
            val listener = valueChangeListener
            valueChangeListener = { _: Int, _: CustomKnob -> }
            binding.knob.state = value - (property?.getMinimumValue() ?: 0)
            valueChangeListener = listener
        }
        get() = binding.knob.state + (property?.getMinimumValue() ?: 0)


    var name : String
    set(value) {
        binding.name = value
    }
    get() = binding.name ?: ""

    private var currentValue = 0
    private var binding: KnobLayoutBinding

    var stringConverter: ((value: Int) -> String) = {
        it.toString()
    }

    var valueChangeListener: (value: Int, knob: CustomKnob) -> Unit = { _: Int, _: CustomKnob ->}


    init {
        val inflater = LayoutInflater.from(context)
        binding = KnobLayoutBinding.inflate(inflater, this, true)
        context.theme.obtainStyledAttributes(
            attributeSet,
            R.styleable.CustomKnob,
            0, 0
        ).apply {
            binding.knobValue.textSize = getFloat(R.styleable.CustomKnob_textSize, 10f)
            binding.name = getString(R.styleable.CustomKnob_name)
            binding.knob.state =
                getInt(R.styleable.CustomKnob_value, 0) - (property?.getMinimumValue() ?: 0)
            binding.knob.numberOfStates =
                (property?.getMaximumValue() ?: 100) - (property?.getMinimumValue() ?: 0)

            binding.knob.setOnStateChanged {
                binding.knobValue.text = stringConverter(it + (property?.getMinimumValue() ?: 0))
                if (currentValue != it) {
                    currentValue = it
                    valueChangeListener(it, this@CustomKnob)
                }
            }
        }
    }
}