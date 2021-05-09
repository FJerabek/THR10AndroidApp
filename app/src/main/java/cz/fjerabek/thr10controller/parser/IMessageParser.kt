package cz.fjerabek.thr10controller.parser

import cz.fjerabek.thr.data.bluetooth.IBluetoothMessage

/**
 * Interface for bluetooth message parser
 */
interface IMessageParser {
    /**
     * Deserializes message from string
     * @param string sting message
     * @return bluetooth message object
     */
    fun deserialize(string: String): IBluetoothMessage

    /**
     * Serializes bluetooth message to string
     * @param message bluetooth message object
     * @return serialized message
     */
    fun serialize(message: IBluetoothMessage): String
}