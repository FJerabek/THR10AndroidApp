package cz.fjerabek.thr10controller.data.message

import cz.fjerabek.thr10controller.data.message.bluetooth.BtMessage

interface MessageSender {
    fun sendMessage(message : BtMessage)
}