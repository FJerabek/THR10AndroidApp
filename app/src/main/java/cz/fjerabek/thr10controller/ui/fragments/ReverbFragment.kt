package cz.fjerabek.thr10controller.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import cz.fjerabek.thr10controller.R
import cz.fjerabek.thr10controller.data.message.MessageSender

class ReverbFragment : Fragment() {

    companion object {
        fun getInstance(sender : MessageSender): ReverbFragment {
            val instance =
                ReverbFragment()
            instance.sender = sender
            return instance
        }
    }

    private lateinit var sender: MessageSender
    private lateinit var viewModel: ReverbViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.reverb_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ReverbViewModel::class.java)
        // TODO: Use the ViewModel
    }

}