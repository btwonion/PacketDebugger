package dev.nyon.packetdebugger.mixins;

import dev.nyon.packetdebugger.MainKt;
import dev.nyon.packetdebugger.recording.SentPacket;
import net.minecraft.network.Connection;
import net.minecraft.network.ConnectionProtocol;
import net.minecraft.network.PacketSendListener;
import net.minecraft.network.protocol.Packet;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Connection.class)
public class ConnectionMixin {

    @Inject(
        method = "doSendPacket",
        at = @At(
            value = "TAIL"
        )
    )
    public void invokePacketSent(
        Packet<?> packet,
        @Nullable PacketSendListener sendListener,
        ConnectionProtocol newProtocol,
        ConnectionProtocol currentProtocol,
        CallbackInfo ci
    ) {
        var recording = MainKt.getCurrentRecording();
        if (recording != null) recording.getPackets().add(new SentPacket(packet));
    }
}
