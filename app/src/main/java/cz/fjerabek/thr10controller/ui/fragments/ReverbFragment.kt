package cz.fjerabek.thr10controller.ui.fragments

import android.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import cz.fjerabek.thr.data.enums.reverb.EReverbType
import cz.fjerabek.thr10controller.databinding.ReverbFragmentBinding
import cz.fjerabek.thr10controller.viewmodels.ControlActivityViewModel
import timber.log.Timber

class ReverbFragment : Fragment() {
    private lateinit var viewModel: ControlActivityViewModel
    private lateinit var binding: ReverbFragmentBinding

    private var changeListen = true;

    companion object {
        fun getInstance(): ReverbFragment {
            return ReverbFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(requireActivity()).get(ControlActivityViewModel::class.java)
        binding = ReverbFragmentBinding.inflate(inflater)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

//        val items = listOf(EReverbType.HALL, EReverbType.PLATE, EReverbType.ROOM, EReverbType.SPRING)
//
//        val dataAdapter =
//            ArrayAdapter(requireContext(), R.layout.simple_spinner_dropdown_item, items)

//        viewModel.activePreset.observe(viewLifecycleOwner) {
//            changeListen = false
//            binding.reverb = it.reverb
//            binding.reverbSelect.setSelection(items.indexOf(it.reverb?.specific?.type), true)
//            changeListen = true
//        }
//
//        binding.reverbSelect.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
//            override fun onNothingSelected(p0: AdapterView<*>?) {
//                Timber.e("Effect type nothing selected")
//            }
//
//            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
//                if(!changeListen) return
//                val changeMessage = ChangeMessage(
//                    IDReverb.TYPE,
//                    dataAdapter.getItem(p2)!!.id.toInt()
//                )
//                viewModel.activePreset.value?.processChangeMessage(changeMessage)
//                viewModel.activePreset.postValue(viewModel.activePreset.value)
//                viewModel.sender?.sendMessage(BtChangeMessage(EMessageType.CHANGE, changeMessage))
//            }
//
//        }
//
//        binding.reverbStatus.setOnCheckedChangeListener { _, checked ->
//            handleChange(
//                EReverb.STATUS,
//                if (checked) EStatus.ON.value.toInt() else EStatus.OFF.value.toInt()
//            )
//            viewModel.activePreset.value?.effect?.status = if (checked) EStatus.ON else EStatus.OFF
//        }
//
//        binding.reverbSelect.adapter = dataAdapter

        return binding.root
    }


//    private fun handleChange(property: IControlProperty, value: Int) {
//        val changeMessage = ChangeMessage(property.getPropertyId(), value)
//        viewModel.sender?.sendMessage(
//            BtChangeMessage(
//                EMessageType.CHANGE,
//                changeMessage
//            )
//        )
//    }
}