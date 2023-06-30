package com.cramsan.ps2link.appfrontend.profilepager.weaponlist

import androidx.compose.runtime.Composable

@Composable
actual fun KillsText(count: Long?): String {
    return "KILLS: ${count ?: 0}"
}

@Composable
actual fun VehiclesDestroyedText(count: Long?): String {
    return "VEHICLES DESTROYED: ${count ?: 0}"
}

@Composable
actual fun HeadshotsText(count: Long?): String {
    return "HEADSHOTS: ${count ?: 0}"
}

@Composable
actual fun NCCountText(count: Long): String {
    return "NC: $count"
}

@Composable
actual fun TRCountText(count: Long): String {
    return "TR: $count"
}

@Composable
actual fun VSCountText(count: Long): String {
    return "VS: $count"
}
