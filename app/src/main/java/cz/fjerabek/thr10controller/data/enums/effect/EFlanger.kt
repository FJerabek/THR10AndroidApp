package cz.fjerabek.thr10controller.data.enums.effect

import cz.fjerabek.thr10controller.data.controls.IDEffect
import cz.fjerabek.thr10controller.data.enums.IControlProperty

enum class EFlanger(val id : Byte, val max : Byte, val min : Byte, val dumpPosition : Int) :
    IControlProperty {
    SPEED(IDEffect.IDFlanger.SPEED, 0x64, 0x00, 178),
    MANUAL(IDEffect.IDFlanger.MANUAL, 0x64, 0x00, 179),
    DEPTH(IDEffect.IDFlanger.DEPTH, 0x64, 0x00, 180),
    FEEDBACK(IDEffect.IDFlanger.FEEDBACK, 0x64, 0x00, 181),
    SPREAD(IDEffect.IDFlanger.SPREAD, 0x64, 0x00, 182);

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