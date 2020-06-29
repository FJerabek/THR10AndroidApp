package cz.fjerabek.thr10controller.data.message.bluetooth

enum class EMessageType {
    GET_PRESETS,
    PRESETS_STATUS,
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