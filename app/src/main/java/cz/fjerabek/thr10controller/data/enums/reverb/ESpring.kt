package cz.fjerabek.thr10controller.data.enums.reverb

import cz.fjerabek.thr10controller.data.controls.IDReverb
import cz.fjerabek.thr10controller.data.enums.IControlProperty

enum class ESpring(val id : Byte, val max : Byte, val min : Byte, val dumpPosition : Int) :
    IControlProperty {
    REVERB(IDReverb.IDSpring.REVERB, 0x64, 0x00, 210),
    FILTER(IDReverb.IDSpring.FILTER, 0x64, 0x00, 211);

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