package dev.nyon.packetdebugger.mixins;

import net.minecraft.network.protocol.PacketUtils;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(PacketUtils.class)
public interface PacketUtilsAccessor {

    @Accessor("LOGGER")
    static Logger getLogger() {
        throw new AssertionError();
    }
}
