package at.sunilson.presentationcore.extensions

import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAccessor

fun TemporalAccessor.formatFull() = DateTimeFormatter.ofPattern("dd.MM.YYYY HH:mm").format(this)
