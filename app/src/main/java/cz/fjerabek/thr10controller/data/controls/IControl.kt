package cz.fjerabek.thr10.controls

interface IControl {
    fun toDump(dump : ByteArray) : ByteArray
}