package cz.fjerabek.thr10controller.data.controls.reverb

import cz.fjerabek.thr10controller.data.enums.reverb.EReverbType

interface ReverbSpecific {
    val type : EReverbType
    fun toDump(dump : ByteArray) : ByteArray
}