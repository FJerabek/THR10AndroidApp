package cz.fjerabek.thr10.bluetooth.messages

enum class EMessageType {
    GET_PRESETS,
    ADD_PRESET,
    SET_PRESET,
    CHANGE,
    SET_PRESETS,
    GET_STATUS,
    PRESET_CHANGE,
    POWER_OFF,
    LAMP,
    WIDE_STEREO,
    DUMP_REQUEST,
    FIRMWARE_STATUS,
    MIDI_DISCONNECTED,
    UART_STATUS
}