package cz.fjerabek.thr10controller.data.controls.compressor

import cz.fjerabek.thr10controller.data.enums.compressor.ECompressorType
import cz.fjerabek.thr10controller.data.enums.compressor.ERack
import kotlinx.serialization.Serializable

@Serializable
class Rack(val threshold : Int,
           val attack: Byte,
           val release : Byte,
           val ratio : Byte,
           val knee : Byte,
           val output : Int) : CompressorSpecific {
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

    override fun toDump(data: ByteArray): ByteArray {
        data[ERack.THRESHOLD.dumpPosition.first] = (threshold / 128).toByte()
        data[ERack.THRESHOLD.dumpPosition.second] = (threshold % 128).toByte()

        data[ERack.ATTACK.dumpPosition.first] = attack
        data[ERack.RELEASE.dumpPosition.first] = release
        data[ERack.RATIO.dumpPosition.first] = ratio

        data[ERack.KNEE.dumpPosition.first] = knee
        data[ERack.OUTPUT.dumpPosition.first] = (output / 128).toByte()
        data[ERack.OUTPUT.dumpPosition.second] = (output % 128).toByte()

        return data
    }

    companion object {

        fun fromDump(dump: ByteArray) : Rack {
            return Rack(
                (dump[ERack.THRESHOLD.dumpPosition.first] * 128) + dump[ERack.THRESHOLD.dumpPosition.second],
                dump[ERack.ATTACK.dumpPosition.first],
                dump[ERack.RELEASE.dumpPosition.first],
                dump[ERack.RATIO.dumpPosition.first],
                dump[ERack.KNEE.dumpPosition.first],
                (dump[ERack.OUTPUT.dumpPosition.first] * 128) + dump[ERack.OUTPUT.dumpPosition.second])
        }
    }
}