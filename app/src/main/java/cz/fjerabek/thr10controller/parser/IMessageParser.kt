package cz.fjerabek.thr10controller.parser

import cz.fjerabek.thr.data.bluetooth.IBluetoothMessage

interface IMessageParser {
    fun deserialize(string: String): IBluetoothMessage

    fun serialize(message: IBluetoothMessage): String
}