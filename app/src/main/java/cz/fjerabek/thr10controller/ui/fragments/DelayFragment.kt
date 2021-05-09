package cz.fjerabek.thr10controller.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import cz.fjerabek.thr10controller.databinding.DelayFragmentBinding
import cz.fjerabek.thr10controller.viewmodels.ControlActivityViewModel

/**
 * Fragment providing UI for delay configuration
 */
class DelayFragment : Fragment() {
    /**
     * Control view model
     */
    private lateinit var viewModel: ControlActivityViewModel

    /**
     * GUI data binding
     */
    private lateinit var binding: DelayFragmentBinding

    companion object {
        /**
         * Gets instance of fragment
         * @return instance of Delay fragment
         */
        fun getInstance(): DelayFragment {
            return DelayFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(requireActivity()).get(ControlActivityViewModel::class.java)
        binding = DelayFragmentBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        return binding.root
    }
}