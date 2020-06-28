package cz.fjerabek.thr10controller.data.controls.reverb

import cz.fjerabek.thr10controller.data.enums.reverb.EReverbType
import cz.fjerabek.thr10controller.data.enums.reverb.ESpring
import kotlinx.serialization.Serializable

@Serializable
class Spring(
    val reverb : Byte,
    val filter : Byte
) : ReverbSpecific {
    override val type: EReverbType = EReverbType.SPRING

//    var reverb = reverb
//        set(value) {
//            require(value <= ESpring.REVERB.max && value >= ESpring.REVERB.min){"Reverb value out of range"}
//            field = value
//        }
//
//    var filter = filter
//        set(value) {
//            require(value <= ESpring.FILTER.max && value >= ESpring.FILTER.min){"Filter value out of range"}
//            field = value
//        }

    override fun toDump(dump: ByteArray): ByteArray {
        dump[ESpring.REVERB.dumpPosition] = reverb
        dump[ESpring.FILTER.dumpPosition] = filter
        return dump
    }

    companion object {
        fun fromDump(dump : ByteArray) : Spring {

            return Spring(
                dump[ESpring.REVERB.dumpPosition],
                dump[ESpring.FILTER.dumpPosition])
        }
    }
}