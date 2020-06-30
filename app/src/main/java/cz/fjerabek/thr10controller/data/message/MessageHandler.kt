package cz.fjerabek.thr10controller.data.message

import cz.fjerabek.thr10controller.data.message.bluetooth.*

interface MessageHandler {
    fun handlePresetsStatusMessage(message : BtPresetsMessage)
    fun handleChangeMessage(message : BtChangeMessage)
    fun handleUartStatusMessage(message : BtUartStatusMessage)
    fun handlePresetChangeMessage(message : BtPresetChangeMessage)
    fun handleDumpMessage(message : BtPresetMessage)
}