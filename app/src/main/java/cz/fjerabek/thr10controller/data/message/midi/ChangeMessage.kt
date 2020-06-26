package cz.fjerabek.thr10.midi.message

import cz.fjerabek.thr10.IMessage
import cz.fjerabek.thr10.bluetooth.messages.EMessageType
import kotlinx.serialization.Serializable

@Serializable
class ChangeMessage(
    var property: Byte,
    var value : Int
) : IMessage {
    val type = EMessageType.CHANGE
    override fun getSysexData(): ByteArray {
        val valueArray = byteArrayOf((value / 128).toByte(), (value % 128).toByte())
        val prefix = byteArrayOf(0xf0.toByte(), 0x43, 0x7D, 0x10, 0x41, 0x30, 0x01)

        return prefix.plus(property).plus(valueArray).plus(0xF7.toByte())
    }

    companion object {
        fun fromData(data : ByteArray) : ChangeMessage {
            return ChangeMessage(data[data.size - 4], data[data.size - 3] * 128 + data[data.size - 2])
        }
    }

}