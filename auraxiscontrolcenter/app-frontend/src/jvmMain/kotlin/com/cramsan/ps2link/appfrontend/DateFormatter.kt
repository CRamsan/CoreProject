package com.cramsan.ps2link.appfrontend

import kotlinx.datetime.Instant
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 *
 */
actual fun formatSimpleDate(instant: Instant): String {
    return simpleFormatter.format(Date(instant.toEpochMilliseconds()))
}

/**
 *
 */
actual fun formatSimpleDateTime(instant: Instant): String {
    return fullFormatter.format(Date(instant.toEpochMilliseconds()))
}

private var simpleFormatter = SimpleDateFormat("MMMM dd, yyyy")
private var fullFormatter = SimpleDateFormat("MMM dd hh:mm:ss a", Locale.getDefault())
