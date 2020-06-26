package cz.fjerabek.thr10.enums

interface IControlProperty {
    fun getPropertyId() : Byte
    fun getMinimumValue() : Int
    fun getMaximumValue() : Int
}