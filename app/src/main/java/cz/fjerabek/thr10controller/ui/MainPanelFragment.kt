package cz.fjerabek.thr10controller.ui

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import cz.fjerabek.thr10controller.R

class MainPanelFragment : Fragment() {

    companion object {
        fun newInstance() = MainPanelFragment()
    }

    private lateinit var viewModel: MainPanelViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.main_panel_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainPanelViewModel::class.java)
        // TODO: Use the ViewModel
    }

}