package com.cramsan.ps2link.appfrontend.profilepager.weaponlist

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.cramsan.ps2link.ui.R

@Composable
actual fun KillsText(count: Long?): String {
    return stringResource(R.string.text_kills, count ?: 0)
}

@Composable
actual fun VehiclesDestroyedText(count: Long?): String {
    return stringResource(
        R.string.text_vehicle_kills_,
        count ?: 0
    )
}

@Composable
actual fun HeadshotsText(count: Long?): String {
    return stringResource(
        R.string.text_vehicle_kills_,
        count ?: 0
    )
}

@Composable
actual fun NCCountText(count: Long): String {
    return stringResource(R.string.text_nc_, count)
}

@Composable
actual fun TRCountText(count: Long): String {
    return stringResource(R.string.text_tr_, count)
}

@Composable
actual fun VSCountText(count: Long): String {
    return stringResource(R.string.text_vs_, count)
}
