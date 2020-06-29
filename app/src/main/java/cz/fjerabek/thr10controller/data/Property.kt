package cz.fjerabek.thr10controller.data

import kotlin.reflect.KClass

@Target(AnnotationTarget.PROPERTY)
annotation class Property(val propertyId : Byte) {
}