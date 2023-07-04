package com.cramsan.ps2link.network.ws.testgui.ui.screens.tracker

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.cramsan.ps2link.appfrontend.UnknownText
import com.cramsan.ps2link.core.models.CharacterClass
import com.cramsan.ps2link.core.models.Faction
import com.cramsan.ps2link.core.models.KillType
import com.cramsan.ps2link.core.models.Namespace
import com.cramsan.ps2link.network.ws.testgui.ui.theme.Dimensions
import com.cramsan.ps2link.ui.FrameBottom
import com.cramsan.ps2link.ui.FrameSlim
import com.cramsan.ps2link.ui.SlimButton
import com.cramsan.ps2link.ui.items.KillItem
import com.cramsan.ps2link.ui.theme.Padding

/**
 * Render [memberList] as a column of members with their online status.
 */
@Composable
fun TrackerCompose(
    actionLabel: String?,
    isLoading: Boolean,
    profileName: String?,
    events: List<PlayerEventUIModel>,
    eventHandler: TrackerEventHandler,
) {
    FrameBottom(
        modifier = Modifier
            .fillMaxHeight()
            .width(Dimensions.maxColumnWidth),
    ) {
        Column(
            modifier = Modifier.padding(Padding.medium),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                profileName?.let {
                    Text(it)
                }

                SlimButton(
                    enabled = !isLoading,
                    onClick = { eventHandler.onActionSelected() }
                ) {
                    Text(actionLabel ?: "")
                }
            }

            FrameSlim(
                modifier = Modifier.padding(Padding.small)
                    .fillMaxHeight()
                    .fillMaxWidth()
            ) {
                LazyColumn {
                    items(events) {
                        when (it) {
                            is PlayerKillUIModel -> {
                                val time = it.time ?: UnknownText()
                                KillItem(
                                    modifier = Modifier.fillMaxWidth(),
                                    useVerticalMode = false,
                                    killType = it.killType,
                                    faction = it.faction,
                                    attacker = it.playerName ?: UnknownText(),
                                    time = time,
                                    weaponName = it.weaponName ?: UnknownText(),
                                    weaponImage = it.weaponImage,
                                    onClick = {
                                        it.characterId?.let { characterId ->
                                            eventHandler.onProfileSelected(characterId, it.namespace)
                                        }
                                    },
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

/**
 *
 */
interface TrackerEventHandler {
    /**
     *
     */
    fun onProfileSelected(profileId: String, namespace: Namespace)

    fun onActionSelected()
}

@Preview
@Composable
fun PreviewTracker() {
    TrackerCompose(
        actionLabel = "Pause",
        isLoading = false,
        profileName = "cramsan",
        events = listOf(
            PlayerKillUIModel(
                killType = KillType.KILL,
                profile = CharacterClass.ENGINEER,
                faction = Faction.NC,
                playerName = "Cramsan",
                playerRank = 75,
                characterId = "",
                namespace = Namespace.PS2PC,
                time = "2023-02-31 12:11:01AM",
                weaponImage = "",
                weaponName = "Pulsar C",
            ),
            PlayerKillUIModel(
                killType = KillType.KILLEDBY,
                profile = CharacterClass.HEAVY_ASSAULT,
                faction = Faction.VS,
                playerName = "Test",
                playerRank = 11,
                characterId = "",
                namespace = Namespace.PS2PC,
                time = "2023-02-31 12:11:01AM",
                weaponImage = "",
                weaponName = "Pulsar C",
            ),
            PlayerKillUIModel(
                killType = KillType.SUICIDE,
                profile = CharacterClass.MAX,
                faction = Faction.TR,
                playerName = "Test2",
                playerRank = 99,
                characterId = "",
                namespace = Namespace.PS2PC,
                time = "2023-02-31 12:11:01AM",
                weaponImage = "",
                weaponName = "Pulsar C",
            ),
        ),
        eventHandler = object : TrackerEventHandler {
            override fun onProfileSelected(profileId: String, namespace: Namespace) = Unit

            override fun onActionSelected() = Unit
        }
    )
}
