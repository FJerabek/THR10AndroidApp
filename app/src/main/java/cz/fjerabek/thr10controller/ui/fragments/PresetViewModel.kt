package cz.fjerabek.thr10controller.ui.fragments

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import cz.fjerabek.thr10controller.data.Preset
import cz.fjerabek.thr10controller.data.controls.MainPanel
import cz.fjerabek.thr10controller.data.enums.mainpanel.EAmpType
import cz.fjerabek.thr10controller.data.message.bluetooth.IBtMessageSender

class PresetViewModel : ViewModel() {
    val presets: MutableLiveData<ArrayList<Preset>> by lazy {
        MutableLiveData<ArrayList<Preset>>(ArrayList())
    }

    val activePreset: MutableLiveData<Preset> by lazy {
        MutableLiveData<Preset>(
            Preset(
                "Dump preset",
                MainPanel(EAmpType.CLEAN, 0, 0, 0, 0, 0, null)
            )
        )
    }

    var sender: IBtMessageSender? = null
}