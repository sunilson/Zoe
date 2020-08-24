package at.sunilson.core.extensions

import java.time.ZonedDateTime

fun ZonedDateTime.isSameMonth(otherDateTime: ZonedDateTime) =
    this.month == otherDateTime.month && this.year == otherDateTime.year

fun ZonedDateTime.isSameDay(otherDateTime: ZonedDateTime) =
    this.year == otherDateTime.year && this.dayOfYear == otherDateTime.dayOfYear