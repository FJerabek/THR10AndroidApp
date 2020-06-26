package cz.fjerabek.thr10.controls.effect

import cz.fjerabek.thr10.enums.effect.EChorus
import cz.fjerabek.thr10.enums.effect.EEffectType
import kotlinx.serialization.Serializable

@Serializable
class Chorus(val speed : Byte,
             val depth : Byte,
             val mix : Byte) : EffectSpecific {
    override val type: EEffectType = EEffectType.CHORUS

//    var speed = speed
//        set(value) {
//            require(EChorus.SPEED.min <= value && EChorus.SPEED.max >= value) {"Speed value out of range"}
//            field = value
//        }
//
//    var depth = depth
//        set(value) {
//            require(EChorus.DEPTH.min <= value && EChorus.DEPTH.max >= value) {"Depth value out of range"}
//            field = value
//        }
//
//    var mix = mix
//        set(value) {
//            require(EChorus.MIX.min <= value && EChorus.MIX.max >= value){"Mix value out of range"}
//            field = value
//        }
    override fun toDump(dump: ByteArray): ByteArray {
        dump[EChorus.SPEED.dumpPosition] = speed
        dump[EChorus.DEPTH.dumpPosition] = depth
        dump[EChorus.MIX.dumpPosition] = mix
        return dump
    }

    companion object {
        fun fromDump(dump : ByteArray) : Chorus {
            return Chorus(dump[EChorus.SPEED.dumpPosition], dump[EChorus.DEPTH.dumpPosition], dump[EChorus.MIX.dumpPosition])
        }
    }
}