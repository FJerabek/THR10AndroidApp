package cz.fjerabek.thr10controller.data.enums.effect

import cz.fjerabek.thr10controller.data.controls.IDEffect
import cz.fjerabek.thr10controller.data.enums.IControlProperty

enum class EChorus(val id : Byte, val max : Byte, val min : Byte, val dumpPosition : Int) :
    IControlProperty {
    SPEED(IDEffect.IDChorus.SPEED, 0x64, 0x00, 178),
    DEPTH(IDEffect.IDChorus.DEPTH, 0x64, 0x00, 179),
    MIX(IDEffect.IDChorus.MIX, 0x64, 0x00, 180);

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