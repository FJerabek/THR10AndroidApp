package cz.fjerabek.thr10controller.data.enums.compressor

import cz.fjerabek.thr10controller.data.controls.IDCompressor
import cz.fjerabek.thr10controller.data.enums.IControlProperty

enum class EStomp(val id : Byte, val  max : Byte, val min : Byte, val dumpPosition : Int) :
    IControlProperty {
    SUSTAIN(IDCompressor.IDStomp.SUSTAIN, 0x64, 0x00, 162),
    OUTPUT(IDCompressor.IDStomp.OUTPUT, 0x64, 0x00, 163);

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