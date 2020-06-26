package cz.fjerabek.thr10.controls

import cz.fjerabek.thr10.controls.reverb.*
import cz.fjerabek.thr10.enums.EStatus
import cz.fjerabek.thr10.enums.reverb.EReverb
import cz.fjerabek.thr10.enums.reverb.EReverbType
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonParametricSerializer

@Serializable
class Reverb(
    val status : EStatus,
    @Serializable(with = ReverbSerializer::class) val type : ReverbSpecific ?
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
        dump[EReverb.TYPE.dumpPosition] = type?.type?.id ?: 0
        type?.toDump(dump)
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