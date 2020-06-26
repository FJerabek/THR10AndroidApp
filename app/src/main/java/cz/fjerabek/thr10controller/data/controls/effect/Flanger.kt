package cz.fjerabek.thr10.controls.effect

import cz.fjerabek.thr10.enums.effect.EEffectType
import cz.fjerabek.thr10.enums.effect.EFlanger
import kotlinx.serialization.Serializable

@Serializable
class Flanger(val speed : Byte,
              val manual : Byte,
              val depth : Byte,
              val feedback : Byte,
              val spread : Byte) : EffectSpecific {
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