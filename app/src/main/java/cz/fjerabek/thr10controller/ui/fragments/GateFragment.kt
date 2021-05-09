package cz.fjerabek.thr10controller.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import cz.fjerabek.thr10controller.databinding.GateFragmentBinding
import cz.fjerabek.thr10controller.viewmodels.ControlActivityViewModel

/**
 * Fragment providing UI for configuration of gate
 */
class GateFragment : Fragment() {
    /**
     * Control view Model
     */
    private lateinit var viewModel: ControlActivityViewModel

    /**
     * UI data binding
     */
    private lateinit var binding: GateFragmentBinding

    companion object {
        /**
         * Gets instance of [GateFragment]
         * @return returns instance of [GateFragment]
         */
        fun getInstance(): GateFragment {
            return GateFragment()
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(requireActivity()).get(ControlActivityViewModel::class.java)
        binding = GateFragmentBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        return binding.root
    }
}