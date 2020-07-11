package cz.fjerabek.thr10controller.data.controls

import cz.fjerabek.thr10controller.data.Property
import cz.fjerabek.thr10controller.data.controls.compressor.CompressorSpecific
import cz.fjerabek.thr10controller.data.controls.compressor.Rack
import cz.fjerabek.thr10controller.data.controls.compressor.Stomp
import cz.fjerabek.thr10controller.data.enums.EStatus
import cz.fjerabek.thr10controller.data.enums.compressor.ECompressor
import cz.fjerabek.thr10controller.data.enums.compressor.ECompressorType
import cz.fjerabek.thr10controller.data.message.midi.ChangeMessage
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonParametricSerializer
import kotlin.reflect.full.memberProperties

@Serializable
class Compressor(
    @Property(IDCompressor.STATUS)
    var status : EStatus,
    @Property(IDCompressor.TYPE)
    @Serializable(with = CompressorSerializer::class)
    var specific : CompressorSpecific?
) : IControl {



    override fun toDump(dump: ByteArray): ByteArray {
        dump[ECompressor.STATUS.dumpPosition] = status.value
        dump[ECompressor.TYPE.dumpPosition] = specific?.type?.id ?: 0

        return specific?.toDump(dump) ?: dump
    }

    override fun processChangeMessage(message: ChangeMessage): Boolean {
        //Handle compressor type change
        if(message.property == IDCompressor.TYPE) {
            specific?.let {
                val compressorSpecificDump = it.toDump(ByteArray(274))
                specific = when (ECompressorType.fromId(message.value.toByte())) {
                    ECompressorType.STOMP -> Stomp.fromDump(compressorSpecificDump)
                    ECompressorType.RACK -> Rack.fromDump(compressorSpecificDump)
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


    object CompressorSerializer : JsonParametricSerializer<CompressorSpecific>(CompressorSpecific::class) {
        override fun selectSerializer(element: JsonElement): KSerializer<out CompressorSpecific> {
            val type = element.jsonObject.getPrimitive("type").content

            return when(ECompressorType.valueOf(type)) {
                ECompressorType.RACK -> Rack.serializer()
                ECompressorType.STOMP -> Stomp.serializer()
            }
        }

    }

    companion object {
        fun fromDump(dump : ByteArray) : Compressor {
            val status = EStatus.fromValue(dump[ECompressor.STATUS.dumpPosition])!!

            val compressorSpecific = when(ECompressorType.fromId(dump[ECompressor.TYPE.dumpPosition])!!){
                ECompressorType.STOMP -> Stomp.fromDump(dump)
                ECompressorType.RACK -> Rack.fromDump(dump)
            }

            return Compressor(status, compressorSpecific)
        }
    }

}