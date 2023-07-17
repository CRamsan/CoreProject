package com.cramsan.ps2link.network.ws.testgui.ui.dialogs

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.cramsan.ps2link.appfrontend.addoutfit.OutfitAddCompose
import com.cramsan.ps2link.appfrontend.addoutfit.OutfitAddEventHandler
import com.cramsan.ps2link.appfrontend.addoutfit.OutfitAddViewModelInterface
import com.cramsan.ps2link.appfrontend.addprofile.ProfileAddCompose
import com.cramsan.ps2link.appfrontend.addprofile.ProfileAddEventHandler
import com.cramsan.ps2link.appfrontend.addprofile.ProfileAddViewModelInterface
import com.cramsan.ps2link.core.models.Namespace
import com.cramsan.ps2link.network.ws.testgui.ui.theme.Dimensions
import com.cramsan.ps2link.ui.setAlpha
import com.cramsan.ps2link.ui.theme.Color
import org.koin.compose.koinInject

@Composable
fun PS2Dialog(
    isVisible: Boolean,
    onOutsideClicked: () -> Unit,
    content: @Composable () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }

    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn(),
        exit = fadeOut(),
    ) {
        Box(
            modifier = Modifier
                .background(MaterialTheme.colors.background.setAlpha(Color.transparent))
                .fillMaxSize()
                .clickable(
                    indication = null,
                    interactionSource = interactionSource
                ) { onOutsideClicked() },
            contentAlignment = Alignment.Center,
        ) {
            Box(
                modifier = Modifier
                    .width(Dimensions.maxColumnWidth)
                    .height(Dimensions.dialogHeight)
                    .clickable(
                        indication = null,
                        interactionSource = interactionSource
                    ) { },
                contentAlignment = Alignment.Center,
            ) {
                content()
            }
        }
    }
}

@Composable
fun AddProfileDialog(
    profileAddViewModel: ProfileAddViewModelInterface = koinInject(),
) {
    val viewModel = remember { profileAddViewModel }

    val searchQueryState = viewModel.searchQuery.collectAsState()
    val profileList = viewModel.profileList.collectAsState()
    val isLoading = viewModel.isLoading.collectAsState()
    val isError = viewModel.isError.collectAsState()
    ProfileAddCompose(
        searchField = searchQueryState.value,
        profileItems = profileList.value,
        isLoading = isLoading.value,
        isError = isError.value,
        eventHandler = object : ProfileAddEventHandler {
            override fun onSearchFieldUpdated(searchField: String) {
                viewModel.onSearchFieldUpdated(searchField)
            }

            override fun onProfileSelected(profileId: String, namespace: Namespace) {
                viewModel.onProfileSelected(profileId, namespace)
            }
        },
    )
}

@Composable
fun AddOutfitDialog(
    outfitAddViewModel: OutfitAddViewModelInterface = koinInject(),
) {
    val viewModel = remember { outfitAddViewModel }

    val tagSearchQueryState = viewModel.tagSearchQuery.collectAsState()
    val nameSearchQueryState = viewModel.nameSearchQuery.collectAsState()
    val profileList = viewModel.outfitList.collectAsState()
    val isLoading = viewModel.isLoading.collectAsState()
    val isError = viewModel.isError.collectAsState()
    OutfitAddCompose(
        tagSearchField = tagSearchQueryState.value,
        nameSearchField = nameSearchQueryState.value,
        outfitItems = profileList.value,
        isLoading = isLoading.value,
        isError = isError.value,
        eventHandler = object : OutfitAddEventHandler {
            override fun onTagFieldUpdated(searchField: String) {
                viewModel.onTagFieldUpdated(searchField)
            }

            override fun onNameFieldUpdated(searchField: String) {
                viewModel.onNameFieldUpdated(searchField)
            }

            override fun onOutfitSelected(outfitId: String, namespace: Namespace) {
                viewModel.onOutfitSelected(outfitId, namespace)
            }
        },
    )
}

@Composable
fun SearchForProfileTrackerDialog(
    profileAddViewModel: ProfileAddViewModelInterface = koinInject(),
    onProfileSelected: (characterId: String, namespace: Namespace) -> Unit,
) {
    val viewModel = remember { profileAddViewModel }

    val searchQueryState = viewModel.searchQuery.collectAsState()
    val profileList = viewModel.profileList.collectAsState()
    val isLoading = viewModel.isLoading.collectAsState()
    val isError = viewModel.isError.collectAsState()
    ProfileAddCompose(
        searchField = searchQueryState.value,
        profileItems = profileList.value,
        isLoading = isLoading.value,
        isError = isError.value,
        eventHandler = object : ProfileAddEventHandler {
            override fun onSearchFieldUpdated(searchField: String) {
                viewModel.onSearchFieldUpdated(searchField)
            }

            override fun onProfileSelected(profileId: String, namespace: Namespace) {
                onProfileSelected(profileId, namespace)
            }
        },
    )
}
