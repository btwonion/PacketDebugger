package dev.nyon.packetdebugger.config

import com.akuleshov7.ktoml.Toml
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import net.fabricmc.loader.api.FabricLoader
import kotlin.io.path.createDirectories
import kotlin.io.path.readText
import kotlin.io.path.writeText

val toml = Toml()

val path = FabricLoader.getInstance().configDir.resolve("packet-debugger.json").createDirectories()
var config: ConfigModel = ConfigModel()

fun loadConfig() {
    val text = path.readText()
    if (text.isEmpty()) saveConfig()
    else config = toml.decodeFromString(text)
}

fun saveConfig() {
    path.writeText(toml.encodeToString(config))
}