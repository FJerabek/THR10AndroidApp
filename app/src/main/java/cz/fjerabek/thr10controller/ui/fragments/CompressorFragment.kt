package cz.fjerabek.thr10controller.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import cz.fjerabek.thr.data.controls.TypeConverter
import cz.fjerabek.thr10controller.databinding.CompressorFragmentBinding
import cz.fjerabek.thr10controller.viewmodels.ControlActivityViewModel

/**
 * Application fragment providing user interface for compressor configuration
 */
class CompressorFragment : Fragment() {
    /**
     * Control view model
     */
    private lateinit var viewModel: ControlActivityViewModel

    /**
     * GUI data binding
     */
    private lateinit var binding: CompressorFragmentBinding

    companion object {
        /**
         * Creates new instance of fragment
         * @return new instance of fragment
         */
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
        binding.converter = TypeConverter

        return binding.root
    }
}