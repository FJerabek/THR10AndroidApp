package cz.fjerabek.thr10controller.data.message.serial

import kotlinx.serialization.Serializable

@Serializable
class FirmwareVersionMessage(
    var major : Int,
    var minor : Int,
    var patch : Int
) {

    companion object {
        fun fromString(string : String) : FirmwareVersionMessage {
            val parameters = string.split(";")
            require(parameters[0] == "\$ok") {"Invalid firmware message data"}
            return FirmwareVersionMessage(parameters[1].toInt(), parameters[2].toInt(), parameters[3].toInt())
        }
    }
}