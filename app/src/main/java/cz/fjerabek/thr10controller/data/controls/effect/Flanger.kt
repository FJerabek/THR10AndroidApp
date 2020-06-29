package cz.fjerabek.thr10controller.data.controls.effect

import cz.fjerabek.thr10controller.data.Property
import cz.fjerabek.thr10controller.data.controls.IDEffect
import cz.fjerabek.thr10controller.data.enums.effect.EEffectType
import cz.fjerabek.thr10controller.data.enums.effect.EFlanger
import cz.fjerabek.thr10controller.data.message.midi.ChangeMessage
import kotlinx.serialization.Serializable
import kotlin.reflect.KMutableProperty
import kotlin.reflect.full.memberProperties

@Serializable
class Flanger(
    @Property(IDEffect.IDFlanger.SPEED)
    var speed : Byte,
    @Property(IDEffect.IDFlanger.MANUAL)
    var manual : Byte,
    @Property(IDEffect.IDFlanger.DEPTH)
    var depth : Byte,
    @Property(IDEffect.IDFlanger.FEEDBACK)
    var feedback : Byte,
    @Property(IDEffect.IDFlanger.SPREAD)
    var spread : Byte
) : EffectSpecific {
    override val type: EEffectType = EEffectType.FLANGER

//    var speed = speed
//        set(value) {
//            require(EFlanger.SPEED.min <= value  && EFlanger.SPEED.max >= value) {"Speed value out of range"}
//            field = value
//        }
//
//    var manual = manual
//        set(value) {
//            require(EFlanger.MANUAL.min <= value  && EFlanger.MANUAL.max >= value) {"Manual value out of range"}
//            field = value
//        }
//
//    var depth = depth
//        set(value) {
//            require(EFlanger.DEPTH.min <= value  && EFlanger.DEPTH.max >= value) {"Depth value out of range"}
//            field = value
//        }
//
//    var feedback = feedback
//        set(value) {
//            require(EFlanger.FEEDBACK.min <= value  && EFlanger.FEEDBACK.max >= value) {"Feedback value out of range"}
//            field = value
//        }
//
//    var spread = spread
//        set(value) {
//            require(EFlanger.SPREAD.min <= value  && EFlanger.SPREAD.max >= value) {"Spread value out of range"}
//            field = value
//        }

    override fun toDump(dump: ByteArray): ByteArray {
        dump[EFlanger.SPEED.dumpPosition] = speed
        dump[EFlanger.MANUAL.dumpPosition] = manual
        dump[EFlanger.DEPTH.dumpPosition] = depth
        dump[EFlanger.FEEDBACK.dumpPosition] = feedback
        dump[EFlanger.SPREAD.dumpPosition] = spread
        return dump
    }

    companion object {
        fun fromDump(dump : ByteArray) : Flanger {
            return Flanger(dump[EFlanger.SPEED.dumpPosition],
                dump[EFlanger.MANUAL.dumpPosition],
                dump[EFlanger.DEPTH.dumpPosition],
                dump[EFlanger.FEEDBACK.dumpPosition],
                dump[EFlanger.SPREAD.dumpPosition])
        }
    }
}