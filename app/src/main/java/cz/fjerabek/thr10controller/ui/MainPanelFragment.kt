package cz.fjerabek.thr10controller.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import cz.fjerabek.thr10controller.R
import cz.fjerabek.thr10controller.databinding.MainPanelFragmentBinding

class MainPanelFragment : Fragment() {

    companion object {
        fun newInstance() = MainPanelFragment()
    }

    private lateinit var viewModel: MainPanelViewModel
    private lateinit var binding : MainPanelFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.main_panel_fragment, container, false)
        binding = DataBindingUtil.bind(view)!!

        binding.knob.setOnStateChanged {
            binding.knobValue.text = it.toString()
        }

        binding.strings = listOf("Test", "Test1", "Test2", "Test3").toTypedArray()
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainPanelViewModel::class.java)
        // TODO: Use the ViewModel
    }

}