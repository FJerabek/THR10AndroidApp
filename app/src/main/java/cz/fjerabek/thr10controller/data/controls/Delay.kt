package cz.fjerabek.thr10.controls

import cz.fjerabek.thr10.enums.EStatus
import cz.fjerabek.thr10.enums.delay.EDelay
import kotlinx.serialization.Serializable

@Serializable
class Delay(
    val status : EStatus,
    val time : Int,
    val feedback : Byte,
    val highCut : Int,
    val lowCut : Int,
    val level : Byte
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
        dump[EDelay.TIME.dumpPosition.second] = (time % 128).toByte()
        dump[EDelay.FEEDBACK.dumpPosition.first] = feedback
        dump[EDelay.HIGH_CUT.dumpPosition.first] = (highCut / 128).toByte()
        dump[EDelay.HIGH_CUT.dumpPosition.second] = (highCut % 128).toByte()
        dump[EDelay.LOW_CUT.dumpPosition.first] = (lowCut / 128).toByte()
        dump[EDelay.LOW_CUT.dumpPosition.second] = (lowCut % 128).toByte()
        dump[EDelay.LEVEL.dumpPosition.first] = level

        return dump
    }

    companion object {
        fun fromDump(dump : ByteArray) : Delay {

            return Delay(
                EStatus.fromValue(dump[EDelay.STATUS.dumpPosition.first])!!,
                (dump[EDelay.TIME.dumpPosition.first] * 128) + dump[EDelay.TIME.dumpPosition.second],
                dump[EDelay.FEEDBACK.dumpPosition.first],
                (dump[EDelay.HIGH_CUT.dumpPosition.first] * 128) + dump[EDelay.HIGH_CUT.dumpPosition.second],
                (dump[EDelay.LOW_CUT.dumpPosition.first] * 128) + dump[EDelay.LOW_CUT.dumpPosition.second],
                dump[EDelay.LEVEL.dumpPosition.first]
            )
        }
    }
}