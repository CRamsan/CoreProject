package com.cramsan.ps2link.appfrontend.outfitpager.outfit

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.cramsan.ps2link.appfrontend.UnknownText
import com.cramsan.ps2link.core.models.Character
import com.cramsan.ps2link.core.models.Faction
import com.cramsan.ps2link.core.models.Namespace
import com.cramsan.ps2link.core.models.Server
import com.cramsan.ps2link.ui.FrameBottom
import com.cramsan.ps2link.ui.FrameSlim
import com.cramsan.ps2link.ui.SlimButton
import com.cramsan.ps2link.ui.SwipeToRefreshColumn
import com.cramsan.ps2link.ui.theme.Padding
import com.cramsan.ps2link.ui.theme.Size
import com.cramsan.ps2link.ui.widgets.FactionIcon

@Composable
fun OutfitCompose(
    name: String? = null,
    tag: String? = null,
    leader: Character? = null,
    memberCount: Long = 0,
    creationTime: String? = null,
    faction: Faction?,
    isLoading: Boolean,
    eventHandler: OutfitEventHandler,
) {
    FrameBottom {
        SwipeToRefreshColumn(
            isLoading = isLoading,
            onRefreshRequested = { eventHandler.onRefreshRequested() },
        ) {
            // Top Faction icon
            FactionIcon(
                modifier = Modifier.size(Size.xxlarge),
                faction = faction ?: Faction.UNKNOWN,
            )

            val mediumModifier = Modifier
                .fillMaxWidth()
                .padding(Padding.medium)
            val smallModifier = Modifier
                .fillMaxWidth()
                .padding(Padding.small)

            val formattedTag = tag?.let {
                OutfitTagText(it)
            } ?: ""
            val displayName = name ?: UnknownText()

            // Outfit name
            FrameSlim(modifier = mediumModifier) {
                Row(modifier = smallModifier) {
                    Text(
                        text = "$formattedTag $displayName",
                        style = MaterialTheme.typography.h5,
                    )
                }
            }

            FrameSlim(modifier = mediumModifier) {
                Column(modifier = smallModifier) {
                    // Leader
                    FrameSlim(modifier = smallModifier) {
                        Column(modifier = smallModifier) {
                            Text(text = OutfitLeaderText())
                            SlimButton(
                                modifier = Modifier.fillMaxWidth(),
                                enabled = leader != null,
                                onClick = {
                                    leader?.let {
                                        eventHandler.onProfileSelected(it.characterId, it.namespace)
                                    }
                                },
                            ) {
                                Text(
                                    text = leader?.name ?: UnknownText()
                                )
                            }
                        }
                    }

                    // Member count
                    FrameSlim(modifier = smallModifier) {
                        Column(modifier = smallModifier) {
                            Text(text = OutfitMembersText())
                            Text(text = memberCount.toString())
                        }
                    }

                    // Creation date
                    FrameSlim(modifier = smallModifier) {
                        Column(modifier = smallModifier) {
                            Text(text = OutfitCreatedText())
                            Text(
                                text = creationTime ?: UnknownText(),
                            )
                        }
                    }
                }
            }
        }
    }
}

interface OutfitEventHandler {
    fun onProfileSelected(profileId: String, namespace: Namespace)
    fun onRefreshRequested()
}

@Composable
expect fun OutfitTagText(tag: String): String

@Composable
expect fun OutfitLeaderText(): String

@Composable
expect fun OutfitMembersText(): String

@Composable
expect fun OutfitCreatedText(): String

data class OutfitUIModel(
    val id: String,
    val name: String? = null,
    val tag: String? = null,
    val faction: Faction = Faction.UNKNOWN,
    val server: Server? = null,
    val timeCreated: String? = null,
    val leader: Character? = null,
    val memberCount: Int = 0,
    val namespace: Namespace,
    val cached: Boolean,
)
