package cz.fjerabek.thr10controller.data.controls

interface IControl {
    fun toDump(dump : ByteArray) : ByteArray
}