package com.cesarandres.ps2link.fragments.profilepager.weaponlist

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.cramsan.ps2link.appfrontend.profilepager.weaponlist.WeaponListCompose
import com.cramsan.ps2link.appfrontend.profilepager.weaponlist.WeaponListEventHandler
import com.cramsan.ps2link.core.models.Faction
import kotlinx.collections.immutable.persistentListOf

@Preview
@Composable
fun Preview() {
    WeaponListCompose(
        weaponList = persistentListOf(),
        useVerticalMode = false,
        faction = Faction.TR,
        isLoading = true,
        isError = false,
        eventHandler = object : WeaponListEventHandler {
            override fun onRefreshRequested() = Unit
        },
    )
}
