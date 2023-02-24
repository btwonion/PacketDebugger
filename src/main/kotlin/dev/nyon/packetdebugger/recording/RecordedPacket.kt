package dev.nyon.packetdebugger.recording

import net.minecraft.network.PacketListener
import net.minecraft.network.chat.Component
import net.minecraft.network.protocol.Packet

interface RecordedPacket {
    val packet: Packet<out PacketListener>
    fun style(packet: Packet<out PacketListener>): List<Component>
}