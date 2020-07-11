package cz.fjerabek.thr10controller.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import cz.fjerabek.thr10controller.R
import cz.fjerabek.thr10controller.data.enums.EStatus
import cz.fjerabek.thr10controller.data.enums.IControlProperty
import cz.fjerabek.thr10controller.data.enums.gate.EGate
import cz.fjerabek.thr10controller.data.message.bluetooth.BtChangeMessage
import cz.fjerabek.thr10controller.data.message.bluetooth.EMessageType
import cz.fjerabek.thr10controller.data.message.midi.ChangeMessage
import cz.fjerabek.thr10controller.databinding.DelayFragmentBinding
import cz.fjerabek.thr10controller.databinding.GateFragmentBinding
import cz.fjerabek.thr10controller.ui.CustomKnob

class GateFragment : Fragment() {
    private lateinit var viewModel: PresetViewModel
    private lateinit var binding: GateFragmentBinding

    companion object {
        fun getInstance(): GateFragment {
            return GateFragment()
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(requireActivity()).get(PresetViewModel::class.java)
        binding = GateFragmentBinding.inflate(inflater, container, false)


        viewModel.activePreset.observe(viewLifecycleOwner) {
            binding.preset = it
        }


        val defaultListener: (value : Int, knob: CustomKnob) -> Unit = { value, knob ->
            handleChange(knob.property!!, value)
        }

        binding.threshold.valueChangeListener = defaultListener
        binding.release.valueChangeListener = defaultListener
        binding.status.setOnCheckedChangeListener { _, checked ->
            handleChange(
                EGate.STATUS,
                if (checked) EStatus.ON.value.toInt() else EStatus.OFF.value.toInt()
            )
            viewModel.activePreset.value?.gate?.status = if (checked) EStatus.ON else EStatus.OFF
        }

        return binding.root
    }

    private fun handleChange(property: IControlProperty, value: Int) {
        val changeMessage = ChangeMessage(property.getPropertyId(), value)
        viewModel.sender?.sendMessage(
            BtChangeMessage(
                EMessageType.CHANGE,
                changeMessage
            )
        )
    }
}