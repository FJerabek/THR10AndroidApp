package cz.fjerabek.thr10controller.data.message.bluetooth

import cz.fjerabek.thr10controller.data.message.bluetooth.BtMessage

interface IBtMessageSender {
    fun sendMessage(message : BtMessage)
}