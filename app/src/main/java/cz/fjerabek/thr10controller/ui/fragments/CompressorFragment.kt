package cz.fjerabek.thr10controller.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import cz.fjerabek.thr.data.controls.compressor.Rack
import cz.fjerabek.thr.data.controls.compressor.Stomp
import cz.fjerabek.thr.data.enums.compressor.ECompressorType
import cz.fjerabek.thr10controller.R
import cz.fjerabek.thr10controller.databinding.CompressorFragmentBinding
import cz.fjerabek.thr10controller.viewmodels.ControlActivityViewModel
import timber.log.Timber

class CompressorFragment : Fragment() {
    private lateinit var viewModel: ControlActivityViewModel
    private lateinit var binding: CompressorFragmentBinding

    companion object {
        fun getInstance(): CompressorFragment {
            return CompressorFragment()
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(requireActivity()).get(ControlActivityViewModel::class.java)
        binding = CompressorFragmentBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        return binding.root
    }
}