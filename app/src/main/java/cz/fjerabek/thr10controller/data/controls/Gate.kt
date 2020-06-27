package cz.fjerabek.thr10.controls

import cz.fjerabek.thr10.enums.EStatus
import cz.fjerabek.thr10.enums.gate.EGate
import kotlinx.serialization.Serializable

@Serializable
class Gate(
    val status : EStatus,
    val threshold : Byte,
    val release : Byte
) : IControl {

//    var status = status
//        set(value) {
//            require(EGate.STATUS.min <= value.value && EGate.STATUS.max <= value.value) {"Status out of range"}
//            field = value
//        }
//
//    var threshold = threshold
//        set(value) {
//            require(EGate.THRESHOLD.min <= value && EGate.THRESHOLD.max >= value) {"Threshold value out of range"}
//            field = value
//        }
//
//    var release = release
//        set(value) {
//            require(EGate.RELEASE.min <= value && EGate.RELEASE.max >= value)
//            field = value
//        }


    override fun toDump(dump: ByteArray): ByteArray {
        dump[EGate.STATUS.dumpPosition] = status.value
        dump[EGate.THRESHOLD.dumpPosition] = threshold
        dump[EGate.RELEASE.dumpPosition] = release
        return dump
    }

    companion object {
        fun fromDump(dump : ByteArray) : Gate {
            return Gate(EStatus.fromValue(dump[EGate.STATUS.dumpPosition])!!,
                dump[EGate.THRESHOLD.dumpPosition],
                dump[EGate.RELEASE.dumpPosition])
        }
    }
}