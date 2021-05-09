package cz.fjerabek.thr10controller.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import cz.fjerabek.thr10controller.ui.adapters.BluetoothDeviceWrapper

/**
 * View model holding connectable devices
 */
class MainActivityViewModel : ViewModel() {
    /**
     * Paired devices
     */
    val devices: MutableLiveData<List<BluetoothDeviceWrapper>> by lazy {
        MutableLiveData(emptyList())
    }
}