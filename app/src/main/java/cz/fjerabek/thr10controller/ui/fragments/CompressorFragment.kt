package cz.fjerabek.thr10controller.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import cz.fjerabek.thr10controller.R
import cz.fjerabek.thr10controller.data.controls.compressor.Rack
import cz.fjerabek.thr10controller.data.controls.compressor.Stomp
import cz.fjerabek.thr10controller.data.enums.compressor.ECompressor
import cz.fjerabek.thr10controller.data.enums.compressor.ECompressorType
import cz.fjerabek.thr10controller.data.message.bluetooth.BtChangeMessage
import cz.fjerabek.thr10controller.data.message.bluetooth.EMessageType
import cz.fjerabek.thr10controller.data.message.midi.ChangeMessage
import cz.fjerabek.thr10controller.databinding.CompressorFragmentBinding
import timber.log.Timber
import javax.crypto.ExemptionMechanismException

class CompressorFragment : Fragment() {
    private lateinit var viewModel: PresetViewModel
    private lateinit var binding: CompressorFragmentBinding

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



        return view
    }

    override fun onStart() {
        super.onStart()
    }

}