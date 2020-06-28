package cz.fjerabek.thr10controller.data.controls.effect

import cz.fjerabek.thr10controller.data.enums.effect.EEffectType
import cz.fjerabek.thr10controller.data.enums.effect.EPhaser
import kotlinx.serialization.Serializable

@Serializable
class Phaser (val speed : Byte,
              val manual : Byte,
              val depth : Byte,
              val feedback : Byte) : EffectSpecific {
    override val type: EEffectType = EEffectType.PHASER

//    var speed = speed
//        set(value) {
//            require(EPhaser.SPEED.min <= value && EPhaser.SPEED.max >= value) { "Speed value out of range" }
//            field = value
//        }
//
//    var manual = manual
//        set(value) {
//            require(EPhaser.MANUAL.min <= value && EPhaser.MANUAL.max >= value) { "Manual value out of range" }
//            field = value
//        }
//
//    var depth = depth
//        set(value) {
//            require(EPhaser.DEPTH.min <= value && EPhaser.DEPTH.max >= value) { "Depth value out of range" }
//            field = value
//        }
//
//    var feedback = feedback
//        set(value) {
//            require(EPhaser.FEEDBACK.min <= value && EPhaser.FEEDBACK.max >= value) { "Feedback value out of range" }
//            field = value
//        }


    override fun toDump(dump: ByteArray): ByteArray {
        dump[EPhaser.SPEED.dumpPosition] = speed
        dump[EPhaser.MANUAL.dumpPosition] = manual
        dump[EPhaser.DEPTH.dumpPosition] = depth
        dump[EPhaser.FEEDBACK.dumpPosition] = feedback
        return dump
    }

    companion object {
        fun fromDump(dump: ByteArray): Phaser {

            return Phaser(
                dump[EPhaser.SPEED.dumpPosition],
                dump[EPhaser.MANUAL.dumpPosition],
                dump[EPhaser.DEPTH.dumpPosition],
                dump[EPhaser.FEEDBACK.dumpPosition])
        }
    }
}