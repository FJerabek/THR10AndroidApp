package cz.fjerabek.thr10controller.data.enums

interface IControlProperty {
    fun getPropertyId() : Byte
    fun getMinimumValue() : Int
    fun getMaximumValue() : Int
}