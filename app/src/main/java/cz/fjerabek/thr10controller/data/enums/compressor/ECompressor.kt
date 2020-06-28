package cz.fjerabek.thr10controller.data.enums.compressor

import cz.fjerabek.thr10controller.data.controls.IDCompressor
import cz.fjerabek.thr10controller.data.enums.IControlProperty

enum class ECompressor(val id : Byte, val max : Byte, val min : Byte, val dumpPosition : Int) :
    IControlProperty {
    STATUS(IDCompressor.STATUS, 0x7F, 0x00, 176),
    TYPE(IDCompressor.TYPE, 0x01,0x00, 161);

    override fun getPropertyId(): Byte {
        return this.id
    }

    override fun getMaximumValue(): Int {
        return this.max.toInt()
    }

    override fun getMinimumValue(): Int {
        return this.min.toInt()
    }
}