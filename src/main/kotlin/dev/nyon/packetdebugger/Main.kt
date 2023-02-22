@file:Suppress("unused")

package dev.nyon.packetdebugger

import com.mojang.blaze3d.platform.InputConstants
import dev.nyon.packetdebugger.config.loadConfig
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper
import net.minecraft.client.KeyMapping
import net.minecraft.client.Minecraft
import org.lwjgl.glfw.GLFW

const val modId = "packet-debugger"
var client: Minecraft = Minecraft.getInstance()

fun initClient() {
    loadConfig()

    val toggleDebugging = KeyBindingHelper.registerKeyBinding(
        KeyMapping(
            "Toggle debugging", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_P, "PacketDebugger"
        )
    )

    while (toggleDebugging.consumeClick()) {
        enabled = !enabled


    }
}

fun initServer() {
    loadConfig()
}

object PacketDebugger {

    fun init() {
        loadConfig()
    }
}