package cz.fjerabek.thr10controller.data.controls

import cz.fjerabek.thr10controller.data.Property
import cz.fjerabek.thr10controller.data.enums.EStatus
import cz.fjerabek.thr10controller.data.enums.mainpanel.EAmpType
import cz.fjerabek.thr10controller.data.enums.mainpanel.ECabinetType
import cz.fjerabek.thr10controller.data.message.midi.ChangeMessage
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty
import kotlin.reflect.full.createType
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.starProjectedType
import kotlin.reflect.full.withNullability

interface IControl {
    fun toDump(dump : ByteArray) : ByteArray

    fun processChangeMessage(message: ChangeMessage): Boolean {
        val property = this::class.memberProperties.find { member ->
            member.annotations.find{ it is Property && it.propertyId == message.property } != null
        }


        return if (property is KMutableProperty<*>) {
            property.setter.call(this, when(property.setter.parameters[1].type) {
                EAmpType::class
                    .createType() -> EAmpType.fromId(message.value.toByte())

                Byte::class
                    .createType() -> message.value.toByte()

                Int::class
                    .createType() -> message.value

                EStatus::class
                    .createType() -> EStatus.fromValue(message.value.toByte())

                ECabinetType::class
                    .createType()
                    .withNullability(true) -> ECabinetType.fromId(message.value.toByte())

                else -> error("Received change message for unsupported type ${property.setter.parameters[1].type}")
            })
            true
        } else {
            false
        }
    }
}