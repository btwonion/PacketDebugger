package dev.nyon.packetdebugger.recording

import kotlinx.datetime.Instant

data class Recording(var packets: MutableList<RecordedPacket>, var start: Instant, var end: Instant)