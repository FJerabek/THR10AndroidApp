package cz.fjerabek.thr10controller.data.message

import cz.fjerabek.thr10controller.data.message.bluetooth.BtChangeMessage
import cz.fjerabek.thr10controller.data.message.bluetooth.BtPresetChangeMessage
import cz.fjerabek.thr10controller.data.message.bluetooth.BtPresetsMessage
import cz.fjerabek.thr10controller.data.message.bluetooth.BtUartStatusMessage

interface MessageHandler {
    fun handlePresetsStatusMessage(message : BtPresetsMessage)
    fun handleChangeMessage(message : BtChangeMessage)
    fun handleUartStatusMessage(message : BtUartStatusMessage)
    fun handlePresetChangeMessage(message : BtPresetChangeMessage)
}