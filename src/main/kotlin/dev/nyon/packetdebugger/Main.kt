@file:Suppress("unused")

package dev.nyon.packetdebugger

import com.mojang.blaze3d.platform.InputConstants
import dev.nyon.packetdebugger.config.loadConfig
import dev.nyon.packetdebugger.gui.server.openRecordingGUI
import dev.nyon.packetdebugger.recording.Recording
import kotlinx.datetime.Clock
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper
import net.minecraft.client.KeyMapping
import net.minecraft.client.Minecraft
import net.silkmc.silk.commands.PermissionLevel
import net.silkmc.silk.commands.command
import net.silkmc.silk.core.text.literalText
import org.lwjgl.glfw.GLFW

const val modId = "packet-debugger"
var client: Minecraft = Minecraft.getInstance()
var currentRecording: Recording? = null
var recordingCache: MutableList<Recording> = mutableListOf()

fun initClient() {
    loadConfig()

    val toggleDebugging = KeyBindingHelper.registerKeyBinding(
        KeyMapping(
            "Toggle debugging", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_P, "PacketDebugger"
        )
    )

    while (toggleDebugging.consumeClick()) {
        enabled = !enabled

        if (!enabled) {
            currentRecording!!.end = Clock.System.now()
            recordingCache.add(currentRecording!!)

            print(currentRecording)
            currentRecording = null
            return
        }

        print("true")
    }
}

fun initServer() {
    loadConfig()

    command("packetdebugger") {
        alias("pd", "packetd")
        requiresPermissionLevel(PermissionLevel.OWNER)
        literal("start") {
            runs {
                if (currentRecording != null) {
                    source.sendSystemMessage(literalText("There is already a recording in progress.") {
                        color = 0x713329
                    })
                    return@runs
                }

                currentRecording = Recording(mutableListOf(), Clock.System.now(), null)
                source.sendSystemMessage(literalText("Recording has started!") {
                    color = 0x477127
                })
            }
        }

        literal("stop") {
            runs {
                if (currentRecording == null) {
                    source.sendSystemMessage(literalText("There is no recording in progress.") {
                        color = 0x713329
                    })
                    return@runs
                }

                currentRecording!!.end = Clock.System.now()
                recordingCache.add(currentRecording!!)
                currentRecording = null
                source.sendSystemMessage(literalText("Recording has stopped!") {
                    color = 0x477127
                })
            }
        }

        literal("clear") {
            runs {
                recordingCache.clear()
                source.sendSystemMessage(literalText("Cache has been cleared!") {
                    color = 0x477127
                })
            }
        }

        literal("view") {
            runs {
                source.playerOrException.openRecordingGUI()
            }
        }
    }
}

object PacketDebugger {

    fun init() {
        loadConfig()
    }
}