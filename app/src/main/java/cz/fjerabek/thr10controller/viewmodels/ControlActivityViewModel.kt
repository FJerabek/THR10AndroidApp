package cz.fjerabek.thr10controller.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import cz.fjerabek.thr.data.controls.Delay
import cz.fjerabek.thr.data.controls.Gate
import cz.fjerabek.thr.data.controls.MainPanel
import cz.fjerabek.thr.data.controls.compressor.Stomp
import cz.fjerabek.thr.data.controls.effect.Chorus
import cz.fjerabek.thr.data.controls.reverb.Spring
import cz.fjerabek.thr.data.enums.EStatus
import cz.fjerabek.thr.data.enums.compressor.ECompressor
import cz.fjerabek.thr.data.enums.compressor.ECompressorType
import cz.fjerabek.thr.data.enums.compressor.EStomp
import cz.fjerabek.thr.data.enums.delay.EDelay
import cz.fjerabek.thr.data.enums.effect.EChorus
import cz.fjerabek.thr.data.enums.effect.EEffect
import cz.fjerabek.thr.data.enums.effect.EEffectType
import cz.fjerabek.thr.data.enums.gate.EGate
import cz.fjerabek.thr.data.enums.mainpanel.EAmpType
import cz.fjerabek.thr.data.enums.mainpanel.ECabinetType
import cz.fjerabek.thr.data.enums.mainpanel.EMainPanel
import cz.fjerabek.thr.data.enums.reverb.EReverb
import cz.fjerabek.thr.data.enums.reverb.EReverbType
import cz.fjerabek.thr.data.enums.reverb.ESpring
import cz.fjerabek.thr.data.midi.PresetMessage
import cz.fjerabek.thr.data.uart.ECharging
import cz.fjerabek.thr.data.uart.FWVersionMessage
import cz.fjerabek.thr.data.uart.StatusMessage

/**
 * View model containing combo state and preset values.
 * @param application application instance
 */
class ControlActivityViewModel(application: Application) : AndroidViewModel(application) {

    /**
     * Firmware version
     */
    val fwVersion: MutableLiveData<FWVersionMessage> = MutableLiveData()

    /**
     * Last hw status message received
     */
    val hwStatus: MutableLiveData<StatusMessage> =
        MutableLiveData(StatusMessage(0, 0, ECharging.DISCHARGING, 0))

    /**
     * Available user presets
     */
    val presets: MutableLiveData<MutableList<PresetMessage>> = MutableLiveData(mutableListOf())

    /**
     * Currently active preset
     */
    val activePreset: MutableLiveData<PresetMessage> = MutableLiveData(
        PresetMessage(
            "Default",
            MainPanel(
                EAmpType.CLEAN,
                EMainPanel.GAIN.min,
                EMainPanel.MASTER.min,
                EMainPanel.BASS.min,
                EMainPanel.MIDDLE.min,
                EMainPanel.TREBLE.min,
                ECabinetType.CAB_4X10
            ),
            Stomp(
                EStatus.OFF,
                EStomp.SUSTAIN.min,
                EStomp.OUTPUT.min,
            ),
            Chorus(
                EStatus.OFF,
                EChorus.SPEED.min,
                EChorus.DEPTH.min,
                EChorus.MIX.min,
            ),
            Delay(
                EStatus.OFF,
                EDelay.TIME.min.toInt(),
                EDelay.FEEDBACK.min.toByte(),
                EDelay.HIGH_CUT.min.toInt(),
                EDelay.LOW_CUT.min.toInt(),
                EDelay.LEVEL.min.toByte(),
            ),
            Spring(
                EStatus.OFF,
                ESpring.REVERB.min,
                ESpring.FILTER.min
            ),
            Gate(
                EStatus.OFF,
                EGate.THRESHOLD.min,
                EGate.RELEASE.min,
            )
        )
    )

    /**
     * UI value change callback
     */
    val changeMessageCallback: MutableLiveData<((Byte, Int) -> Unit)?> = MutableLiveData()

    /**
     * Type value changed handler
     */
    fun changeTypeCallback(id: Byte, value: Int) {
        when (id) {
            EEffect.TYPE.propertyId -> {
                if (activePreset.value?.effect?.effectType?.id?.toInt() != value) {
                    changeMessageCallback.value?.invoke(id, value)
                    changeMessageCallback.value?.invoke(
                        EEffect.STATUS.propertyId,
                        activePreset.value?.compressor?.status?.value?.toInt()
                            ?: EStatus.OFF.value.toInt()
                    )
                }
            }
            EReverb.TYPE.propertyId -> {
                if (activePreset.value?.reverb?.reverbType?.id?.toInt() != value) {
                    changeMessageCallback.value?.invoke(id, value)
                    changeMessageCallback.value?.invoke(
                        EReverb.STATUS.propertyId,
                        activePreset.value?.reverb?.status?.value?.toInt()
                            ?: EStatus.OFF.value.toInt()
                    )
                }
            }
            ECompressor.TYPE.propertyId -> {
                if (activePreset.value?.compressor?.compressorType?.id?.toInt() != value) {
                    changeMessageCallback.value?.invoke(id, value)
                    changeMessageCallback.value?.invoke(
                        ECompressor.STATUS.propertyId,
                        activePreset.value?.compressor?.status?.value?.toInt()
                            ?: EStatus.OFF.value.toInt()
                    )
                }
            }
        }
    }

    /**
     * Index of active preset
     */
    val activePresetIndex: MutableLiveData<Int> = MutableLiveData(-1)

    /**
     * Was preset changed. Show revert menu
     */
    val presetChanged: MutableLiveData<Boolean> = MutableLiveData(false)

    /**
     * Is THR connected
     */
    val connected: MutableLiveData<Boolean> = MutableLiveData(false)

    /**
     * Compressor types
     */
    val compressorTypes
        get() = ECompressorType.values().toList()

    /**
     * Reverb types
     */
    val reverbTypes
        get() = EReverbType.values().toList()

    /**
     * Effect types
     */
    val effectTypes
        get() = EEffectType.values().toList()

    /**
     * Adds new preset
     */
    fun addPreset() {
        val newPresets = presets.value?.toMutableList()

        val preset: PresetMessage = if (activePreset.value != null) {
            activePreset.value!!
        } else if (presets.value != null && presets.value!!.size > 0) {
            presets.value!!.first()
        } else return

        newPresets?.add(preset)
        presets.value = newPresets
    }
}