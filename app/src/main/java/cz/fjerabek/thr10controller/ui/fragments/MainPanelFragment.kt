package cz.fjerabek.thr10controller.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import cz.fjerabek.thr10controller.R
import cz.fjerabek.thr10controller.data.enums.mainpanel.EAmpType
import cz.fjerabek.thr10controller.data.enums.mainpanel.ECabinetType
import cz.fjerabek.thr10controller.data.enums.mainpanel.EMainPanel
import cz.fjerabek.thr10controller.data.message.MessageSender
import cz.fjerabek.thr10controller.databinding.MainPanelFragmentBinding

class MainPanelFragment : Fragment() {

    companion object {
        fun getInstance(sender : MessageSender): MainPanelFragment {
            val instance =
                MainPanelFragment()
            instance.sender = sender
            return instance
        }
    }

    private lateinit var sender : MessageSender
    private lateinit var binding : MainPanelFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.main_panel_fragment, container, false)
        binding = DataBindingUtil.bind(view)!!

//        binding.knob.setOnStateChanged {
//            binding.knobValue.text = it.toString()
//            Timber.d("Test logger test $it")
//            sender.sendMessage(BtRequestMessage(EMessageType.GET_STATUS))
//        }

        binding.gain.name = "Gain"
        binding.gain.knob.numberOfStates = 1 + EMainPanel.GAIN.getMaximumValue() - EMainPanel.GAIN.getMinimumValue()
        binding.gain.knob.setOnStateChanged {
            binding.gain.knobValue.text = it.toString()
        }

        binding.ampType.name = "Amp type"
        binding.ampType.knobValue.textSize = 10f
        binding.ampType.knob.numberOfStates = 1 + EMainPanel.AMP.getMaximumValue() - EMainPanel.AMP.getMinimumValue()
        binding.ampType.knob.setOnStateChanged {
            binding.ampType.knobValue.text = EAmpType.fromId(it.toByte()).toString()
        }

        binding.bass.name = "Bass"
        binding.bass.knob.numberOfStates = 1 + EMainPanel.BASS.getMaximumValue() - EMainPanel.BASS.getMinimumValue()
        binding.bass.knob.setOnStateChanged {
            binding.bass.knobValue.text = it.toString()
        }

        binding.master.name = "Master"
        binding.master.knob.numberOfStates = 1 + EMainPanel.MASTER.getMaximumValue() - EMainPanel.MASTER.getMinimumValue()
        binding.master.knob.setOnStateChanged {
            binding.master.knobValue.text = it.toString()
        }

        binding.middle.name = "Middle"
        binding.middle.knob.numberOfStates = 1 + EMainPanel.MIDDLE.getMaximumValue() - EMainPanel.MIDDLE.getMinimumValue()
        binding.middle.knob.setOnStateChanged {
            binding.middle.knobValue.text = it.toString()
        }

        binding.treble.name = "Treble"
        binding.treble.knob.numberOfStates = 1 + EMainPanel.TREBLE.getMaximumValue() - EMainPanel.TREBLE.getMinimumValue()
        binding.treble.knob.setOnStateChanged {
            binding.treble.knobValue.text = it.toString()
        }


        binding.cabinet.name = "Cabinet"
        binding.cabinet.knobValue.textSize = 8f

        binding.cabinet.knob.numberOfStates = 1 + EMainPanel.CABINET.getMaximumValue() - EMainPanel.CABINET.getMinimumValue()
        binding.cabinet.knob.setOnStateChanged {
            binding.cabinet.knobValue.text = ECabinetType.fromId(it.toByte()).toString().replace("_", "\n")
        }

        binding.strings = listOf("Test", "Test1", "Test2", "Test3").toTypedArray()
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        // TODO: Use the ViewModel
    }

}