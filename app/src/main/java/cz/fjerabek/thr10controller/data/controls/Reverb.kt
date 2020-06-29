package cz.fjerabek.thr10controller.data.controls

import cz.fjerabek.thr10controller.data.Property
import cz.fjerabek.thr10controller.data.controls.compressor.Rack
import cz.fjerabek.thr10controller.data.controls.compressor.Stomp
import cz.fjerabek.thr10controller.data.enums.EStatus
import cz.fjerabek.thr10controller.data.enums.reverb.EReverb
import cz.fjerabek.thr10controller.data.enums.reverb.EReverbType
import cz.fjerabek.thr10controller.data.controls.reverb.*
import cz.fjerabek.thr10controller.data.enums.compressor.ECompressorType
import cz.fjerabek.thr10controller.data.enums.effect.EEffectType
import cz.fjerabek.thr10controller.data.message.midi.ChangeMessage
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonParametricSerializer

@Serializable
class Reverb(
    @Property(IDReverb.STATUS)
    var status : EStatus,
    @Property(IDReverb.TYPE)
    @Serializable(with = ReverbSerializer::class)
    var specific : ReverbSpecific?
) : IControl {

//    var status = status
//        set(value) {
//            require(EReverb.STATUS.min <= value.value && EReverb.STATUS.max <= value.value) {"Status out of range"}
//            field = value
//        }
//
//    var type = type
//        set(value) {
//            if(value != null)
//                require(EReverb.TYPE.max >= value.type.id && EEffect.STATUS.min <= value.type.id) {"Type out of range"}
//            field = value
//        }

    override fun processChangeMessage(message: ChangeMessage): Boolean {
        //Handle compressor type change
        if(message.property == IDEffect.TYPE) {
            specific?.let {
                val reverbSpecificDump = it.toDump(ByteArray(274))
                specific = when (EReverbType.fromId(message.value.toByte())) {
                    EReverbType.HALL -> Hall.fromDump(reverbSpecificDump)
                    EReverbType.ROOM -> Room.fromDump(reverbSpecificDump)
                    EReverbType.PLATE -> Plate.fromDump(reverbSpecificDump)
                    EReverbType.SPRING -> Spring.fromDump(reverbSpecificDump)
                }
            }
            return true
        } else {
            return if(message.property != IDCompressor.STATUS) {
                specific!!.processChangeMessage(message)
            } else {
                status = EStatus.fromValue(message.value.toByte())
                true
            }
        }
    }


    object ReverbSerializer : JsonParametricSerializer<ReverbSpecific>(ReverbSpecific::class) {
        override fun selectSerializer(element: JsonElement): KSerializer<out ReverbSpecific> {
            val type = element.jsonObject.getPrimitive("type").content

            return when(EReverbType.valueOf(type)) {
                EReverbType.HALL -> Hall.serializer()
                EReverbType.PLATE -> Plate.serializer()
                EReverbType.ROOM -> Room.serializer()
                EReverbType.SPRING -> Spring.serializer()
            }
        }

    }

    override fun toDump(dump: ByteArray): ByteArray {
        dump[EReverb.STATUS.dumpPosition] = status.value
        dump[EReverb.TYPE.dumpPosition] = specific?.type?.id ?: 0
        specific?.toDump(dump)
        return dump
    }

    companion object {
        fun fromDump(dump : ByteArray) : Reverb {

            val rType = when(EReverbType.fromId(dump[EReverb.TYPE.dumpPosition])!!) {
                EReverbType.HALL -> Hall.fromDump(dump)
                EReverbType.ROOM -> Room.fromDump(dump)
                EReverbType.PLATE -> Plate.fromDump(dump)
                EReverbType.SPRING -> Spring.fromDump(dump)
            }
            return Reverb(EStatus.fromValue(dump[EReverb.STATUS.dumpPosition])!!, rType)
        }
    }
}