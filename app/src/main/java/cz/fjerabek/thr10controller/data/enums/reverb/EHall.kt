package cz.fjerabek.thr10controller.data.enums.reverb

import cz.fjerabek.thr10controller.data.controls.IDReverb
import cz.fjerabek.thr10controller.data.enums.IControlProperty

enum class EHall(val id : Byte, val max : Short, val min : Short, val dumpPosition : Pair<Int, Int>) :
    IControlProperty {
    TIME(IDReverb.IDHall.TIME, 0xC8, 0x03, Pair(210, 211)),
    PRE_DELAY(IDReverb.IDHall.PRE_DELAY, 2000,  1, Pair(212, 213)),
    LOW_CUT(IDReverb.IDHall.LOW_CUT, 8000,  21, Pair(214, 215)),
    HIGH_CUT(IDReverb.IDHall.HIGH_CUT, 16001,  1000, Pair(216, 217)),
    HIGH_RATIO(IDReverb.IDHall.HIGH_RATIO, 10,  1, Pair(218, -1)),
    LOW_RATIO(IDReverb.IDHall.LOW_RATIO, 14,  1, Pair(219, -1)),
    LEVEL(IDReverb.IDHall.LEVEL, 0x64,  0x00, Pair(220, -1));

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