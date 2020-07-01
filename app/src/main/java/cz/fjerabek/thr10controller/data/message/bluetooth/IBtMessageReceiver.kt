package cz.fjerabek.thr10controller.data.message.bluetooth

import cz.fjerabek.thr10controller.data.message.bluetooth.BtMessage

interface IBtMessageReceiver {
    fun receiveMessage(message : BtMessage)
}