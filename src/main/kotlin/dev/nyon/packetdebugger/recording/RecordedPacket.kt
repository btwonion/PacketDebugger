package dev.nyon.packetdebugger.recording

import kotlinx.datetime.Instant
import net.minecraft.network.PacketListener
import net.minecraft.network.chat.Component
import net.minecraft.network.protocol.Packet

interface RecordedPacket {
    val packet: Packet<out PacketListener>
    val executed: Instant

    fun style(): List<Component>
}