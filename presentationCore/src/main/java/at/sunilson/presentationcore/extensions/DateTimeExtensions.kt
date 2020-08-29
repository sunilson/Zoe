package at.sunilson.presentationcore.extensions

import java.time.Duration
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAccessor

fun TemporalAccessor.formatFull() = DateTimeFormatter.ofPattern("dd.MM.YYYY HH:mm").format(this)
fun TemporalAccessor.formatPattern(pattern: String) =
    DateTimeFormatter.ofPattern(pattern).format(this)

fun Duration.format(): String {
    val seconds = seconds
    return String.format("%02dh:%02dm", seconds / 3600, (seconds % 3600) / 60);
}

fun Instant.toZonedDateTime() = ZonedDateTime.from(this).withZoneSameInstant(ZoneId.systemDefault())