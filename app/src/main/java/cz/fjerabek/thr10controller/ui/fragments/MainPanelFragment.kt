package cz.fjerabek.thr10controller.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import cz.fjerabek.thr10controller.R
import cz.fjerabek.thr10controller.data.Preset
import cz.fjerabek.thr10controller.data.enums.IControlProperty
import cz.fjerabek.thr10controller.data.enums.mainpanel.EAmpType
import cz.fjerabek.thr10controller.data.enums.mainpanel.ECabinetType
import cz.fjerabek.thr10controller.data.enums.mainpanel.EMainPanel
import cz.fjerabek.thr10controller.data.message.MessageSender
import cz.fjerabek.thr10controller.data.message.bluetooth.BtChangeMessage
import cz.fjerabek.thr10controller.data.message.bluetooth.EMessageType
import cz.fjerabek.thr10controller.data.message.midi.ChangeMessage
import cz.fjerabek.thr10controller.databinding.MainPanelFragmentBinding
import timber.log.Timber

class MainPanelFragment : Fragment() {
    private lateinit var viewModel: PresetViewModel
    private lateinit var binding: MainPanelFragmentBinding
    private var messageSend = true

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

        viewModel.activePresetIndex.observe(viewLifecycleOwner, Observer { index ->
            run {
                Timber.d("Active index changed $index")
                if (index == -1){
                    //Non preset settings
                    setPreset(viewModel.dumpPreset.value!!)
                } else {
                    setPreset(viewModel.presets.value!![index])
                }
            }
        })


        binding.gain.name = "Gain"
        binding.gain.knob.numberOfStates =
            1 + EMainPanel.GAIN.getMaximumValue() - EMainPanel.GAIN.getMinimumValue()
        binding.gain.knob.setOnStateChanged {

            binding.gain.knobValue.text = it.toString()
            handleChange(EMainPanel.GAIN, it)
        }

        binding.ampType.name = "Amp type"
        binding.ampType.knobValue.textSize = 10f
        binding.ampType.knob.numberOfStates =
            1 + EMainPanel.AMP.getMaximumValue() - EMainPanel.AMP.getMinimumValue()
        binding.ampType.knob.setOnStateChanged {
            binding.ampType.knobValue.text = EAmpType.fromId(it.toByte()).toString()
            handleChange(EMainPanel.AMP, it)
        }

        binding.bass.name = "Bass"
        binding.bass.knob.numberOfStates =
            1 + EMainPanel.BASS.getMaximumValue() - EMainPanel.BASS.getMinimumValue()
        binding.bass.knob.setOnStateChanged {
            binding.bass.knobValue.text = it.toString()
            handleChange(EMainPanel.BASS, it)
        }

        binding.master.name = "Master"
        binding.master.knob.numberOfStates =
            1 + EMainPanel.MASTER.getMaximumValue() - EMainPanel.MASTER.getMinimumValue()
        binding.master.knob.setOnStateChanged {
            binding.master.knobValue.text = it.toString()
            handleChange(EMainPanel.MASTER, it)
        }

        binding.middle.name = "Middle"
        binding.middle.knob.numberOfStates =
            1 + EMainPanel.MIDDLE.getMaximumValue() - EMainPanel.MIDDLE.getMinimumValue()
        binding.middle.knob.setOnStateChanged {
            binding.middle.knobValue.text = it.toString()
            handleChange(EMainPanel.MIDDLE, it)
        }

        binding.treble.name = "Treble"
        binding.treble.knob.numberOfStates =
            1 + EMainPanel.TREBLE.getMaximumValue() - EMainPanel.TREBLE.getMinimumValue()
        binding.treble.knob.setOnStateChanged {
            binding.treble.knobValue.text = it.toString()
            handleChange(EMainPanel.TREBLE, it)
        }


        binding.cabinet.name = "Cabinet"
        binding.cabinet.knobValue.textSize = 8f
        binding.cabinet.knob.numberOfStates =
            1 + EMainPanel.CABINET.getMaximumValue() - EMainPanel.CABINET.getMinimumValue()
        binding.cabinet.knob.setOnStateChanged {
            binding.cabinet.knobValue.text =
                ECabinetType.fromId(it.toByte()).toString().replace("_", "\n")
            handleChange(EMainPanel.CABINET, it)
        }

        binding.strings = listOf("Test", "Test1", "Test2", "Test3").toTypedArray()
        return view
    }

    private fun setPreset(preset: Preset) {
        messageSend = false
        binding.gain.knob.state = preset.mainPanel.gain.toInt()
        binding.bass.knob.state = preset.mainPanel.bass.toInt()
        binding.master.knob.state = preset.mainPanel.master.toInt()
        binding.middle.knob.state = preset.mainPanel.middle.toInt()
        binding.treble.knob.state = preset.mainPanel.treble.toInt()

        binding.ampType.knob.state = preset.mainPanel.amp.id.toInt()
        binding.cabinet.knob.state = preset.mainPanel.cabinet?.id?.toInt() ?: 0
        messageSend = true
    }

    private fun handleChange(property: IControlProperty, value: Int) {
        val changeMessage = ChangeMessage(property.getPropertyId(), property.getMinimumValue() + value)

        if (viewModel.activePresetIndex.value!! != -1) {
            viewModel.presets.value?.get(viewModel.activePresetIndex.value!!)
                ?.processChangeMessage(changeMessage)
        }
        if (messageSend) {
            viewModel.sender?.sendMessage(
                BtChangeMessage(
                    EMessageType.CHANGE,
                    changeMessage
                )
            )
        }
    }

}