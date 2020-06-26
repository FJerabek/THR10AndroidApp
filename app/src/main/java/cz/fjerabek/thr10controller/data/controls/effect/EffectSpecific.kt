package cz.fjerabek.thr10.controls.effect

import cz.fjerabek.thr10.enums.effect.EEffectType

interface EffectSpecific {
    val type : EEffectType
    fun toDump(dump : ByteArray) : ByteArray
}