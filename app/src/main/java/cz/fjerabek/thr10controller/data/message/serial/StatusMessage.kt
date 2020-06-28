package cz.fjerabek.thr10controller.data.message.serial

import cz.fjerabek.thr10controller.data.enums.ECharging
import kotlinx.serialization.Serializable

@Serializable
class StatusMessage (
    var uptime : Long,
    var battery : Int,
    var charging : ECharging,
    var current : Int) {

    companion object {
        fun fromString(string : String) : StatusMessage {
            val parameters = string.split(";")
            require(parameters[0] == "\$ok") {"Invalid status message data"}
            return StatusMessage(
                parameters[1].toLong(), parameters[2].toInt(),
                ECharging.fromValue(parameters[3])!!, parameters[4].toInt()
            )
        }
    }
}