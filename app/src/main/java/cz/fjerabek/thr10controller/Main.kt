package cz.fjerabek.thr10controller

import cz.fjerabek.thr10controller.data.Preset
import cz.fjerabek.thr10controller.data.controls.IDMainPanel
import cz.fjerabek.thr10controller.data.controls.MainPanel
import cz.fjerabek.thr10controller.data.enums.mainpanel.EAmpType
import cz.fjerabek.thr10controller.data.enums.mainpanel.ECabinetType
import cz.fjerabek.thr10controller.data.message.midi.ChangeMessage
import kotlin.reflect.full.starProjectedType

var preset = Preset("test", MainPanel(EAmpType.ACO, 10, 10, 10,10, 10, null))


fun main() {
    println(preset.mainPanel.cabinet)
    preset.processChangeMessage(ChangeMessage(IDMainPanel.CABINET, ECabinetType.AMERICAN_4X12.id.toInt()))
    println(preset.mainPanel.cabinet)
}