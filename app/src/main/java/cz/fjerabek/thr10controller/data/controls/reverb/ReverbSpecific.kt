package cz.fjerabek.thr10controller.data.controls.reverb

import cz.fjerabek.thr10controller.data.controls.IControl
import cz.fjerabek.thr10controller.data.enums.reverb.EReverbType

interface ReverbSpecific : IControl {
    val type : EReverbType
}