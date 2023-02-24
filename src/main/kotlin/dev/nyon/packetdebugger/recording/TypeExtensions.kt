package dev.nyon.packetdebugger.recording

import dev.nyon.packetdebugger.config.config
import java.util.*

fun Class<*>.color(): Int = when (this) {
    Boolean.Companion::class.java -> config.typeColors.booleanColor
    Int.Companion::class.java -> config.typeColors.intColor
    Long.Companion::class.java -> config.typeColors.longColor
    Double.Companion::class.java -> config.typeColors.doubleColor
    Short.Companion::class.java -> config.typeColors.shortColor
    String.Companion::class.java -> config.typeColors.stringColor
    UUID::class.java -> config.typeColors.uuidColor
    else -> config.typeColors.defaultColor
}