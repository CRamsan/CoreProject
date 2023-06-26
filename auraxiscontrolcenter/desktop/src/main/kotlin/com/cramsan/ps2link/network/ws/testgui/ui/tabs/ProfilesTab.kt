package com.cramsan.ps2link.network.ws.testgui.ui.tabs

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.cramsan.ps2link.appfrontend.profilepager.friendlist.FriendListCompose
import com.cramsan.ps2link.appfrontend.profilepager.friendlist.FriendListEventHandler
import com.cramsan.ps2link.appfrontend.profilepager.friendlist.FriendListViewModelInterface
import com.cramsan.ps2link.appfrontend.profilepager.killlist.KillListCompose
import com.cramsan.ps2link.appfrontend.profilepager.killlist.KillListEventHandler
import com.cramsan.ps2link.appfrontend.profilepager.killlist.KillListViewModelInterface
import com.cramsan.ps2link.appfrontend.profilepager.profile.ProfileCompose
import com.cramsan.ps2link.appfrontend.profilepager.profile.ProfileEventHandler
import com.cramsan.ps2link.appfrontend.profilepager.profile.ProfileViewModelInterface
import com.cramsan.ps2link.appfrontend.profilepager.statlist.StatListCompose
import com.cramsan.ps2link.appfrontend.profilepager.statlist.StatListEventHandler
import com.cramsan.ps2link.appfrontend.profilepager.statlist.StatListViewModelInterface
import com.cramsan.ps2link.appfrontend.profilepager.weaponlist.WeaponListCompose
import com.cramsan.ps2link.appfrontend.profilepager.weaponlist.WeaponListEventHandler
import com.cramsan.ps2link.appfrontend.profilepager.weaponlist.WeaponListViewModelInterface
import com.cramsan.ps2link.core.models.Faction
import com.cramsan.ps2link.core.models.Namespace
import com.cramsan.ps2link.network.ws.testgui.application.ApplicationManager
import com.cramsan.ps2link.network.ws.testgui.ui.dialogs.AddProfileDialog
import com.cramsan.ps2link.network.ws.testgui.ui.dialogs.PS2Dialog
import com.cramsan.ps2link.network.ws.testgui.ui.dialogs.PS2DialogType
import com.cramsan.ps2link.ui.FrameBottom
import com.cramsan.ps2link.ui.SlimButton
import com.cramsan.ps2link.ui.theme.Padding
import com.cramsan.ps2link.ui.theme.Size
import kotlinx.collections.immutable.persistentListOf
import org.koin.compose.koinInject

@Composable
fun ProfilesTab(
    applicationManager: ApplicationManager = koinInject(),
    eventHandler: ProfilesTabEventHandler = koinInject(),
) {
    val uiModel by applicationManager.uiModel.collectAsState()

    if (uiModel.windowUIModel.showFTE) {
        SlimButton(
            onClick = { eventHandler.onOpenSearchProfileDialogSelected() },
            modifier = Modifier
                .height(Size.xlarge)
        ) { Text("Search For Character") }
    } else {
        Row(
            modifier = Modifier.fillMaxSize(),
        ) {
            Box(modifier = Modifier.weight(1f)) {
                ProfileTab()
            }
            Box(modifier = Modifier.weight(1f)) {
                FriendsTab()
            }
            Box(modifier = Modifier.weight(1f)) {
                StatsListTab()
            }
            Box(modifier = Modifier.weight(1f)) {
                KillListTab()
            }
            Box(modifier = Modifier.weight(1f)) {
                WeaponListTab()
            }
        }
    }

    val showDialog = uiModel.windowUIModel.dialogUIModel?.dialogType == PS2DialogType.ADD_PROFILE
    PS2Dialog(
        isVisible = showDialog,
        onOutsideClicked = { applicationManager.onDialogOutsideSelected() },
    ) {
        AddProfileDialog()
    }
}

