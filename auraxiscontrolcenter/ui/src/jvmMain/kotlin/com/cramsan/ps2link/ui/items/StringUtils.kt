package com.cramsan.ps2link.ui.items

import androidx.compose.runtime.Composable
import com.cramsan.ps2link.ui.unknownString

@Composable
actual fun TextAllTime(value: Double?): String {
    return "All Time: ${value ?: unknownString()}"
}

@Composable
actual fun TextToday(value: Double?): String {
    return "Today: ${value ?: unknownString()}"
}

@Composable
actual fun TextThisWeek(value: Double?): String {
    return "This Week: ${value ?: unknownString()}"
}

@Composable
actual fun TextThisMonth(value: Double?): String {
    return "This Month: ${value ?: unknownString()}"
}
