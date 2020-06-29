package cz.fjerabek.thr10controller.data.message.serial

import cz.fjerabek.thr10controller.data.message.bluetooth.BtMessage

interface MessageHandler {
    fun receiveMessage(message : BtMessage)
}