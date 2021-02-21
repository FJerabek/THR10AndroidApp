package cz.fjerabek.thr10controller.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import cz.fjerabek.thr10controller.databinding.DelayFragmentBinding
import cz.fjerabek.thr10controller.viewmodels.PresetViewModel

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
        binding = DelayFragmentBinding.inflate(inflater, container, false)


//        viewModel.activePreset.observe(viewLifecycleOwner) {
//            binding.preset = it
//        }

//        val defaultListener: (value : Int, knob: CustomKnob) -> Unit = { value, knob ->
//            handleChange(knob.property!!, value)
//        }
//
//        binding.feedback.valueChangeListener = defaultListener
//        binding.highCut.valueChangeListener = defaultListener
//        binding.level.valueChangeListener = defaultListener
//        binding.lowCut.valueChangeListener = defaultListener
//        binding.time.valueChangeListener = defaultListener
//        binding.status.setOnCheckedChangeListener { _, checked ->
//            handleChange(
//                EDelay.STATUS,
//                if (checked) EStatus.ON.value.toInt() else EStatus.OFF.value.toInt()
//            )
//        }
//
        return binding.root
    }
//
//    private fun handleChange(property: IControlProperty, value: Int) {
//        val changeMessage = ChangeMessage(property.getPropertyId(), value)
//        viewModel.sender?.sendMessage(
//            BtChangeMessage(
//                EMessageType.CHANGE,
//                changeMessage
//            )
//        )
//    }
}