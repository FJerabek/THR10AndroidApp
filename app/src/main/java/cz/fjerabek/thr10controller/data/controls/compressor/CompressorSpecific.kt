package cz.fjerabek.thr10.controls.compressor

import cz.fjerabek.thr10.enums.compressor.ECompressorType

interface CompressorSpecific {
    val type : ECompressorType
    fun toDump(data : ByteArray) : ByteArray
}