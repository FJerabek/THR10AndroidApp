package cz.fjerabek.thr10controller.viewmodels

import android.bluetooth.BluetoothDevice
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import cz.fjerabek.thr10controller.databinding.BluetoothListRowLayoutBinding
import cz.fjerabek.thr10controller.ui.adapters.BluetoothDeviceWrapper

class MainActivityViewModel : ViewModel() {
    val devices: MutableLiveData<List<BluetoothDeviceWrapper>> by lazy {
        MutableLiveData(emptyList())
    }
}