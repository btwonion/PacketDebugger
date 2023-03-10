package dev.nyon.packetdebugger.recording

import dev.nyon.packetdebugger.config.config
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import net.minecraft.network.PacketListener
import net.minecraft.network.chat.Component
import net.minecraft.network.protocol.Packet

data class ReceivedPacket(
    override val packet: Packet<out PacketListener>,
) : RecordedPacket {
    override val executed: Instant = Clock.System.now()
    override fun style(): List<Component> {
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