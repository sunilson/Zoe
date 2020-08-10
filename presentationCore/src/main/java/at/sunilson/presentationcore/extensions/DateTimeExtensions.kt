package at.sunilson.presentationcore.extensions

import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAccessor

fun TemporalAccessor.formatFull() = DateTimeFormatter.ofPattern("dd.MM.YYYY HH:mm").format(this)

fun Instant.toZonedDateTime() = ZonedDateTime.from(this).withZoneSameInstant(ZoneId.systemDefault())