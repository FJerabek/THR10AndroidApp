package cz.fjerabek.thr10controller.data.controls.reverb

import cz.fjerabek.thr10controller.data.Property
import cz.fjerabek.thr10controller.data.controls.IDReverb
import cz.fjerabek.thr10controller.data.enums.reverb.EHall
import cz.fjerabek.thr10controller.data.enums.reverb.EReverbType
import kotlinx.serialization.Serializable

@Serializable
class Hall(
    @Property(IDReverb.IDHall.TIME)
    var time: Int,
    @Property(IDReverb.IDHall.PRE_DELAY)
    var preDelay: Int,
    @Property(IDReverb.IDHall.LOW_CUT)
    var lowCut: Int,
    @Property(IDReverb.IDHall.HIGH_CUT)
    var highCut: Int,
    @Property(IDReverb.IDHall.HIGH_RATIO)
    var highRatio: Byte,
    @Property(IDReverb.IDHall.LOW_RATIO)
    var lowRatio: Byte,
    @Property(IDReverb.IDHall.LEVEL)
    var level: Byte
) : ReverbSpecific {
    override val type: EReverbType = EReverbType.HALL
//    var time = time
//        set(value) {
//            require(EHall.TIME.min <= value && EHall.TIME.max >= value) {"Time value out of range"}
//            field = value
//        }
//
//    var preDelay = preDelay
//        set(value) {
//            require(EHall.PRE_DELAY.min <= value && EHall.PRE_DELAY.max >= value) {"Pre delay value out of range"}
//            field = value
//        }
//
//    var lowCut = lowCut
//        set(value) {
//            require(EHall.LOW_CUT.min <= value && EHall.LOW_CUT.max >= value) {"Low cut value out of range"}
//            field = value
//        }
//
//    var highCut = highCut
//        set(value) {
//            require(EHall.HIGH_CUT.min <= value && EHall.HIGH_CUT.max >= value) {"High cut value out of range"}
//            field = value
//        }
//
//    var highRatio = highRatio
//        set(value) {
//            require(EHall.HIGH_RATIO.min <= value && EHall.HIGH_RATIO.max >= value) {"High ratio value out of range"}
//            field = value
//        }
//
//    var lowRatio = lowRatio
//        set(value) {
//            require(EHall.LOW_RATIO.min <= value && EHall.LOW_RATIO.max >= value) {"Low ratio value out of range"}
//            field = value
//        }
//
//    var level = level
//        set(value) {
//            require(EHall.LEVEL.min <= value && EHall.LEVEL.max >= value) {"Level value out of range"}
//            field = value
//        }


    override fun toDump(dump: ByteArray): ByteArray {
        dump[EHall.TIME.dumpPosition.first] = (time / 128).toByte()
        dump[EHall.TIME.dumpPosition.second!!] = (time % 128).toByte()

        dump[EHall.PRE_DELAY.dumpPosition.first] = (preDelay / 128).toByte()
        dump[EHall.PRE_DELAY.dumpPosition.second!!] = (preDelay % 128).toByte()

        dump[EHall.LOW_CUT.dumpPosition.first] = (lowCut / 128).toByte()
        dump[EHall.LOW_CUT.dumpPosition.second!!] = (lowCut % 128).toByte()

        dump[EHall.HIGH_CUT.dumpPosition.first] = (highCut / 128).toByte()
        dump[EHall.HIGH_CUT.dumpPosition.second!!] = (highCut % 128).toByte()

        dump[EHall.HIGH_RATIO.dumpPosition.first] = highRatio
        dump[EHall.LOW_RATIO.dumpPosition.first] = lowRatio
        dump[EHall.LEVEL.dumpPosition.first] = level

        return dump
    }

    companion object {
        fun fromDump(dump : ByteArray) : Hall {
            return Hall((dump[EHall.TIME.dumpPosition.first] * 128) + dump[EHall.TIME.dumpPosition.second!!],
                (dump[EHall.PRE_DELAY.dumpPosition.first] * 128) + dump[EHall.PRE_DELAY.dumpPosition.second!!],
                (dump[EHall.LOW_CUT.dumpPosition.first] * 128) + dump[EHall.LOW_CUT.dumpPosition.second!!],
                (dump[EHall.HIGH_CUT.dumpPosition.first] * 128) + dump[EHall.HIGH_CUT.dumpPosition.second!!],
                dump[EHall.HIGH_RATIO.dumpPosition.first],
                dump[EHall.LOW_RATIO.dumpPosition.first],
                dump[EHall.LEVEL.dumpPosition.first])
        }
    }
}