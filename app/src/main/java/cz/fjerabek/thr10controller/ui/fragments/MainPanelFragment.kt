package cz.fjerabek.thr10controller.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import cz.fjerabek.thr10controller.R
import cz.fjerabek.thr10controller.data.Preset
import cz.fjerabek.thr10controller.data.enums.IControlProperty
import cz.fjerabek.thr10controller.data.enums.mainpanel.EAmpType
import cz.fjerabek.thr10controller.data.enums.mainpanel.ECabinetType
import cz.fjerabek.thr10controller.data.message.bluetooth.BtChangeMessage
import cz.fjerabek.thr10controller.data.message.bluetooth.EMessageType
import cz.fjerabek.thr10controller.data.message.midi.ChangeMessage
import cz.fjerabek.thr10controller.databinding.MainPanelFragmentBinding
import cz.fjerabek.thr10controller.ui.CustomKnob
import timber.log.Timber

class MainPanelFragment : Fragment() {
    private lateinit var viewModel: PresetViewModel
    private lateinit var binding: MainPanelFragmentBinding

    companion object {
        fun getInstance(): MainPanelFragment {
            return MainPanelFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.main_panel_fragment, container, false)
        binding = DataBindingUtil.bind(view)!!

        viewModel = ViewModelProvider(requireActivity()).get(PresetViewModel::class.java)

        val defaultListener: (value : Int, knob: CustomKnob) -> Unit = { value, knob ->
            handleChange(knob.property!!, value)
        }

        viewModel.activePreset.observe(viewLifecycleOwner) {
            binding.preset = it
        }


        binding.gain.valueChangeListener = defaultListener

        binding.amp.valueChangeListener = defaultListener
        binding.amp.valueStringConverter = {
            EAmpType.fromId(it.toByte()).toString()
        }

        binding.bass.valueChangeListener = defaultListener
        binding.master.valueChangeListener = defaultListener
        binding.middle.valueChangeListener = defaultListener
        binding.treble.valueChangeListener = defaultListener

        binding.cabinet.valueChangeListener = defaultListener
        binding.cabinet.valueStringConverter = {
            ECabinetType.fromId(it.toByte()).toString().replace("_", "\n")
        }
        return view
    }

    private fun handleChange(property: IControlProperty, value: Int) {
        val changeMessage = ChangeMessage(property.getPropertyId(), property.getMinimumValue() + value)

        viewModel.activePreset.value?.processChangeMessage(changeMessage)
        viewModel.sender?.sendMessage(
            BtChangeMessage(
                EMessageType.CHANGE,
                changeMessage
            )
        )
    }

}