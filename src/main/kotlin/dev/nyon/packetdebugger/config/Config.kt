package dev.nyon.packetdebugger.config

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import net.fabricmc.loader.api.FabricLoader
import kotlin.io.path.createDirectories
import kotlin.io.path.readText
import kotlin.io.path.writeText

val json = Json {
    encodeDefaults = true
    prettyPrint = true
}

val path = FabricLoader.getInstance().configDir.resolve("packet-debugger.json").createDirectories()
var config: ConfigModel = ConfigModel()

fun loadConfig() {
    val text = path.readText()
    if (text.isEmpty()) saveConfig()
    else config = json.decodeFromString(text)
}

fun saveConfig() {
    path.writeText(json.encodeToString(config))
}