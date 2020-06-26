package cz.fjerabek.thr10

import cz.fjerabek.thr10.controls.*
import kotlinx.serialization.Serializable
import java.nio.charset.Charset

@Serializable
class Preset(
    var name : String,
    var mainPanel: MainPanel,
    var compressor: Compressor? = null,
    var effect: Effect? = null,
    var delay: Delay? = null,
    var reverb: Reverb? = null,
    var gate: Gate? = null) : IControl {

    override fun toDump(dump: ByteArray): ByteArray {
        mainPanel.toDump(dump)
        compressor?.toDump(dump)
        effect?.toDump(dump)
        delay?.toDump(dump)
        reverb?.toDump(dump)
        gate?.toDump(dump)
        return dump
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