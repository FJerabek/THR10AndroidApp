package cz.fjerabek.thr10controller.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import cz.fjerabek.thr.data.enums.compressor.ECompressorType
import cz.fjerabek.thr.data.enums.effect.EEffectType
import cz.fjerabek.thr.data.enums.reverb.EReverbType
import cz.fjerabek.thr.data.midi.PresetMessage
import cz.fjerabek.thr.data.uart.FWVersionMessage
import cz.fjerabek.thr.data.uart.StatusMessage

class ControlActivityViewModel(application: Application) : AndroidViewModel(application) {

    val fwVersion: MutableLiveData<FWVersionMessage> = MutableLiveData()
    val hwStatus: MutableLiveData<StatusMessage> = MutableLiveData()
    val presets: MutableLiveData<MutableList<PresetMessage>> = MutableLiveData(mutableListOf())
    val activePreset: MutableLiveData<PresetMessage> = MutableLiveData()
    val changeMessageCallback: MutableLiveData<((Byte, Int) -> Unit)?> = MutableLiveData()
    val activePresetIndex: MutableLiveData<Int> = MutableLiveData(-1)
    val presetChanged: MutableLiveData<Boolean> = MutableLiveData(false)

    val compressorTypes
        get() = ECompressorType.values().toList()

    val reverbTypes
        get() = EReverbType.values().toList()

    val effectTypes
        get() = EEffectType.values().toList()

    fun addPreset() {
        val newPresets = presets.value?.toMutableList();
        val preset = activePreset.value!!
        newPresets?.add(preset)
        presets.value = newPresets
    }
}