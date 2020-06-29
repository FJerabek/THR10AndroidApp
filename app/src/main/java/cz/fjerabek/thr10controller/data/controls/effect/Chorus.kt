package cz.fjerabek.thr10controller.data.controls.effect

import cz.fjerabek.thr10controller.data.Property
import cz.fjerabek.thr10controller.data.controls.IDEffect
import cz.fjerabek.thr10controller.data.enums.effect.EChorus
import cz.fjerabek.thr10controller.data.enums.effect.EEffectType
import cz.fjerabek.thr10controller.data.message.midi.ChangeMessage
import kotlinx.serialization.Serializable
import kotlin.reflect.KMutableProperty
import kotlin.reflect.full.memberProperties

@Serializable
class Chorus(
    @Property(IDEffect.IDChorus.SPEED)
    var speed : Byte,
    @Property(IDEffect.IDChorus.DEPTH)
    var depth : Byte,
    @Property(IDEffect.IDChorus.MIX)
    var mix : Byte
) : EffectSpecific {
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