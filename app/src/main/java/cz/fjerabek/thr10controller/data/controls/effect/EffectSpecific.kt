package cz.fjerabek.thr10controller.data.controls.effect

import cz.fjerabek.thr10controller.data.controls.IControl
import cz.fjerabek.thr10controller.data.enums.effect.EEffectType

interface EffectSpecific : IControl {
    val type : EEffectType
}