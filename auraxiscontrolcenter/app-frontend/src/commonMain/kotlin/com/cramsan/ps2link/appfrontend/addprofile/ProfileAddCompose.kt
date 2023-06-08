package com.cramsan.ps2link.appfrontend.addprofile

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.cramsan.ps2link.appfrontend.UnknownErrorText
import com.cramsan.ps2link.core.models.Character
import com.cramsan.ps2link.core.models.Namespace
import com.cramsan.ps2link.ui.ErrorOverlay
import com.cramsan.ps2link.ui.FrameBottom
import com.cramsan.ps2link.ui.FrameSlim
import com.cramsan.ps2link.ui.LoadingOverlay
import com.cramsan.ps2link.ui.SearchField
import com.cramsan.ps2link.ui.items.ProfileItem
import com.cramsan.ps2link.ui.theme.Padding
import kotlinx.collections.immutable.ImmutableList

/**
 * Render a page to add a profile to track in the app.
 */
@Composable
fun ProfileAddCompose(
    searchField: String,
    profileItems: ImmutableList<Character>,
    isLoading: Boolean,
    isError: Boolean,
    eventHandler: ProfileAddEventHandler,
) {
    FrameBottom {
        Column(modifier = Modifier.fillMaxSize()) {
            FrameSlim(modifier = Modifier.fillMaxWidth()) {
                SearchField(
                    value = searchField,
                    hint = PlayerNameText(),
                ) { text ->
                    eventHandler.onSearchFieldUpdated(text)
                }
            }

            Box(modifier = Modifier.fillMaxSize()) {
                LazyColumn {
                    items(profileItems) {
                        ProfileItem(
                            modifier = Modifier.fillMaxWidth().padding(vertical = Padding.micro),
                            label = it.name ?: "",
                            level = it.battleRank?.toInt() ?: 0,
                            faction = it.faction,
                            namespace = it.namespace,
                            onClick = { eventHandler.onProfileSelected(it.characterId, it.namespace) },
                        )
                    }
                }

                LoadingOverlay(enabled = isLoading)
                ErrorOverlay(
                    isError = isError,
                    error = UnknownErrorText()
                )
            }
        }
    }
}

interface ProfileAddEventHandler {
    fun onSearchFieldUpdated(searchField: String)
    fun onProfileSelected(profileId: String, namespace: Namespace)
}

@Composable
expect fun PlayerNameText(): String
