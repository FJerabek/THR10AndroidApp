package cz.fjerabek.thr10controller.data.controls

import cz.fjerabek.thr10controller.data.Property
import cz.fjerabek.thr10controller.data.enums.EStatus
import cz.fjerabek.thr10controller.data.enums.delay.EDelay
import cz.fjerabek.thr10controller.data.message.midi.ChangeMessage
import kotlinx.serialization.Serializable
import kotlin.reflect.KMutableProperty
import kotlin.reflect.full.memberProperties

@Serializable
class Delay(
    @Property(IDDelay.STATUS)
    var status : EStatus,
    @Property(IDDelay.TIME)
    var time : Int,
    @Property(IDDelay.FEEDBACK)
    var feedback : Byte,
    @Property(IDDelay.HIGH_CUT)
    var highCut : Int,
    @Property(IDDelay.LOW_CUT)
    var lowCut : Int,
    @Property(IDDelay.LEVEL)
    var level : Byte
) : IControl {

//    var status = status
//        set(value) {
//            require(EDelay.STATUS.min <= value.value && EDelay.STATUS.max <= value.value) {"Status out of range"}
//            field = value
//        }
//
//    var time = time
//        set(value) {
//            require(value >= EDelay.TIME.min && value <= EDelay.TIME.max) {"Time value out of range"}
//            field = value
//        }
//
//    var feedback = feedback
//        set(value) {
//            require(value >= EDelay.FEEDBACK.min && value <= EDelay.FEEDBACK.max) {"Feedback value out of range"}
//            field = value
//        }
//    var highCut = highCut
//        set(value) {
//            require(value >= EDelay.HIGH_CUT.min && value <= EDelay.HIGH_CUT.max) {"High cut value out of range"}
//            field = value
//        }
//    var lowCut = lowCut
//        set(value) {
//            require(value >= EDelay.LOW_CUT.min && value <= EDelay.LOW_CUT.max) {"Low cut value out of range"}
//            field = value
//        }
//    var level = level
//        set(value) {
//            require(value >= EDelay.LEVEL.min && value <= EDelay.LEVEL.max) {"Level value out of range"}
//            field = value
//        }

    override fun toDump(dump: ByteArray): ByteArray {
        dump[EDelay.STATUS.dumpPosition.first] = status.value
        dump[EDelay.TIME.dumpPosition.first] = (time / 128).toByte()
        dump[EDelay.TIME.dumpPosition.second!!] = (time % 128).toByte()
        dump[EDelay.FEEDBACK.dumpPosition.first] = feedback
        dump[EDelay.HIGH_CUT.dumpPosition.first] = (highCut / 128).toByte()
        dump[EDelay.HIGH_CUT.dumpPosition.second!!] = (highCut % 128).toByte()
        dump[EDelay.LOW_CUT.dumpPosition.first] = (lowCut / 128).toByte()
        dump[EDelay.LOW_CUT.dumpPosition.second!!] = (lowCut % 128).toByte()
        dump[EDelay.LEVEL.dumpPosition.first] = level

        return dump
    }

    companion object {
        fun fromDump(dump : ByteArray) : Delay {

            return Delay(
                EStatus.fromValue(dump[EDelay.STATUS.dumpPosition.first])!!,
                (dump[EDelay.TIME.dumpPosition.first] * 128) + dump[EDelay.TIME.dumpPosition.second!!],
                dump[EDelay.FEEDBACK.dumpPosition.first],
                (dump[EDelay.HIGH_CUT.dumpPosition.first] * 128) + dump[EDelay.HIGH_CUT.dumpPosition.second!!],
                (dump[EDelay.LOW_CUT.dumpPosition.first] * 128) + dump[EDelay.LOW_CUT.dumpPosition.second!!],
                dump[EDelay.LEVEL.dumpPosition.first]
            )
        }
    }
}