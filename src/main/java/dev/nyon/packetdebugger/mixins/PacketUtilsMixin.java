package dev.nyon.packetdebugger.mixins;

import dev.nyon.packetdebugger.MainKt;
import dev.nyon.packetdebugger.recording.ReceivedPacket;
import net.minecraft.network.PacketListener;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.PacketUtils;
import net.minecraft.server.RunningOnDifferentThreadException;
import net.minecraft.util.thread.BlockableEventLoop;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PacketUtils.class)
public class PacketUtilsMixin {

    static final Logger logger = PacketUtilsAccessor.getLogger();

    @Inject(
        method = "ensureRunningOnSameThread(Lnet/minecraft/network/protocol/Packet;Lnet/minecraft/network/PacketListener;Lnet/minecraft/util/thread/BlockableEventLoop;)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/util/thread/BlockableEventLoop;executeIfPossible(Ljava/lang/Runnable;)V"
        ),
        cancellable = true
    )
    private static <T extends PacketListener> void handleReceivedPackets(
        Packet<T> packet,
        T processor,
        BlockableEventLoop<?> executor,
        CallbackInfo ci
    ) {
        var currentRecording = MainKt.getCurrentRecording();
        if (!executor.isSameThread()) {
            executor.executeIfPossible(() -> {
                if (processor.getConnection().isConnected()) {
                    try {
                        packet.handle(processor);
                        if (currentRecording != null) currentRecording.getPackets().add(new ReceivedPacket(packet));
                    } catch (Exception var3) {
                        if (processor.shouldPropagateHandlingExceptions()) {
                            throw var3;
                        }

                        logger.error("Failed to handle packet {}, suppressing error", packet, var3);
                    }
                } else {
                    logger.debug("Ignoring packet due to disconnection: {}", packet);
                }
            });
            throw RunningOnDifferentThreadException.RUNNING_ON_DIFFERENT_THREAD;
        }
        else if (currentRecording != null) currentRecording.getPackets().add(new ReceivedPacket(packet));
        ci.cancel();
    }
}
