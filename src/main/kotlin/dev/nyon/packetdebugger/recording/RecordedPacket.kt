package dev.nyon.packetdebugger.recording

import net.minecraft.network.chat.Component
import net.minecraft.network.protocol.Packet

interface RecordedPacket<P : Packet<*>> {
    val packet: P
    fun style(packet: P): List<Component>
}