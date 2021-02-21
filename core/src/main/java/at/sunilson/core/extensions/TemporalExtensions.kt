package at.sunilson.core.extensions

import java.time.LocalDate
import java.time.ZonedDateTime

fun LocalDate.isSameMonth(otherDate: LocalDate) =
    this.month == otherDate.month && this.year == otherDate.year

fun ZonedDateTime.isSameMonth(otherDateTime: ZonedDateTime) =
    this.month == otherDateTime.month && this.year == otherDateTime.year

fun ZonedDateTime.isSameDay(otherDateTime: ZonedDateTime) =
    this.year == otherDateTime.year && this.dayOfYear == otherDateTime.dayOfYear
