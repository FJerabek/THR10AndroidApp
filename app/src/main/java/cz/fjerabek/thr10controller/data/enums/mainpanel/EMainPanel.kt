package cz.fjerabek.thr10controller.data.enums.mainpanel

import cz.fjerabek.thr10controller.data.controls.IDMainPanel
import cz.fjerabek.thr10controller.data.enums.IControlProperty

enum class EMainPanel (val id : Byte, val max : Byte, val min : Byte, val dumpPosition : Int) :
    IControlProperty {
    AMP(IDMainPanel.AMP, 0x07, 0x00, 145),
    GAIN(IDMainPanel.GAIN, 0x64, 0x00, 146),
    MASTER(IDMainPanel.MASTER, 0x64, 0x00, 147),
    BASS(IDMainPanel.BASS, 0x64, 0x00, 148),
    MIDDLE(IDMainPanel.MIDDLE, 0x64, 0x00, 149),
    TREBLE(IDMainPanel.TREBLE, 0x64, 0x00, 150),
    CABINET(IDMainPanel.CABINET, 0x05, 0x00, 151);

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