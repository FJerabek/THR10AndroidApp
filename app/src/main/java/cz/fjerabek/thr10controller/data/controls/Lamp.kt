package cz.fjerabek.thr10controller.data.controls

import kotlinx.serialization.Serializable

@Serializable
class Lamp(var status : Boolean){
    fun toDump(): ByteArray {
        return if(status) {
            byteArrayOf(0xF0.toByte(), 0x43, 0x7D, 0x30, 0x41, 0x30, 0x01, 0x00, 0xF7.toByte())
        } else {
            byteArrayOf(0xF0.toByte(), 0x43, 0x7D, 0x30, 0x41, 0x30, 0x01, 0x01, 0xF7.toByte())
        }
    }

}