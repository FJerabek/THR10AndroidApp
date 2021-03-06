package cz.fjerabek.thr10controller.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import cz.fjerabek.thr.data.enums.mainpanel.EAmpType
import cz.fjerabek.thr.data.enums.mainpanel.ECabinetType
import cz.fjerabek.thr10controller.R
import cz.fjerabek.thr10controller.databinding.MainPanelFragmentBinding
import cz.fjerabek.thr10controller.viewmodels.ControlActivityViewModel
import timber.log.Timber

class MainPanelFragment : Fragment() {
    private lateinit var viewModel: ControlActivityViewModel
    private lateinit var binding: MainPanelFragmentBinding

    companion object {
        fun getInstance(): MainPanelFragment {
            return MainPanelFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(requireActivity()).get(ControlActivityViewModel::class.java)
        binding = MainPanelFragmentBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        binding.amp.valueStringConverter = {
            EAmpType.fromId(it.toByte()).toString()
        }

        binding.cabinet.valueStringConverter = {
            ECabinetType.fromId(it.toByte()).toString().replace("_", "\n")
        }
        return binding.root
    }
}