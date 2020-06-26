package cz.fjerabek.thr10.controls.reverb

import cz.fjerabek.thr10.enums.reverb.EReverbType
import cz.fjerabek.thr10.enums.reverb.ERoom
import kotlinx.serialization.Serializable

@Serializable
class Room(
    val time: Int,
    val preDelay: Int,
    val lowCut: Int,
    val highCut: Int,
    val highRatio: Byte,
    val lowRatio: Byte,
    val level: Byte
) : ReverbSpecific {
    override val type: EReverbType = EReverbType.ROOM

//    var time = time
//        set(value) {
//            require(ERoom.TIME.min <= value && ERoom.TIME.max >= value) {"Time value out of range"}
//            field = value
//        }
//
//    var preDelay = preDelay
//        set(value) {
//            require(ERoom.PRE_DELAY.min <= value && ERoom.PRE_DELAY.max >= value) {"Pre delay value out of range"}
//            field = value
//        }
//
//    var lowCut = lowCut
//        set(value) {
//            require(ERoom.LOW_CUT.min <= value && ERoom.LOW_CUT.max >= value) {"Low cut value out of range"}
//            field = value
//        }
//
//    var highCut = highCut
//        set(value) {
//            require(ERoom.HIGH_CUT.min <= value && ERoom.HIGH_CUT.max >= value) {"High cut value out of range"}
//            field = value
//        }
//
//    var highRatio = highRatio
//        set(value) {
//            require(ERoom.HIGH_RATIO.min <= value && ERoom.HIGH_RATIO.max >= value) {"High ratio value out of range"}
//            field = value
//        }
//
//    var lowRatio = lowRatio
//        set(value) {
//            require(ERoom.LOW_RATIO.min <= value && ERoom.LOW_RATIO.max >= value) {"Low ratio value out of range"}
//            field = value
//        }
//
//    var level = level
//        set(value) {
//            require(ERoom.LEVEL.min <= value && ERoom.LEVEL.max >= value) {"Level value out of range"}
//            field = value
//        }


    override fun toDump(dump: ByteArray): ByteArray {
        dump[ERoom.TIME.dumpPosition.first] = (time / 128).toByte()
        dump[ERoom.TIME.dumpPosition.second] = (time % 128).toByte()

        dump[ERoom.PRE_DELAY.dumpPosition.first] = (preDelay / 128).toByte()
        dump[ERoom.PRE_DELAY.dumpPosition.second] = (preDelay % 128).toByte()

        dump[ERoom.LOW_CUT.dumpPosition.first] = (lowCut / 128).toByte()
        dump[ERoom.LOW_CUT.dumpPosition.second] = (lowCut % 128).toByte()

        dump[ERoom.HIGH_CUT.dumpPosition.first] = (highCut / 128).toByte()
        dump[ERoom.HIGH_CUT.dumpPosition.second] = (highCut % 128).toByte()

        dump[ERoom.HIGH_RATIO.dumpPosition.first] = highRatio
        dump[ERoom.LOW_RATIO.dumpPosition.first] = lowRatio
        dump[ERoom.LEVEL.dumpPosition.first] = level

        return dump
    }

    companion object {
        fun fromDump(dump : ByteArray) : Room {
            return Room((dump[ERoom.TIME.dumpPosition.first] * 128) + dump[ERoom.TIME.dumpPosition.second],
                (dump[ERoom.PRE_DELAY.dumpPosition.first] * 128) + dump[ERoom.PRE_DELAY.dumpPosition.second],
                (dump[ERoom.LOW_CUT.dumpPosition.first] * 128) + dump[ERoom.LOW_CUT.dumpPosition.second],
                (dump[ERoom.HIGH_CUT.dumpPosition.first] * 128) + dump[ERoom.HIGH_CUT.dumpPosition.second],
                dump[ERoom.HIGH_RATIO.dumpPosition.first],
                dump[ERoom.LOW_RATIO.dumpPosition.first],
                dump[ERoom.LEVEL.dumpPosition.first])
        }
    }
}