package dev.nyon.packetdebugger.recording

import dev.nyon.packetdebugger.config.config
import dev.nyon.packetdebugger.util.color
import net.minecraft.network.PacketListener
import net.minecraft.network.chat.Component
import net.minecraft.network.protocol.Packet

data class ReceivedPacket<P : Packet<out PacketListener>>(
    override val packet: P
) : RecordedPacket<P> {
    override fun style(packet: P): List<Component> {
        val clazz = packet.javaClass
        val fields = clazz.declaredFields
        return buildList {
            add(Component.literal("Received Packet - ${clazz.simpleName}").withStyle {
                it.withColor(config.receivedPacketColor)
            })
            add(Component.literal(clazz.name).withStyle { it.withColor(config.fullClassNameColor) })
            add(Component.empty())
            fields.forEach { field ->
                add(Component.literal("${field.name}: ${field.get(field.type)}")
                    .withStyle { it.withColor(field.type.color()) })
            }
        }
    }
}