package cz.fjerabek.thr10controller.data.message.serial

import cz.fjerabek.thr10controller.data.message.bluetooth.BtMessage

interface MessageReceiver {
    fun receiveMessage(message : BtMessage)
}