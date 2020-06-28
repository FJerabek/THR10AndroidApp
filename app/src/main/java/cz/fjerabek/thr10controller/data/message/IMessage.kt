package cz.fjerabek.thr10controller.data.message

import cz.fjerabek.thr10controller.data.message.midi.ChangeMessage
import cz.fjerabek.thr10controller.data.message.midi.PresetMessage
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonParametricSerializer

interface IMessage {
    fun getSysexData() : ByteArray

    object MidiMessageSerializer : JsonParametricSerializer<IMessage>(IMessage::class) {
        override fun selectSerializer(element: JsonElement): KSerializer<out IMessage> = when {
            "preset" in element -> PresetMessage.serializer()
            else -> ChangeMessage.serializer()
        }

    }
}