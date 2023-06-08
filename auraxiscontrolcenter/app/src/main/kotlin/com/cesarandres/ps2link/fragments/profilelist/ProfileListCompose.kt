package com.cesarandres.ps2link.fragments.profilelist

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.cramsan.ps2link.appfrontend.profilelist.ProfileListCompose
import com.cramsan.ps2link.appfrontend.profilelist.ProfileListEventHandler
import com.cramsan.ps2link.core.models.Namespace
import kotlinx.collections.immutable.persistentListOf

@Preview
@Composable
fun NormalButtonPreview() {
    ProfileListCompose(
        profileItems = persistentListOf(),
        eventHandler = object : ProfileListEventHandler {
            override fun onProfileSelected(profileId: String, namespace: Namespace) = Unit
        },
    )
}
