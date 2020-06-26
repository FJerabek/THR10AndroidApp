package cz.fjerabek.thr10.serial.message

class ButtonMessage(var id : Int,
                    var pressed : Boolean,
                    var pressedTime : Long) {

    companion object {
        fun isButtonMessage(string : String) : Boolean {
            val parameters = string.split(";")
            return parameters[0] == "\$btn"
        }

        fun fromData(string : String): ButtonMessage {
            val parameters = string.split(";")
            require(parameters[0] == "\$btn") { "Illegal button message data" }
            var timePressed : Long = 0

            if(parameters.size > 3)
                timePressed = parameters[3].toLong()

            return ButtonMessage(
                parameters[1].toInt(),
                parameters[2] == "1",
                timePressed
            )
        }
    }
}