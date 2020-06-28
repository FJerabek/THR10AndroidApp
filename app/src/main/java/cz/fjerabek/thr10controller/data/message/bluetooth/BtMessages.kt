package cz.fjerabek.thr10controller.data.message.bluetooth

import cz.fjerabek.thr10controller.data.Preset
import cz.fjerabek.thr10controller.data.message.midi.ChangeMessage
import cz.fjerabek.thr10controller.data.message.serial.FirmwareVersionMessage
import cz.fjerabek.thr10controller.data.message.serial.StatusMessage
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonParametricSerializer

@Serializable
data class BtChangeMessage(override val type: EMessageType, val change : ChangeMessage) : BtMessage

@Serializable
data class BtPresetMessage(override val type: EMessageType, val preset : Preset) : BtMessage

@Serializable
data class BtPresetsMessage(override val type: EMessageType, val presets : List<Preset>) : BtMessage

@Serializable
data class BtRequestMessage(override val type: EMessageType) : BtMessage

@Serializable
data class BtStatusChangeMessage(override val type: EMessageType, val status : Boolean) : BtMessage

@Serializable
data class BtPresetChangeMessage(override val type: EMessageType, val index : Int) : BtMessage

@Serializable
data class BtUartStatusMessage(override val type: EMessageType, val status : StatusMessage) :
    BtMessage

@Serializable
data class BtFirmwareStatusMessage(override val type: EMessageType, val firmware : FirmwareVersionMessage) :
    BtMessage

object BtMessageSerializer : JsonParametricSerializer<BtMessage>(BtMessage::class) {
    override fun selectSerializer(element: JsonElement): KSerializer<out BtMessage> {
        val type = element.jsonObject.getPrimitive("type").content


        return when(EMessageType.valueOf(type)) {
            EMessageType.CHANGE -> BtChangeMessage.serializer()

            EMessageType.DUMP_REQUEST,
            EMessageType.GET_PRESETS,
            EMessageType.GET_STATUS,
            EMessageType.POWER_OFF -> BtRequestMessage.serializer()

            EMessageType.ADD_PRESET,
            EMessageType.SET_PRESET -> BtPresetMessage.serializer()

            EMessageType.SET_PRESETS -> BtPresetsMessage.serializer()

            EMessageType.LAMP,
            EMessageType.WIDE_STEREO -> BtStatusChangeMessage.serializer()

            else -> TODO("BT message $type not yet implemented")
        }
    }

}