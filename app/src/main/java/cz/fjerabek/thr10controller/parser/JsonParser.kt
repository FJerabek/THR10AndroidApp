package cz.fjerabek.thr10controller.parser

import cz.fjerabek.thr.data.bluetooth.IBluetoothMessage
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer

class JsonParser(private val serializer: Json) : IMessageParser {
    override fun deserialize(string: String): IBluetoothMessage =
        serializer.decodeFromString(string)

    override fun serialize(message: IBluetoothMessage): String = serializer.encodeToString(message)
}