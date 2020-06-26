package cz.fjerabek.thr10.controls.effect

import cz.fjerabek.thr10.enums.effect.EEffectType
import cz.fjerabek.thr10.enums.effect.ETremolo
import kotlinx.serialization.Serializable

@Serializable
class Tremolo (
    val freq : Byte,
    val depth : Byte
) : EffectSpecific {
    override val type: EEffectType = EEffectType.TREMOLO
    //    var freq = freq
//        set(value) {
//            require(ETremolo.FREQ.min <= value && ETremolo.FREQ.max >= value) {"Freq value out of range"}
//            field = value
//        }
//
//    var depth = depth
//        set(value) {
//            require(ETremolo.DEPTH.min <= value && ETremolo.DEPTH.max >= value) {"Depth value out of range"}
//            field = value
//        }


    override fun toDump(dump: ByteArray): ByteArray {
        dump[ETremolo.FREQ.dumpPosition] = freq
        dump[ETremolo.DEPTH.dumpPosition] = depth
        return dump
    }

    companion object {
        fun fromDump(dump: ByteArray): Tremolo {
            return Tremolo(
                dump[ETremolo.FREQ.dumpPosition],
                dump[ETremolo.DEPTH.dumpPosition])
        }
    }
}