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
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass
import timber.log.Timber

/**
 * Application entry class
 */
class Application : Application() {
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }
}