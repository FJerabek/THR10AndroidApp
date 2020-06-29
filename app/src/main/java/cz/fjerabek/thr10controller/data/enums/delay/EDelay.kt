package cz.fjerabek.thr10controller.data.enums.delay

import cz.fjerabek.thr10controller.data.controls.IDDelay
import cz.fjerabek.thr10controller.data.enums.IControlProperty

enum class EDelay(val id : Byte, val max : Short, val min : Short, val dumpPosition : Pair<Int, Int?>) :
    IControlProperty {
    STATUS(IDDelay.STATUS, 0x7F, 0x00, Pair(208, null)),
    TIME(IDDelay.TIME, 0x270f, 0x01, Pair(194, 195)),
    FEEDBACK(IDDelay.FEEDBACK, 0x64, 0x00, Pair(196, null)),
    HIGH_CUT(IDDelay.HIGH_CUT, 0x3E81, 0x3E8, Pair(197, 198)),
    LOW_CUT(IDDelay.LOW_CUT, 0x1F40, 0x15, Pair(199, 200)),
    LEVEL(IDDelay.LEVEL, 0x64, 0x00, Pair(201, null));

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