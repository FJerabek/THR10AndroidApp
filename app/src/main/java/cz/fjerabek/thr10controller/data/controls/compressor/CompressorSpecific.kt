package cz.fjerabek.thr10controller.data.controls.compressor

import cz.fjerabek.thr10controller.data.enums.compressor.ECompressorType

interface CompressorSpecific {
    val type : ECompressorType
    fun toDump(data : ByteArray) : ByteArray
}