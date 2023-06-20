package com.cramsan.ps2link.appfrontend

import kotlinx.datetime.Instant

/**
 *
 */
expect fun formatSimpleDate(instant: Instant): String

/**
 *
 */
expect fun formatSimpleDateTime(instant: Instant): String
