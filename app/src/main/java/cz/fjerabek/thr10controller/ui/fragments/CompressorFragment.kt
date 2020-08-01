package cz.fjerabek.thr10controller.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.CompoundButton
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import cz.fjerabek.thr10controller.R
import cz.fjerabek.thr10controller.data.controls.Compressor
import cz.fjerabek.thr10controller.data.controls.IDCompressor
import cz.fjerabek.thr10controller.data.controls.compressor.Stomp
import cz.fjerabek.thr10controller.data.enums.EStatus
import cz.fjerabek.thr10controller.data.enums.IControlProperty
import cz.fjerabek.thr10controller.data.enums.compressor.ECompressor
import cz.fjerabek.thr10controller.data.enums.compressor.ECompressorType
import cz.fjerabek.thr10controller.data.message.bluetooth.BtChangeMessage
import cz.fjerabek.thr10controller.data.message.bluetooth.EMessageType
import cz.fjerabek.thr10controller.data.message.midi.ChangeMessage
import cz.fjerabek.thr10controller.databinding.CompressorFragmentBinding
import cz.fjerabek.thr10controller.ui.CustomKnob
import timber.log.Timber

class CompressorFragment : Fragment() {
    private lateinit var viewModel: PresetViewModel
    private lateinit var binding: CompressorFragmentBinding
    private var stateChangeListener = CompoundButton.OnCheckedChangeListener {_, checked ->
        handleChange(
            ECompressor.STATUS,
            if (checked) EStatus.ON.value.toInt() else EStatus.OFF.value.toInt()
        )
        viewModel.activePreset.value?.compressor?.status = if (checked) EStatus.ON else EStatus.OFF
    }

    private var typeChangeListener =  object : AdapterView.OnItemSelectedListener {
        override fun onNothingSelected(p0: AdapterView<*>?) {
            Timber.d("Nothing selected")
        }

        override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
            val changeMessage = ChangeMessage(IDCompressor.TYPE,
                (p0?.adapter?.getItem(p2)!! as ECompressorType).id.toInt()
            )
            viewModel.activePreset.value?.processChangeMessage(changeMessage)
            viewModel.activePreset.postValue(viewModel.activePreset.value)

            viewModel.sender?.sendMessage(BtChangeMessage(EMessageType.CHANGE, changeMessage))
        }
    }

    companion object {
        fun getInstance(): CompressorFragment {
            return CompressorFragment()
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(requireActivity()).get(PresetViewModel::class.java)
        val view = inflater.inflate(R.layout.compressor_fragment, container, false)
        binding = DataBindingUtil.bind(view)!!

        val items = listOf(ECompressorType.STOMP, ECompressorType.RACK)
        val dataAdapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, items)
        binding.compressorTypeSelect.adapter = dataAdapter

//        binding.compressorTypeSelect.onItemSelectedListener = typeChangeListener

        viewModel.activePreset.observe(viewLifecycleOwner) {
            //Prevent calling set change listener (Sending back bluetooth change messages)
            binding.compressorStatus.setOnCheckedChangeListener(null)
//            binding.compressorTypeSelect.onItemSelectedListener = null

            binding.compressor = it.compressor ?: Compressor(EStatus.OFF, Stomp(0, 0))
            if(it.compressor?.specific?.type!!.id != (binding.compressorTypeSelect.selectedItem as ECompressorType).id && it.compressor != null) {
                Timber.d("Value change set: new: ${binding.compressorTypeSelect.selectedItem} old: ${it.compressor?.specific?.type}")
                binding.compressorTypeSelect.setSelection(items.indexOf(it.compressor?.specific?.type!!))
            }

//            binding.compressorTypeSelect.onItemSelectedListener = typeChangeListener
            binding.compressorStatus.setOnCheckedChangeListener(stateChangeListener)
        }


        val defaultListener: (value : Int, knob: CustomKnob) -> Unit = { value, knob ->
            handleChange(knob.property!!, value)
        }

        binding.attack.valueChangeListener = defaultListener
        binding.knee.valueChangeListener = defaultListener
        binding.output.valueChangeListener = defaultListener
        binding.ratio.valueChangeListener = defaultListener
        binding.release.valueChangeListener = defaultListener
        binding.threshold.valueChangeListener = defaultListener


        binding.compressorStatus.setOnCheckedChangeListener(stateChangeListener)

        return view
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

    override fun onStart() {
        super.onStart()
    }

}