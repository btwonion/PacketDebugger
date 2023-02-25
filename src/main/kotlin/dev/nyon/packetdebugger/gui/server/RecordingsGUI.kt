@file:Suppress("SpellCheckingInspection")
package dev.nyon.packetdebugger.gui.server

import dev.nyon.packetdebugger.config.config
import dev.nyon.packetdebugger.recording.ReceivedPacket
import dev.nyon.packetdebugger.recording.Recording
import dev.nyon.packetdebugger.recording.SentPacket
import dev.nyon.packetdebugger.recordingCache
import kotlinx.datetime.Clock
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.silkmc.silk.core.item.itemStack
import net.silkmc.silk.core.item.setCustomName
import net.silkmc.silk.core.item.setLore
import net.silkmc.silk.core.item.setSkullTexture
import net.silkmc.silk.core.text.literalText
import net.silkmc.silk.igui.*
import net.silkmc.silk.igui.observable.toGuiList

const val arrowRightHeadID =
    "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDJiMGMwN2ZhMGU4OTIzN2Q2NzllMTMxMTZiNWFhNzVhZWJiMzRlOWM5NjhjNmJhZGIyNTFlMTI3YmRkNWIxIn19fQ=="
const val arrowLeftHeadID =
    "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDU5YmUxNTU3MjAxYzdmZjFhMGIzNjk2ZDE5ZWFiNDEwNDg4MGQ2YTljZGI0ZDVmYTIxYjZkYWE5ZGIyZDEifX19"

fun ServerPlayer.openRecordingGUI() {
    var recording = Recording(mutableListOf(), Clock.System.now(), Clock.System.now())

    val gui = igui(
        GuiType.NINE_BY_SIX, literalText("Recording overview") { color = 0x25715F }, 0
    ) {
        page(0) {
            val comp = compound((2 sl 2) rectTo (5 sl 8), recordingCache.toGuiList(), {
                itemStack(Items.REDSTONE) {
                    setCustomName("${it.start} - ${it.end}") {
                        color = 0x929502
                    }
                }
            }) { e, rec ->
                recording = rec
                e.gui.changePage(e.gui.currentPage, e.gui.pagesByNumber[1] ?: error("Wrong inventory id"))
            }

            compoundScrollForwards(6 sl 9, itemStack(Items.PLAYER_HEAD) {
                setSkullTexture(arrowRightHeadID)
            }.guiIcon, comp)
            compoundScrollBackwards(6 sl 1, itemStack(Items.PLAYER_HEAD) {
                setSkullTexture(arrowLeftHeadID)
            }.guiIcon, comp)
        }

        page(1) {
            val comp = compound((2 sl 2) rectTo (5 sl 8), recording.packets.toGuiList(), {
                itemStack(
                    when (it) {
                        is SentPacket -> Item.byId(config.serverOptions.sentPacketItemID)
                        is ReceivedPacket -> Item.byId(config.serverOptions.receivedPacketItemID)
                        else -> {
                            Item.byId(config.serverOptions.sentPacketItemID)
                        }
                    }
                ) {
                    setCustomName(it.executed.toString()) {
                        color = 0x929502
                    }

                    setLore(it.style())
                }
            })

            compoundScrollForwards(6 sl 9, itemStack(Items.PLAYER_HEAD) {
                setSkullTexture(arrowRightHeadID)
            }.guiIcon, comp)
            compoundScrollBackwards(6 sl 1, itemStack(Items.PLAYER_HEAD) {
                setSkullTexture(arrowLeftHeadID)
            }.guiIcon, comp)
        }
    }

    openGui(gui)
}