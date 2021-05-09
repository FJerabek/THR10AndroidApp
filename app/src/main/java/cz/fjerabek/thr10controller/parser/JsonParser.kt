package cz.fjerabek.thr10controller.parser

import cz.fjerabek.thr.data.bluetooth.*
import cz.fjerabek.thr.data.controls.compressor.Compressor
import cz.fjerabek.thr.data.controls.compressor.Rack
import cz.fjerabek.thr.data.controls.compressor.Stomp
import cz.fjerabek.thr.data.controls.effect.*
import cz.fjerabek.thr.data.controls.reverb.*
import cz.fjerabek.thr.data.midi.ChangeMessage
import cz.fjerabek.thr.data.midi.HeartBeatMessage
import cz.fjerabek.thr.data.midi.IMidiMessage
import cz.fjerabek.thr.data.midi.PresetMessage
import cz.fjerabek.thr.data.uart.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass

/**
 * Json message parser object
 */
object JsonParser : IMessageParser {

    /**
     * Serializer module containing all abstract and its concrete definitions
     */
    private val jsonSerializerModule = SerializersModule {
        polymorphic(Effect::class) {
            subclass(Chorus::class)
            subclass(Flanger::class)
            subclass(Phaser::class)
            subclass(Tremolo::class)
        }

        polymorphic(Compressor::class) {
            subclass(Rack::class)
            subclass(Stomp::class)
        }

        polymorphic(Reverb::class) {
            subclass(Hall::class)
            subclass(Plate::class)
            subclass(Room::class)
            subclass(Spring::class)
        }

        polymorphic(IMidiMessage::class) {
            subclass(HeartBeatMessage::class)
            subclass(PresetMessage::class)
            subclass(ChangeMessage::class)
        }

        polymorphic(UartMessage::class) {
            subclass(ButtonMessage::class)
            subclass(FWVersionMessage::class)
            subclass(StatusMessage::class)
            subclass(ShutdownMessage::class)
        }

        polymorphic(IBluetoothMessage::class) {
            subclass(ButtonMessage::class)
            subclass(FWVersionMessage::class)
            subclass(StatusMessage::class)
            subclass(ShutdownMessage::class)
            subclass(HeartBeatMessage::class)
            subclass(PresetMessage::class)
            subclass(ChangeMessage::class)

            subclass(HwStatusRq::class)
            subclass(FwVersionRq::class)
            subclass(PresetsRq::class)
            subclass(PresetsResponse::class)
            subclass(PresetSelect::class)
            subclass(CurrentPresetRq::class)
            subclass(RemovePresetRq::class)
            subclass(AddPresetRq::class)
            subclass(Lamp::class)
            subclass(WideStereo::class)
            subclass(ConnectedRq::class)
            subclass(Connected::class)
            subclass(SetPresetsRq::class)
            subclass(CurrentPresetIndexRq::class)
        }
    }

    /**
     * Json serializer instance
     */
    private val serializer =
        Json {
            prettyPrint = false
            serializersModule = jsonSerializerModule
        }

    override fun deserialize(string: String): IBluetoothMessage =
        serializer.decodeFromString(string)

    override fun serialize(message: IBluetoothMessage): String = serializer.encodeToString(message)
}