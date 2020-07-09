package cz.fjerabek.thr10controller.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import cz.fjerabek.thr10controller.R
import cz.fjerabek.thr10controller.data.enums.IControlProperty
import cz.fjerabek.thr10controller.data.message.bluetooth.BtChangeMessage
import cz.fjerabek.thr10controller.data.message.bluetooth.EMessageType
import cz.fjerabek.thr10controller.data.message.midi.ChangeMessage
import cz.fjerabek.thr10controller.databinding.DelayFragmentBinding
import cz.fjerabek.thr10controller.ui.CustomKnob

class DelayFragment : Fragment() {
    private lateinit var viewModel: PresetViewModel
    private lateinit var binding: DelayFragmentBinding

    companion object {
        fun getInstance(): DelayFragment {
            val instance =
                DelayFragment()
            return instance
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(requireActivity()).get(PresetViewModel::class.java)
        val view = inflater.inflate(R.layout.delay_fragment, container, false)
        binding = DataBindingUtil.bind(view)!!


        viewModel.activePreset.observe(viewLifecycleOwner) {
            binding.preset = it
        }

        val defaultListener: (value : Int, knob: CustomKnob) -> Unit = { value, knob ->
            handleChange(knob.property!!, value)
        }

//        binding.feedback.valueChangeListener = defaultListener
//        binding.highCut.valueChangeListener = defaultListener
//        binding.level.valueChangeListener = defaultListener
//        binding.lowCut.valueChangeListener = defaultListener
//        binding.time.valueChangeListener = defaultListener

        return view
    }

    fun handleChange(property: IControlProperty, value: Int) {
        val changeMessage = ChangeMessage(property.getPropertyId(), value)
        viewModel.sender?.sendMessage(
            BtChangeMessage(
                EMessageType.CHANGE,
                changeMessage
            )
        )
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        // TODO: Use the ViewModel
    }

}