package cz.fjerabek.thr10controller.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import cz.fjerabek.thr10controller.R
import cz.fjerabek.thr10controller.data.message.MessageSender
import cz.fjerabek.thr10controller.data.message.bluetooth.BtRequestMessage
import cz.fjerabek.thr10controller.data.message.bluetooth.EMessageType
import cz.fjerabek.thr10controller.databinding.MainPanelFragmentBinding
import timber.log.Timber

class MainPanelFragment : Fragment() {

    companion object {
        fun getInstance(sender : MessageSender): MainPanelFragment {
            val instance =
                MainPanelFragment()
            instance.sender = sender
            return instance
        }
    }

    private lateinit var sender : MessageSender
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
            Timber.d("Test logger test $it")
            sender.sendMessage(BtRequestMessage(EMessageType.GET_STATUS))
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