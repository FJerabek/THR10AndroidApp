package cz.fjerabek.thr10controller.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import cz.fjerabek.thr.data.controls.TypeConverter
import cz.fjerabek.thr10controller.databinding.ReverbFragmentBinding
import cz.fjerabek.thr10controller.viewmodels.ControlActivityViewModel

/**
 * Fragment providing UI for Reverb configuration
 */
class ReverbFragment : Fragment() {
    /**
     * Control View model
     */
    private lateinit var viewModel: ControlActivityViewModel

    /**
     * GUI data binding
     */
    private lateinit var binding: ReverbFragmentBinding

    companion object {
        /**
         * Returns instance of [ReverbFragment]
         * @return instance of [ReverbFragment]
         */
        fun getInstance(): ReverbFragment {
            return ReverbFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(requireActivity()).get(ControlActivityViewModel::class.java)
        binding = ReverbFragmentBinding.inflate(inflater)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        binding.converter = TypeConverter

        return binding.root
    }
}