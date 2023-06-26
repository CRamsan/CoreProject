package com.cramsan.ps2link.network.ws.testgui.ui.screens.tracker

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import com.cramsan.ps2link.core.models.Namespace
import com.cramsan.ps2link.network.ws.testgui.ui.theme.Dimensions
import com.cramsan.ps2link.ui.FrameBottom
import com.cramsan.ps2link.ui.FrameSlim
import com.cramsan.ps2link.ui.SlimButton
import com.cramsan.ps2link.ui.theme.Padding

/**
 * Render [memberList] as a column of members with their online status.
 */
@OptIn(ExperimentalFoundationApi::class)
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
                        FrameSlim(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(Padding.xsmall)
                                .clickable {
                                    it.characterId?.let { id -> eventHandler.onProfileSelected(id, it.namespace) }
                                }.animateItemPlacement(),
                        ) {
                            Row(
                                modifier = Modifier.padding(Padding.medium)
                            ) {
                                Text(
                                    text = it.event,
                                )
                                Spacer(Modifier.weight(1f))
                                Text(
                                    text = it.description,
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

data class PlayerEventUIModel(
    val event: String,
    val description: String,
    val characterId: String?,
    val namespace: Namespace,
)

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
            PlayerEventUIModel("Achievement Earned", "", "", Namespace.UNDETERMINED),
            PlayerEventUIModel("Item Added", "", "", Namespace.UNDETERMINED),
            PlayerEventUIModel("Vehicle Destroyed", "", "", Namespace.UNDETERMINED),
            PlayerEventUIModel("Gained Experience", "100xp", "", Namespace.UNDETERMINED),
            PlayerEventUIModel("Item Added", "", "", Namespace.UNDETERMINED),
            PlayerEventUIModel("Killed", "Headshot!", "", Namespace.UNDETERMINED),
            PlayerEventUIModel("Vehicle Destroyed", "", "", Namespace.UNDETERMINED),
        ),
        eventHandler = object : TrackerEventHandler {
            override fun onProfileSelected(profileId: String, namespace: Namespace) = Unit

            override fun onActionSelected() = Unit
        }
    )
}
