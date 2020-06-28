package cz.fjerabek.thr10controller.data.enums

enum class ECharging(val value : String) {
    DISCHARGING("dis"),
    CHARGING("chg"),
    FULL("end");

    companion object {
            private val map = values().associateBy(ECharging::value)

            fun fromValue(value : String) = map[value]
    }
}