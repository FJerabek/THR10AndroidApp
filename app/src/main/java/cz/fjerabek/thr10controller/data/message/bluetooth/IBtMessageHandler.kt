package cz.fjerabek.thr10controller.data.message.bluetooth

import cz.fjerabek.thr10controller.data.message.bluetooth.*

interface IBtMessageHandler {
    fun handlePresetsStatusMessage(message : BtPresetsMessage)
    fun handleChangeMessage(message : BtChangeMessage)
    fun handleUartStatusMessage(message : BtUartStatusMessage)
    fun handlePresetChangeMessage(message : BtPresetChangeMessage)
    fun handleDumpMessage(message : BtPresetMessage)
}