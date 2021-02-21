package cz.fjerabek.thr10controller

import android.app.Application
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
import cz.fjerabek.thr10controller.parser.IMessageParser
import cz.fjerabek.thr10controller.parser.JsonParser
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.dsl.module
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass

class Application : Application() {

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
        }
    }

    private val serializerModule = module {
        single {
            Json {
                prettyPrint = true
                serializersModule = jsonSerializerModule
            }
        }

        single<IMessageParser> {
            JsonParser(get())
        }
    }

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@Application)
            modules(
                serializerModule
            )
        }
    }
}