package com.cramsan.ps2link.network.ws.testgui.ui.tabs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.cramsan.ps2link.core.models.Namespace
import com.cramsan.ps2link.network.ws.testgui.application.ApplicationManager
import com.cramsan.ps2link.network.ws.testgui.ui.screens.tracker.TrackerCompose
import com.cramsan.ps2link.network.ws.testgui.ui.screens.tracker.TrackerEventHandler
import com.cramsan.ps2link.network.ws.testgui.ui.screens.tracker.TrackerViewModelInterface
import com.cramsan.ps2link.ui.SlimButton
import com.cramsan.ps2link.ui.theme.Size
import org.koin.compose.koinInject

@Composable
fun TrackerTab(
    applicationManager: ApplicationManager = koinInject(),
    eventHandler: TrackerTabEventHandler = koinInject(),
    trackerViewModel: TrackerViewModelInterface = koinInject(),
) {
    val uiModel by applicationManager.uiModel.collectAsState()

    if (uiModel.windowUIModel.showFTE) {
        Column {
            SlimButton(
                onClick = { eventHandler.onNavigateToProfileTabSelected() },
                modifier = Modifier
                    .height(Size.xlarge)
            ) { Text("Search For Profile") }
        }
    } else {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.Center,
        ) {
            TrackerTab(trackerViewModel)
        }
    }
}

@Composable
private fun TrackerTab(
    trackerViewModel: TrackerViewModelInterface,
) {
    val viewModel = remember { trackerViewModel }

    val uiModel by viewModel.uiModel.collectAsState(null)
    val isLoading by viewModel.isLoading.collectAsState(false)

    TrackerCompose(
        actionLabel = uiModel?.actionLabel,
        isLoading = isLoading,
        profileName = uiModel?.profileName,
        events = uiModel?.events ?: emptyList(),
        eventHandler = object : TrackerEventHandler {
            override fun onProfileSelected(profileId: String, namespace: Namespace) {
                trackerViewModel.onProfileSelected(profileId, namespace)
            }

            override fun onActionSelected() {
                trackerViewModel.onPrimaryActionSelected()
            }
        }
    )
}

interface TrackerTabEventHandler {
    fun onNavigateToProfileTabSelected()
    fun onOpenSearchOutfitDialogSelected()
}
