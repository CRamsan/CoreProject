package com.cesarandres.ps2link.fragments.outfitpager.outfit

import androidx.annotation.MainThread
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.cesarandres.ps2link.R
import com.cramsan.ps2link.core.models.Character
import com.cramsan.ps2link.core.models.Faction
import com.cramsan.ps2link.core.models.Namespace
import com.cramsan.ps2link.ui.FrameBottom
import com.cramsan.ps2link.ui.FrameSlim
import com.cramsan.ps2link.ui.SlimButton
import com.cramsan.ps2link.ui.SwipeToRefreshColumn
import com.cramsan.ps2link.ui.theme.Padding
import com.cramsan.ps2link.ui.theme.Size
import com.cramsan.ps2link.ui.widgets.FactionIcon
import kotlinx.datetime.Instant
import java.text.SimpleDateFormat
import java.util.Date

@Composable
fun OutfitCompose(
    name: String? = null,
    tag: String? = null,
    leader: Character? = null,
    memberCount: Long = 0,
    creationTime: Instant? = null,
    faction: Faction?,
    isLoading: Boolean,
    eventHandler: OutfitEventHandler,
) {
    FrameBottom {
        SwipeToRefreshColumn(
            isLoading = isLoading,
            onRefreshRequested = { eventHandler.onRefreshRequested() },
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
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
                    stringResource(com.cramsan.ps2link.ui.R.string.text_outfit_tag, it)
                } ?: ""
                val displayName = name ?: stringResource(
                    id = com.cramsan.ps2link.ui.R.string.text_unknown
                )

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
                                Text(
                                    text = stringResource(
                                        com.cramsan.ps2link.ui.R.string.text_leader
                                    )
                                )
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
                                        text = leader?.name ?: stringResource(
                                            com.cramsan.ps2link.ui.R.string.text_unknown
                                        )
                                    )
                                }
                            }
                        }

                        // Member count
                        FrameSlim(modifier = smallModifier) {
                            Column(modifier = smallModifier) {
                                Text(text = stringResource(com.cramsan.ps2link.ui.R.string.text_members))
                                Text(text = memberCount.toString())
                            }
                        }

                        // Creation date
                        FrameSlim(modifier = smallModifier) {
                            Column(modifier = smallModifier) {
                                Text(text = stringResource(com.cramsan.ps2link.ui.R.string.text_created))
                                Text(
                                    text = creationTime?.let {
                                        formatter.format(Date(it.toEpochMilliseconds()))
                                    } ?: stringResource(com.cramsan.ps2link.ui.R.string.text_unknown),
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

var formatter = SimpleDateFormat("MMMM dd, yyyy")

@MainThread
interface OutfitEventHandler {
    fun onProfileSelected(profileId: String, namespace: Namespace)
    fun onRefreshRequested()
}

@Preview
@Composable
fun Preview() {
    OutfitCompose(
        faction = Faction.VS,
        isLoading = true,
        eventHandler = object : OutfitEventHandler {
            override fun onProfileSelected(profileId: String, namespace: Namespace) = Unit
            override fun onRefreshRequested() = Unit
        },
    )
}
