package com.cramsan.ps2link.ui.items

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.cramsan.ps2link.ui.R

@Composable
actual fun TextAllTime(value: Double?): String {
    return stringResource(
        R.string.text_stat_all,
        value?.toString() ?: stringResource(R.string.text_unknown)
    )
}

@Composable
actual fun TextToday(value: Double?): String {
    return stringResource(
        R.string.text_stat_today,
        value?.toString() ?: stringResource(R.string.text_unknown)
    )
}

@Composable
actual fun TextThisWeek(value: Double?): String {
    return stringResource(
        R.string.text_stat_week,
        value?.toString() ?: stringResource(R.string.text_unknown)
    )
}

@Composable
actual fun TextThisMonth(value: Double?): String {
    return stringResource(
        R.string.text_stat_month,
        value?.toString() ?: stringResource(R.string.text_unknown)
    )
}
