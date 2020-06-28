package cz.fjerabek.thr10controller.data.controls

import cz.fjerabek.thr10controller.data.controls.effect.*
import cz.fjerabek.thr10controller.data.enums.EStatus
import cz.fjerabek.thr10controller.data.enums.effect.EEffect
import cz.fjerabek.thr10controller.data.enums.effect.EEffectType
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonParametricSerializer

@Serializable
class Effect(
    val status : EStatus,
    @Serializable(with = EffectSerializer::class) val specific : EffectSpecific?) : IControl {

//    var status = status
//        set(value) {
//            require(EEffect.STATUS.min <= value.value && EEffect.STATUS.max <= value.value) {"Status out of range"}
//            field = value
//        }
//
//    var type = type
//        set(value) {
//            if(value != null) {
//                require(EEffect.TYPE.min >= value.type.id && EEffect.TYPE.max <= value.type.id) { "Effect type out of range" }
//            }
//            field = value
//        }

    override fun toDump(dump: ByteArray): ByteArray {
        dump[EEffect.STATUS.dumpPosition] = status.value
        dump[EEffect.TYPE.dumpPosition] = specific?.type?.id ?: 0
        return specific?.toDump(dump) ?: dump
    }

    object EffectSerializer : JsonParametricSerializer<EffectSpecific>(EffectSpecific::class) {
        override fun selectSerializer(element: JsonElement): KSerializer<out EffectSpecific> {
            val type = element.jsonObject.getPrimitive("type").content
            return when(EEffectType.valueOf(type)) {
                EEffectType.CHORUS -> Chorus.serializer()
                EEffectType.FLANGER -> Flanger.serializer()
                EEffectType.PHASER -> Phaser.serializer()
                EEffectType.TREMOLO -> Tremolo.serializer()
            }
        }

    }

    companion object {
        fun fromDump(dump : ByteArray) : Effect {

            val effectType = when(EEffectType.fromId(dump[EEffect.TYPE.dumpPosition])!!) {
                EEffectType.CHORUS -> Chorus.fromDump(dump)
                EEffectType.FLANGER -> Flanger.fromDump(dump)
                EEffectType.TREMOLO -> Tremolo.fromDump(dump)
                EEffectType.PHASER -> Phaser.fromDump(dump)
            }

            return Effect(
                EStatus.fromValue(dump[EEffect.STATUS.dumpPosition])!!,
                effectType
            )
        }
    }
}