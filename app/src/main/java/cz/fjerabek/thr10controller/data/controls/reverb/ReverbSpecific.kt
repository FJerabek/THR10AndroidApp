package cz.fjerabek.thr10.controls.reverb

import cz.fjerabek.thr10.enums.reverb.EReverbType

interface ReverbSpecific {
    val type : EReverbType
    fun toDump(dump : ByteArray) : ByteArray
}