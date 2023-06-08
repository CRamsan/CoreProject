package com.cramsan.ps2link.appfrontend

import kotlinx.datetime.Instant

expect fun FormatSimpleDate(instant: Instant): String

expect fun FormatSimpleDateTime(instant: Instant): String
