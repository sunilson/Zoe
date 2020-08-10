package at.sunilson.presentationcore.extensions

/**
 * Adds a zero in front of this and returns as [String]
 */
fun Int.padZero() = String.format("%02d", this)