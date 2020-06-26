package cz.fjerabek.thr10

import cz.fjerabek.thr10.midi.message.ChangeMessage
import cz.fjerabek.thr10.midi.message.PresetMessage
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