package cz.fjerabek.thr10controller

import android.bluetooth.BluetoothDevice
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import cz.fjerabek.thr10controller.bluetooth.BluetoothService
import cz.fjerabek.thr10controller.databinding.ActivityControlBinding
import cz.fjerabek.thr10controller.databinding.ActivityControlBindingImpl
import cz.fjerabek.thr10controller.databinding.ActivityMainBinding
import cz.fjerabek.thr10controller.ui.MainPanelFragment
import me.aflak.bluetooth.interfaces.DeviceCallback
import me.aflak.bluetooth.interfaces.DiscoveryCallback

private const val NUM_PAGES = 2
private val pageTitles = listOf("Main panel", "Compressor", "Delay", "Effect", "Gate", "Reverb")

class ControlActivity : FragmentActivity() {

    lateinit var bluetoothService : BluetoothService
    lateinit var binding : ActivityControlBinding

    private var serviceConnection = object : ServiceConnection {
        override fun onServiceDisconnected(p0: ComponentName?) {

        }

        override fun onServiceConnected(p0: ComponentName?, binder: IBinder?) {
            binder as BluetoothService.Binder
            bluetoothService = binder.getService()

            /* Set bluetooth service */
            bluetoothService.setDeviceCallback(object : DeviceCallback {
                override fun onDeviceDisconnected(device: BluetoothDevice?, message: String?) {
                }

                override fun onDeviceConnected(device: BluetoothDevice?) {
                }

                override fun onConnectError(device: BluetoothDevice?, message: String?) {
                }

                override fun onMessage(message: ByteArray?) {
                    Log.i("Bluetooth", "Bluetooth message  $message")
                }

                override fun onError(errorCode: Int) {
                    Log.e("Bluetooth", "Device error. Error code: $errorCode")
                }
            })
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_control)

        binding.viewPager.adapter = ViewPagerAdapter(this)
        TabLayoutMediator(binding.tabs, binding.viewPager) {
                tab, position -> tab.text = pageTitles[position]
        }.attach()

        Intent(this, BluetoothService::class.java).also { intent ->
            bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
        }
    }

    override fun onDestroy() {
        super.onDestroy()

    }

    inner class ViewPagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

        override fun getItemCount(): Int {
            return NUM_PAGES
        }

        override fun createFragment(position: Int): Fragment = when(position) {
            1 -> MainPanelFragment()
            else -> MainPanelFragment()
        }

    }
}