@Composable
private fun ProfileTab(
    profileViewModel: ProfileViewModelInterface = koinInject(),
) {
    val viewModel = remember { profileViewModel }

    val profile = viewModel.profile.collectAsState(null)
    val prestigeIcon = viewModel.prestigeIcon.collectAsState()
    val isLoading = viewModel.isLoading.collectAsState()
    val isError = viewModel.isError.collectAsState()
    Column {
        ProfileCompose(
            modifier = Modifier.weight(1f),
            faction = profile.value?.faction,
            br = profile.value?.battleRank?.toInt(),
            prestige = profile.value?.prestige?.toInt(),
            percentToNextBR = profile.value?.percentageToNextBattleRank?.toFloat(),
            certs = profile.value?.certs?.toInt(),
            percentToNextCert = profile.value?.percentageToNextCert?.toFloat(),
            loginStatus = profile.value?.loginStatus,
            lastLogin = profile.value?.lastLogin,
            outfit = profile.value?.outfit,
            server = profile.value?.server?.serverName,
            timePlayed = profile.value?.timePlayed,
            creationTime = profile.value?.creationTime,
            sessionCount = profile.value?.sessionCount,
            prestigeIcon = prestigeIcon.value,
            isLoading = isLoading.value,
            isError = isError.value,
            eventHandler = object : ProfileEventHandler {
                override fun onOutfitSelected(outfitId: String, namespace: Namespace) {
                    viewModel.onOutfitSelected(outfitId, namespace)
                }

                override fun onRefreshRequested() {
                    viewModel.onRefreshRequested()
                }
            },
        )
        FrameBottom(
            modifier = Modifier
                .fillMaxWidth(),
        ) {
            Column(
                modifier = Modifier.padding(Padding.medium),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                SlimButton(
                    onClick = { viewModel.onOpenLiveTrackerSelected() },
                    modifier = Modifier
                        .height(Size.xlarge)
                ) { Text("Open In Live Tracker") }
            }
        }
    }
}

@Composable
private fun FriendsTab(
    friendsViewModel: FriendListViewModelInterface = koinInject(),
) {
    val viewModel = remember { friendsViewModel }

    val friendList = viewModel.friendList.collectAsState()
    val isLoading = viewModel.isLoading.collectAsState()
    val isError = viewModel.isError.collectAsState()
    FriendListCompose(
        friendList = friendList.value,
        isLoading = isLoading.value,
        isError = isError.value,
        eventHandler = object : FriendListEventHandler {
            override fun onProfileSelected(profileId: String, namespace: Namespace) {
                viewModel.onProfileSelected(profileId, namespace)
            }

            override fun onRefreshRequested() {
                viewModel.onRefreshRequested()
            }
        },
    )
}

@Composable
private fun StatsListTab(
    statsListViewModel: StatListViewModelInterface = koinInject(),
) {
    val viewModel = remember { statsListViewModel }

    val statList = viewModel.statList.collectAsState()
    val isLoading = viewModel.isLoading.collectAsState()
    val isError = viewModel.isError.collectAsState()
    StatListCompose(
        statList = statList.value,
        isLoading = isLoading.value,
        isError = isError.value,
        useVerticalMode = true,
        eventHandler = object : StatListEventHandler {
            override fun onProfileSelected(profileId: String, namespace: Namespace) {
                statsListViewModel.onProfileSelected(profileId, namespace)
            }

            override fun onRefreshRequested() {
                statsListViewModel.onRefreshRequested()
            }
        },
    )
}

@Composable
private fun KillListTab(
    killListViewModel: KillListViewModelInterface = koinInject(),
) {
    val viewModel = remember { killListViewModel }

    val killList = viewModel.killList.collectAsState(persistentListOf())
    val isLoading = viewModel.isLoading.collectAsState()
    val isError = viewModel.isError.collectAsState()
    KillListCompose(
        useVerticalMode = true,
        killList = killList.value,
        isLoading = isLoading.value,
        isError = isError.value,
        eventHandler = object : KillListEventHandler {
            override fun onProfileSelected(profileId: String, namespace: Namespace) {
                viewModel.onProfileSelected(profileId, namespace)
            }

            override fun onRefreshRequested() {
                onRefreshRequested()
            }
        },
    )
}

@Composable
private fun WeaponListTab(
    weaponListViewModel: WeaponListViewModelInterface = koinInject(),
) {
    val viewModel = remember { weaponListViewModel }

    val weaponList = viewModel.weaponList.collectAsState()
    val faction = viewModel.faction.collectAsState(Faction.UNKNOWN)
    val isLoading = viewModel.isLoading.collectAsState()
    val isError = viewModel.isError.collectAsState()
    WeaponListCompose(
        faction = faction.value,
        weaponList = weaponList.value,
        isError = isError.value,
        isLoading = isLoading.value,
        useVerticalMode = true,
        eventHandler = object : WeaponListEventHandler {
            override fun onRefreshRequested() {
                viewModel.onRefreshRequested()
            }
        },
    )
}

interface ProfilesTabEventHandler {
    fun onOpenSearchProfileDialogSelected()
}
