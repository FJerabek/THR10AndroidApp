package cz.fjerabek.thr10.midi.message

import cz.fjerabek.thr10.IMessage
import cz.fjerabek.thr10.Preset
import kotlinx.serialization.Serializable

@Serializable
class PresetMessage(var preset : Preset) : IMessage {

    private fun calculateChecksum(data : ByteArray) : Byte {
        var count = 0x71
        for (i in data) {
            count += i
        }
        return ((count.inv() + 1) and 0x7f).toByte()
    }

    override fun getSysexData(): ByteArray {
        val setArray = ByteArray(274) {0}
        val prefix = byteArrayOf(67, 125, 0, 2, 12, 68, 84, 65, 49, 65, 108, 108, 80, 0, 0, 127, 127)

        for (i in prefix.indices) {
            setArray[i] = prefix[i]
        }

        preset.toDump(setArray)

        setArray[setArray.size - 1] = calculateChecksum(setArray.copyOfRange(17, setArray.size))

        return byteArrayOf(0xf0.toByte()).plus(setArray).plus(0xf7.toByte())
    }
}