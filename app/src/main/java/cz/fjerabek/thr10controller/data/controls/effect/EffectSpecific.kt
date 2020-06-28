package cz.fjerabek.thr10controller.data.controls.effect

import cz.fjerabek.thr10controller.data.enums.effect.EEffectType

interface EffectSpecific {
    val type : EEffectType
    fun toDump(dump : ByteArray) : ByteArray
}