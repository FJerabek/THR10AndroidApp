package cz.fjerabek.thr10controller.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import cz.fjerabek.thr10controller.R
import cz.fjerabek.thr10controller.data.controls.IDEffect
import cz.fjerabek.thr10controller.data.controls.IDReverb
import cz.fjerabek.thr10controller.data.enums.EStatus
import cz.fjerabek.thr10controller.data.enums.IControlProperty
import cz.fjerabek.thr10controller.data.enums.effect.EEffect
import cz.fjerabek.thr10controller.data.enums.effect.EEffectType
import cz.fjerabek.thr10controller.data.enums.reverb.EReverb
import cz.fjerabek.thr10controller.data.enums.reverb.EReverbType
import cz.fjerabek.thr10controller.data.message.bluetooth.BtChangeMessage
import cz.fjerabek.thr10controller.data.message.bluetooth.EMessageType
import cz.fjerabek.thr10controller.data.message.midi.ChangeMessage
import cz.fjerabek.thr10controller.databinding.ReverbFragmentBinding
import cz.fjerabek.thr10controller.databinding.ReverbFragmentBindingImpl
import timber.log.Timber

class ReverbFragment : Fragment() {
    private lateinit var viewModel: PresetViewModel
    private lateinit var binding: ReverbFragmentBinding

    private var changeListen = true;

    companion object {
        fun getInstance(): ReverbFragment {
            return ReverbFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(requireActivity()).get(PresetViewModel::class.java)
        binding = ReverbFragmentBinding.inflate(inflater)

        val items = listOf(EReverbType.HALL, EReverbType.PLATE, EReverbType.ROOM, EReverbType.SPRING)

        val dataAdapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, items)

        viewModel.activePreset.observe(viewLifecycleOwner) {
            changeListen = false
            binding.reverb = it.reverb
            binding.reverbSelect.setSelection(items.indexOf(it.reverb?.specific?.type), true)
            changeListen = true
        }

        binding.reverbSelect.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
                Timber.e("Effect type nothing selected")
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                if(!changeListen) return
                val changeMessage = ChangeMessage(
                    IDReverb.TYPE,
                    dataAdapter.getItem(p2)!!.id.toInt()
                )
                viewModel.activePreset.value?.processChangeMessage(changeMessage)
                viewModel.activePreset.postValue(viewModel.activePreset.value)
                viewModel.sender?.sendMessage(BtChangeMessage(EMessageType.CHANGE, changeMessage))
            }

        }

        binding.reverbStatus.setOnCheckedChangeListener { _, checked ->
            handleChange(
                EReverb.STATUS,
                if (checked) EStatus.ON.value.toInt() else EStatus.OFF.value.toInt()
            )
            viewModel.activePreset.value?.effect?.status = if (checked) EStatus.ON else EStatus.OFF
        }

        binding.reverbSelect.adapter = dataAdapter

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