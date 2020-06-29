package cz.fjerabek.thr10controller.data.controls.reverb

import cz.fjerabek.thr10controller.data.Property
import cz.fjerabek.thr10controller.data.controls.IDReverb
import cz.fjerabek.thr10controller.data.enums.reverb.EPlate
import cz.fjerabek.thr10controller.data.enums.reverb.EReverbType
import kotlinx.serialization.Serializable

@Serializable
class Plate(
    @Property(IDReverb.IDPlate.TIME)
    var time: Int,
    @Property(IDReverb.IDPlate.PRE_DELAY)
    var preDelay: Int,
    @Property(IDReverb.IDPlate.LOW_CUT)
    var lowCut: Int,
    @Property(IDReverb.IDPlate.HIGH_CUT)
    var highCut: Int,
    @Property(IDReverb.IDPlate.HIGH_RATIO)
    var highRatio: Byte,
    @Property(IDReverb.IDPlate.LOW_RATIO)
    var lowRatio: Byte,
    @Property(IDReverb.IDPlate.LEVEL)
    var level: Byte
) : ReverbSpecific {

    override val type: EReverbType = EReverbType.PLATE
//    var time = time
//        set(value) {
//            require(EPlate.TIME.min <= value && EPlate.TIME.max >= value) {"Time value out of range"}
//            field = value
//        }
//
//    var preDelay = preDelay
//        set(value) {
//            require(EPlate.PRE_DELAY.min <= value && EPlate.PRE_DELAY.max >= value) {"Pre delay value out of range"}
//            field = value
//        }
//
//    var lowCut = lowCut
//        set(value) {
//            require(EPlate.LOW_CUT.min <= value && EPlate.LOW_CUT.max >= value) {"Low cut value out of range"}
//            field = value
//        }
//
//    var highCut = highCut
//        set(value) {
//            require(EPlate.HIGH_CUT.min <= value && EPlate.HIGH_CUT.max >= value) {"High cut value out of range"}
//            field = value
//        }
//
//    var highRatio = highRatio
//        set(value) {
//            require(EPlate.HIGH_RATIO.min <= value && EPlate.HIGH_RATIO.max >= value) {"High ratio value out of range"}
//            field = value
//        }
//
//    var lowRatio = lowRatio
//        set(value) {
//            require(EPlate.LOW_RATIO.min <= value && EPlate.LOW_RATIO.max >= value) {"Low ratio value out of range"}
//            field = value
//        }
//
//    var level = level
//        set(value) {
//            require(EPlate.LEVEL.min <= value && EPlate.LEVEL.max >= value) {"Level value out of range"}
//            field = value
//        }

    override fun toDump(dump: ByteArray): ByteArray {
        dump[EPlate.TIME.dumpPosition.first] = (time / 128).toByte()
        dump[EPlate.TIME.dumpPosition.second!!] = (time % 128).toByte()

        dump[EPlate.PRE_DELAY.dumpPosition.first] = (preDelay / 128).toByte()
        dump[EPlate.PRE_DELAY.dumpPosition.second!!] = (preDelay % 128).toByte()

        dump[EPlate.LOW_CUT.dumpPosition.first] = (lowCut / 128).toByte()
        dump[EPlate.LOW_CUT.dumpPosition.second!!] = (lowCut % 128).toByte()

        dump[EPlate.HIGH_CUT.dumpPosition.first] = (highCut / 128).toByte()
        dump[EPlate.HIGH_CUT.dumpPosition.second!!] = (highCut % 128).toByte()

        dump[EPlate.HIGH_RATIO.dumpPosition.first] = highRatio
        dump[EPlate.LOW_RATIO.dumpPosition.first] = lowRatio
        dump[EPlate.LEVEL.dumpPosition.first] = level

        return dump
    }

    companion object {
        fun fromDump(dump : ByteArray) : Plate {
            return Plate((dump[EPlate.TIME.dumpPosition.first] * 128) + dump[EPlate.TIME.dumpPosition.second!!],
                (dump[EPlate.PRE_DELAY.dumpPosition.first] * 128) + dump[EPlate.PRE_DELAY.dumpPosition.second!!],
                (dump[EPlate.LOW_CUT.dumpPosition.first] * 128) + dump[EPlate.LOW_CUT.dumpPosition.second!!],
                (dump[EPlate.HIGH_CUT.dumpPosition.first] * 128) + dump[EPlate.HIGH_CUT.dumpPosition.second!!],
                dump[EPlate.HIGH_RATIO.dumpPosition.first],
                dump[EPlate.LOW_RATIO.dumpPosition.first],
                dump[EPlate.LEVEL.dumpPosition.first])
        }
    }
}