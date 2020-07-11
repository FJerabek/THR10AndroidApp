package cz.fjerabek.thr10controller.data

import cz.fjerabek.thr10controller.data.controls.*
import cz.fjerabek.thr10controller.data.message.midi.ChangeMessage
import kotlinx.serialization.Serializable
import java.nio.charset.Charset
import kotlin.reflect.full.memberProperties

@Serializable
class Preset(
    var name : String,
    var mainPanel: MainPanel,
    var compressor: Compressor? = null,
    var effect: Effect? = null,
    var delay: Delay? = null,
    var reverb: Reverb? = null,
    var gate: Gate? = null) : IControl {

    override fun processChangeMessage(message : ChangeMessage) : Boolean =
        mainPanel.processChangeMessage(message) ||
                compressor?.processChangeMessage(message) ?: false ||
                effect?.processChangeMessage(message) ?: false ||
                delay?.processChangeMessage(message) ?: false ||
                reverb?.processChangeMessage(message) ?: false ||
                gate?.processChangeMessage(message) ?: false

    override fun toDump(dump: ByteArray): ByteArray {
        mainPanel.toDump(dump)
        compressor?.toDump(dump)
        effect?.toDump(dump)
        delay?.toDump(dump)
        reverb?.toDump(dump)
        gate?.toDump(dump)
        return dump
    }

    override fun toString(): String {
        return "Preset(name='$name', mainPanel=$mainPanel, compressor=$compressor, effect=$effect, delay=$delay, reverb=$reverb, gate=$gate)"
    }

    companion object {
        fun fromDump(dump: ByteArray) : Preset {
            return Preset(
                dump.copyOfRange(17, 80).toString(Charset.forName("ASCII")),
                MainPanel.fromDump(dump),
                Compressor.fromDump(dump),
                Effect.fromDump(dump),
                Delay.fromDump(dump),
                Reverb.fromDump(dump),
                Gate.fromDump(dump))
        }
    }


}