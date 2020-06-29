package cz.fjerabek.thr10controller.data.controls.compressor

import cz.fjerabek.thr10controller.data.controls.IControl
import cz.fjerabek.thr10controller.data.enums.compressor.ECompressorType

interface CompressorSpecific : IControl {
    val type : ECompressorType
}