package cz.fjerabek.thr10controller.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.WindowDecorActionBar
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import cz.fjerabek.thr10controller.R
import cz.fjerabek.thr10controller.data.controls.IDCompressor
import cz.fjerabek.thr10controller.data.controls.IDEffect
import cz.fjerabek.thr10controller.data.enums.EStatus
import cz.fjerabek.thr10controller.data.enums.IControlProperty
import cz.fjerabek.thr10controller.data.enums.compressor.ECompressor
import cz.fjerabek.thr10controller.data.enums.effect.EEffect
import cz.fjerabek.thr10controller.data.enums.effect.EEffectType
import cz.fjerabek.thr10controller.data.message.bluetooth.BtChangeMessage
import cz.fjerabek.thr10controller.data.message.bluetooth.EMessageType
import cz.fjerabek.thr10controller.data.message.midi.ChangeMessage
import cz.fjerabek.thr10controller.databinding.EffectFragmentBinding
import cz.fjerabek.thr10controller.ui.CustomKnob
import timber.log.Timber

class EffectFragment : Fragment() {
    private lateinit var viewModel: PresetViewModel
    private lateinit var binding : EffectFragmentBinding

    companion object {
        fun getInstance(): EffectFragment {
            return EffectFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(requireActivity()).get(PresetViewModel::class.java)

        binding = EffectFragmentBinding.inflate(inflater, container, false)

        val items = listOf(EEffectType.CHORUS, EEffectType.FLANGER, EEffectType.PHASER, EEffectType.TREMOLO)

        val dataAdapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, items)

        viewModel.activePreset.observe(viewLifecycleOwner) {
            binding.effect = it.effect
            binding.effectSelect.setSelection(items.indexOf(it.effect?.specific?.type), true)
        }

        binding.effectSelect.adapter = dataAdapter
        binding.effectSelect.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
                Timber.e("Effect type nothing selected")
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                val changeMessage = ChangeMessage(
                    IDEffect.TYPE,
                    dataAdapter.getItem(p2)!!.id.toInt()
                )
                viewModel.activePreset.value?.processChangeMessage(changeMessage)
                viewModel.activePreset.postValue(viewModel.activePreset.value)
                viewModel.sender?.sendMessage(BtChangeMessage(EMessageType.CHANGE, changeMessage))
            }

        }


        binding.effectStatus.setOnCheckedChangeListener { _, checked ->
            handleChange(
                EEffect.STATUS,
                if (checked) EStatus.ON.value.toInt() else EStatus.OFF.value.toInt()
            )
            viewModel.activePreset.value?.effect?.status = if (checked) EStatus.ON else EStatus.OFF
        }


        val defaultListener: (value : Int, knob: CustomKnob) -> Unit = { value, knob ->
            handleChange(knob.property!!, value)
        }

        //Chorus
        binding.chorus.depth.valueChangeListener = defaultListener
        binding.chorus.mix.valueChangeListener = defaultListener
        binding.chorus.speed.valueChangeListener = defaultListener

        //Flanger
        binding.flanger.depth.valueChangeListener = defaultListener
        binding.flanger.feedback.valueChangeListener = defaultListener
        binding.flanger.manual.valueChangeListener = defaultListener
        binding.flanger.speed.valueChangeListener = defaultListener
        binding.flanger.spread.valueChangeListener = defaultListener

        //Phaser
        binding.phaser.depth.valueChangeListener = defaultListener
        binding.phaser.feedback.valueChangeListener = defaultListener
        binding.phaser.manual.valueChangeListener = defaultListener
        binding.phaser.speed.valueChangeListener = defaultListener

        //Tremolo
        binding.tremolo.freq.valueChangeListener = defaultListener
        binding.tremolo.depth.valueChangeListener = defaultListener

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