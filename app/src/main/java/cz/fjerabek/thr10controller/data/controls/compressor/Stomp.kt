package cz.fjerabek.thr10controller.data.controls.compressor

import cz.fjerabek.thr10controller.data.enums.compressor.ECompressorType
import cz.fjerabek.thr10controller.data.enums.compressor.EStomp
import kotlinx.serialization.Serializable

@Serializable
class Stomp (val sustain : Byte,
             val output : Byte): CompressorSpecific {
    override val type: ECompressorType = ECompressorType.STOMP

//    var sustain  = sustain
//        set(value) {
//            require(value >= EStomp.SUSTAIN.min && value <= EStomp.SUSTAIN.max) {"Sustain out of range"}
//            field = value
//        }
//
//    var output  = output
//        set(value) {
//            require(value >= EStomp.OUTPUT.min && value <= EStomp.OUTPUT.max) {"Output ouf of range"}
//            field = value
//        }


    override fun toDump(data: ByteArray): ByteArray {
        data[EStomp.SUSTAIN.dumpPosition] = sustain
        data[EStomp.OUTPUT.dumpPosition] = output
        return data
    }

    companion object {
        fun fromDump(dump: ByteArray) : Stomp {
            return Stomp(dump[EStomp.SUSTAIN.dumpPosition], dump[EStomp.OUTPUT.dumpPosition])
        }
    }
}