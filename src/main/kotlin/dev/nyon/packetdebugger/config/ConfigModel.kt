package dev.nyon.packetdebugger.config

import kotlinx.serialization.Serializable

@Serializable
data class ConfigModel(
    val printInConsole: Boolean = true,
    val debugSent: Boolean = true,
    val debugReceived: Boolean = true,
    val receivedPacketColor: Int = 0x317C79,
    val sentPacketColor: Int = 0x7C2C75,
    val fullClassNameColor: Int = 0x444243,
    val typeColors: TypeColors = TypeColors()
)

@Serializable
data class TypeColors(
    val defaultColor: Int = 0x54728C,
    val booleanColor: Int = 0x711B1B,
    val intColor: Int = 0x71560D,
    val longColor: Int = 0x8C8107,
    val shortColor: Int = 0x598C12,
    val doubleColor: Int = 0x098C5A,
    val stringColor: Int = 0x12748C,
    val uuidColor: Int = 0x13428C
)