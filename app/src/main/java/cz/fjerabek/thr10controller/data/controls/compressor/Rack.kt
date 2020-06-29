package cz.fjerabek.thr10controller.data.controls.compressor

import cz.fjerabek.thr10controller.data.Property
import cz.fjerabek.thr10controller.data.controls.IDCompressor
import cz.fjerabek.thr10controller.data.enums.compressor.ECompressorType
import cz.fjerabek.thr10controller.data.enums.compressor.ERack
import cz.fjerabek.thr10controller.data.message.midi.ChangeMessage
import kotlinx.serialization.Serializable
import kotlin.reflect.KMutableProperty
import kotlin.reflect.full.memberProperties

@Serializable
class Rack(
    @Property(IDCompressor.IDRack.THRESHOLD)
    var threshold : Int,
    @Property(IDCompressor.IDRack.ATTACK)
    var attack: Byte,
    @Property(IDCompressor.IDRack.RELEASE)
    var release : Byte,
    @Property(IDCompressor.IDRack.RATIO)
    var ratio : Byte,
    @Property(IDCompressor.IDRack.KNEE)
    var knee : Byte,
    @Property(IDCompressor.IDRack.OUTPUT)
    var output : Int
) : CompressorSpecific {

    override val type: ECompressorType = ECompressorType.RACK

//    var threshold  = threshold
//        set(value) {
//            require(value >= ERack.THRESHOLD.min && value <= ERack.THRESHOLD.max){"Threshold out of range"}
//            field = value
//        }
//
//    var attack  = attack
//        set(value) {
//            require(value >= ERack.ATTACK.min && value <= ERack.ATTACK.max){"Attack out of range"}
//            field = value
//        }
//
//    var release  = release
//        set(value) {
//            require(value >= ERack.RELEASE.min && value <= ERack.RELEASE.max){"Release out of range"}
//            field = value
//        }
//
//    var ratio  = ratio
//        set(value) {
//            require(value >= ERack.RATIO.min && value <= ERack.RATIO.max){"Ratio out of range"}
//            field = value
//        }
//
//    var knee  = knee
//        set(value) {
//            require(value >= ERack.KNEE.min && value <= ERack.KNEE.max){"Knee out of range"}
//            field = value
//        }
//
//    var output  = output
//        set(value) {
//            require(value >= ERack.OUTPUT.min && value <= ERack.OUTPUT.max){"Output out of range"}
//            field = value
//        }

    override fun toDump(dump: ByteArray): ByteArray {
        dump[ERack.THRESHOLD.dumpPosition.first] = (threshold / 128).toByte()
        dump[ERack.THRESHOLD.dumpPosition.second!!] = (threshold % 128).toByte()

        dump[ERack.ATTACK.dumpPosition.first] = attack
        dump[ERack.RELEASE.dumpPosition.first] = release
        dump[ERack.RATIO.dumpPosition.first] = ratio

        dump[ERack.KNEE.dumpPosition.first] = knee
        dump[ERack.OUTPUT.dumpPosition.first] = (output / 128).toByte()
        dump[ERack.OUTPUT.dumpPosition.second!!] = (output % 128).toByte()

        return dump
    }


    companion object {

        fun fromDump(dump: ByteArray) : Rack {
            return Rack(
                (dump[ERack.THRESHOLD.dumpPosition.first] * 128) + dump[ERack.THRESHOLD.dumpPosition.second!!],
                dump[ERack.ATTACK.dumpPosition.first],
                dump[ERack.RELEASE.dumpPosition.first],
                dump[ERack.RATIO.dumpPosition.first],
                dump[ERack.KNEE.dumpPosition.first],
                (dump[ERack.OUTPUT.dumpPosition.first] * 128) + dump[ERack.OUTPUT.dumpPosition.second!!])
        }
    }
}