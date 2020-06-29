package cz.fjerabek.thr10controller.data.enums.compressor

import cz.fjerabek.thr10controller.data.controls.IDCompressor
import cz.fjerabek.thr10controller.data.enums.IControlProperty

enum class ERack(val id : Byte, val max : Int, val min : Byte, val dumpPosition: Pair<Int, Int?>) :
    IControlProperty {
    THRESHOLD(IDCompressor.IDRack.THRESHOLD, 0x258, 0x00, Pair(162, 163)),
    ATTACK(IDCompressor.IDRack.ATTACK, 0x64, 0x00, Pair(164, null)),
    RELEASE(IDCompressor.IDRack.RELEASE, 0x64, 0x00, Pair(165, null)),
    RATIO(IDCompressor.IDRack.RATIO, 0x05, 0x00, Pair(166, null)),
    KNEE(IDCompressor.IDRack.KNEE, 0x02, 0x00, Pair(167, null)),
    OUTPUT(IDCompressor.IDRack.OUTPUT, 0x258, 0x00, Pair(168, 169));

    override fun getPropertyId(): Byte {
        return this.id
    }

    override fun getMaximumValue(): Int {
        return this.max
    }

    override fun getMinimumValue(): Int {
        return this.min.toInt()
    }
}