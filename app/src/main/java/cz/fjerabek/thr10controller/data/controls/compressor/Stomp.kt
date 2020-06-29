package cz.fjerabek.thr10controller.data.controls.compressor

import cz.fjerabek.thr10controller.data.Property
import cz.fjerabek.thr10controller.data.controls.IDCompressor
import cz.fjerabek.thr10controller.data.enums.compressor.ECompressorType
import cz.fjerabek.thr10controller.data.enums.compressor.EStomp
import kotlinx.serialization.Serializable

@Serializable
class Stomp (
    @Property(IDCompressor.IDStomp.SUSTAIN)
    var sustain : Byte,
    @Property(IDCompressor.IDStomp.OUTPUT)
    var output : Byte
): CompressorSpecific {
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


    override fun toDump(dump: ByteArray): ByteArray {
        dump[EStomp.SUSTAIN.dumpPosition] = sustain
        dump[EStomp.OUTPUT.dumpPosition] = output
        return dump
    }

    companion object {
        fun fromDump(dump: ByteArray) : Stomp {
            return Stomp(dump[EStomp.SUSTAIN.dumpPosition], dump[EStomp.OUTPUT.dumpPosition])
        }
    }
}