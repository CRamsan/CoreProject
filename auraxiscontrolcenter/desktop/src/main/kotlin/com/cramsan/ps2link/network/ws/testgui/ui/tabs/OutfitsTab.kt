package com.cramsan.ps2link.network.ws.testgui.ui.tabs

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.cramsan.ps2link.appfrontend.outfitpager.members.MemberListCompose
import com.cramsan.ps2link.appfrontend.outfitpager.members.MemberListEventHandler
import com.cramsan.ps2link.appfrontend.outfitpager.members.MembersViewModelInterface
import com.cramsan.ps2link.appfrontend.outfitpager.online.OnlineMemberEventHandler
import com.cramsan.ps2link.appfrontend.outfitpager.online.OnlineMembersCompose
import com.cramsan.ps2link.appfrontend.outfitpager.online.OnlineMembersViewModelInterface
import com.cramsan.ps2link.appfrontend.outfitpager.outfit.OutfitCompose
import com.cramsan.ps2link.appfrontend.outfitpager.outfit.OutfitEventHandler
import com.cramsan.ps2link.appfrontend.outfitpager.outfit.OutfitViewModelInterface
import com.cramsan.ps2link.core.models.Namespace
import com.cramsan.ps2link.network.ws.testgui.application.ApplicationManager
import com.cramsan.ps2link.network.ws.testgui.ui.dialogs.AddOutfitDialog
import com.cramsan.ps2link.network.ws.testgui.ui.dialogs.PS2Dialog
import com.cramsan.ps2link.network.ws.testgui.ui.dialogs.PS2DialogType
import com.cramsan.ps2link.ui.SlimButton
import com.cramsan.ps2link.ui.theme.Size
import org.koin.compose.koinInject

@Composable
fun OutfitsTab(
    applicationManager: ApplicationManager = koinInject(),
    eventHandler: OutfitsTabEventHandler = koinInject(),
    outfitViewModel: OutfitViewModelInterface = koinInject(),
    onlineMembersViewModel: OnlineMembersViewModelInterface = koinInject(),
    membersViewModel: MembersViewModelInterface = koinInject(),
) {
    val uiModel by applicationManager.uiModel.collectAsState()

    if (uiModel.windowUIModel.showFTE) {
        SlimButton(
            onClick = { eventHandler.onOpenSearchOutfitDialogSelected() },
            modifier = Modifier
                .height(Size.xlarge)
        ) { Text("Search For Outfit") }
    } else {
        Row(
            modifier = Modifier.fillMaxSize(),
        ) {
            Box(modifier = Modifier.weight(1f)) {
                OutfitTab(outfitViewModel)
            }
            Box(modifier = Modifier.weight(1f)) {
                OnlineMembersTab(onlineMembersViewModel)
            }
            Box(modifier = Modifier.weight(1f)) {
                MembersTab(membersViewModel)
            }
        }
    }

    val showDialog = uiModel.windowUIModel.dialogUIModel?.dialogType == PS2DialogType.ADD_OUTFIT
    PS2Dialog(
        isVisible = showDialog,
        onOutsideClicked = { applicationManager.onDialogOutsideSelected() },
    ) {
        AddOutfitDialog()
    }
}

@Composable
private fun OutfitTab(
    outfitViewModel: OutfitViewModelInterface,
) {
    val viewModel = remember { outfitViewModel }

    val outfit = viewModel.outfit.collectAsState(null)
    val leader = outfit.value?.leader
    val isLoading = viewModel.isLoading.collectAsState()
    OutfitCompose(
        name = outfit.value?.name,
        leader = leader,
        faction = outfit.value?.faction,
        memberCount = outfit.value?.memberCount?.toLong() ?: 0,
        creationTime = outfit.value?.timeCreated,
        isLoading = isLoading.value,
        eventHandler = object : OutfitEventHandler {
            override fun onProfileSelected(profileId: String, namespace: Namespace) {
                outfitViewModel.onProfileSelected(profileId, namespace)
            }
            override fun onRefreshRequested() {
                outfitViewModel.onRefreshRequested()
            }
        },
    )
}

@Composable
private fun OnlineMembersTab(
    onlineMembersViewModel: OnlineMembersViewModelInterface,
) {
    val viewModel = remember { onlineMembersViewModel }

    val memberList = viewModel.memberList.collectAsState()
    val isLoading = viewModel.isLoading.collectAsState()
    val isError = viewModel.isError.collectAsState()
    OnlineMembersCompose(
        memberList = memberList.value,
        isLoading = isLoading.value,
        isError = isError.value,
        eventHandler = object : OnlineMemberEventHandler {
            override fun onProfileSelected(profileId: String, namespace: Namespace) {
                onlineMembersViewModel.onProfileSelected(profileId, namespace)
            }
            override fun onRefreshRequested() {
                onlineMembersViewModel.onRefreshRequested()
            }
        },
    )
}

@Composable
private fun MembersTab(
    membersViewModel: MembersViewModelInterface,
) {
    val viewModel = remember { membersViewModel }

    val memberList = viewModel.memberList.collectAsState()
    val isLoading = viewModel.isLoading.collectAsState()
    val isError = viewModel.isError.collectAsState()
    MemberListCompose(
        memberList = memberList.value,
        isLoading = isLoading.value,
        isError = isError.value,
        eventHandler = object : MemberListEventHandler {
            override fun onProfileSelected(profileId: String, namespace: Namespace) {
                membersViewModel.onProfileSelected(profileId, namespace)
            }
            override fun onRefreshRequested() {
                membersViewModel.onRefreshRequested()
            }
        },
    )
}
interface OutfitsTabEventHandler {
    fun onOpenSearchOutfitDialogSelected()
}
