package cz.fjerabek.thr10controller.data.controls

import cz.fjerabek.thr10controller.data.enums.mainpanel.EMainPanel

object IDMainPanel {
    const val AMP : Byte = 0x00
    const val GAIN : Byte = 0x01
    const val MASTER : Byte = 0x02
    const val BASS : Byte = 0x03
    const val MIDDLE : Byte = 0x04
    const val TREBLE : Byte = 0x05
    const val CABINET : Byte = 0x06
}

object IDCompressor {
    const val STATUS : Byte = 0x1F
    const val TYPE : Byte = 0x10

    object IDRack {
        const val THRESHOLD : Byte = 0x11
        const val ATTACK : Byte = 0x13
        const val RELEASE : Byte = 0x14
        const val RATIO : Byte = 0x15
        const val KNEE : Byte = 0x16
        const val OUTPUT : Byte = 0x17
    }

    object IDStomp {
        const val SUSTAIN : Byte = 0x11
        const val OUTPUT : Byte = 0x12
    }
}

object IDDelay {
    const val STATUS : Byte = 0x3F
    const val TIME : Byte = 0x31
    const val FEEDBACK : Byte = 0x33
    const val HIGH_CUT : Byte = 0x34
    const val LOW_CUT : Byte = 0x36
    const val LEVEL : Byte = 0x38
}

object IDEffect {
    const val STATUS : Byte = 0x2F
    const val TYPE : Byte = 0x20

    object IDChorus {
        const val SPEED : Byte = 0x21
        const val DEPTH : Byte = 0x22
        const val MIX : Byte = 0x23
    }

    object IDFlanger {
        const val SPEED : Byte = 0x21
        const val MANUAL : Byte = 0x22
        const val DEPTH : Byte = 0x23
        const val FEEDBACK : Byte = 0x24
        const val SPREAD : Byte = 0x25
    }

    object IDPhaser {
        const val SPEED : Byte = 0x21
        const val MANUAL : Byte = 0x22
        const val DEPTH : Byte = 0x23
        const val FEEDBACK : Byte = 0x24
    }

    object IDTremolo {
        const val FREQ : Byte = 0x21
        const val DEPTH : Byte = 0x22
    }
}

object IDGate {
    const val STATUS : Byte = 0x5F
    const val THRESHOLD : Byte = 0x51
    const val RELEASE : Byte = 0x52
}

object IDReverb {
    const val STATUS : Byte = 0x4F
    const val TYPE : Byte = 0x40

    object IDHall {
        const val TIME : Byte = 0x41
        const val PRE_DELAY : Byte = 0x43
        const val LOW_CUT : Byte = 0x45
        const val HIGH_CUT : Byte = 0x47
        const val HIGH_RATIO : Byte = 0x49
        const val LOW_RATIO : Byte = 0x4A
        const val LEVEL : Byte = 0x4B
    }

    object IDPlate {
        const val TIME : Byte = 0x41
        const val PRE_DELAY : Byte = 0x43
        const val LOW_CUT : Byte = 0x45
        const val HIGH_CUT : Byte = 0x47
        const val HIGH_RATIO : Byte = 0x49
        const val LOW_RATIO : Byte = 0x4A
        const val LEVEL : Byte = 0x4B
    }

    object IDRoom {
        const val TIME : Byte = 0x41
        const val PRE_DELAY : Byte = 0x43
        const val LOW_CUT : Byte = 0x45
        const val HIGH_CUT : Byte = 0x47
        const val HIGH_RATIO : Byte = 0x49
        const val LOW_RATIO : Byte = 0x4A
        const val LEVEL : Byte = 0x4B
    }

    object IDSpring {
        const val REVERB : Byte = 0x41
        const val FILTER : Byte = 0x42
    }

}
