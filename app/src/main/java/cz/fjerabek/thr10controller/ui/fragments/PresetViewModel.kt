package cz.fjerabek.thr10controller.ui.fragments

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import cz.fjerabek.thr10controller.data.Preset

class PresetViewModel : ViewModel() {
    var presetLive = MutableLiveData<ArrayList<Preset>>()
    val activePresetIndex = MutableLiveData<Int>()
}