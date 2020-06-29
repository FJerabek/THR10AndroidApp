package cz.fjerabek.thr10controller.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import cz.fjerabek.thr10controller.R
import cz.fjerabek.thr10controller.data.message.MessageSender

class EffectFragment : Fragment() {

    companion object {
        fun getInstance(sender : MessageSender): EffectFragment {
            val instance =
                EffectFragment()
            instance.sender = sender
            return instance
        }
    }

    private lateinit var sender: MessageSender
    private lateinit var viewModel: EffectViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.effect_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(EffectViewModel::class.java)
        // TODO: Use the ViewModel
    }

}