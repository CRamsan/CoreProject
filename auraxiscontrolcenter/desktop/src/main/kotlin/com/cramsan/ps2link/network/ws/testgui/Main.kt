@file:OptIn(DelicateCoroutinesApi::class)

package com.cramsan.ps2link.network.ws.testgui

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.window.application
import com.cramsan.framework.assertlib.AssertUtilInterface
import com.cramsan.framework.core.BaseViewModel
import com.cramsan.framework.thread.ThreadUtilInterface
import com.cramsan.ps2link.appfrontend.BasePS2Event
import com.cramsan.ps2link.appfrontend.addoutfit.OutfitAddViewModelInterface
import com.cramsan.ps2link.appfrontend.addprofile.ProfileAddViewModelInterface
import com.cramsan.ps2link.appfrontend.outfitpager.members.MembersViewModelInterface
import com.cramsan.ps2link.appfrontend.outfitpager.online.OnlineMembersViewModelInterface
import com.cramsan.ps2link.appfrontend.outfitpager.outfit.OutfitViewModelInterface
import com.cramsan.ps2link.appfrontend.profilepager.friendlist.FriendListViewModelInterface
import com.cramsan.ps2link.appfrontend.profilepager.killlist.KillListViewModelInterface
import com.cramsan.ps2link.appfrontend.profilepager.profile.ProfileViewModelInterface
import com.cramsan.ps2link.appfrontend.profilepager.statlist.StatListViewModelInterface
import com.cramsan.ps2link.appfrontend.profilepager.weaponlist.WeaponListViewModelInterface
import com.cramsan.ps2link.core.models.Namespace
import com.cramsan.ps2link.network.ws.messages.ServerEventPayload
import com.cramsan.ps2link.network.ws.testgui.application.ApplicationManager
import com.cramsan.ps2link.network.ws.testgui.application.ApplicationManagerCallback
import com.cramsan.ps2link.network.ws.testgui.application.ProgramMode
import com.cramsan.ps2link.network.ws.testgui.di.ApplicationModule
import com.cramsan.ps2link.network.ws.testgui.di.DomainModule
import com.cramsan.ps2link.network.ws.testgui.di.FrameworkModule
import com.cramsan.ps2link.network.ws.testgui.di.ViewModelModule
import com.cramsan.ps2link.network.ws.testgui.filelogger.FileLog
import com.cramsan.ps2link.network.ws.testgui.hoykeys.HotKeyManager
import com.cramsan.ps2link.network.ws.testgui.ui.ApplicationGUI
import com.cramsan.ps2link.network.ws.testgui.ui.screens.settings.SettingsScreenViewModelInterface
import com.cramsan.ps2link.network.ws.testgui.ui.screens.tracker.TrackerViewModelInterface
import com.cramsan.ps2link.network.ws.testgui.ui.tabs.ApplicationTab
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.context.startKoin
import java.awt.Desktop
import java.net.URI

/**
 * Application entry point.
 */
fun main() = runBlocking {

    startKoin {
        modules(
            FrameworkModule,
            DomainModule,
            ApplicationModule,
            ViewModelModule,
        )
    }

    val component = object : KoinComponent {
        val assertUtil: AssertUtilInterface by inject()
        val threadUtil: ThreadUtilInterface by inject()
        val hotKeyManager: HotKeyManager by inject()
        val applicationManager: ApplicationManager by inject()
    }

    component.assertUtil
    component.threadUtil
    component.hotKeyManager.configureHotKeyManger()

    /**
     * Initialize the GUI
     */
    initializeGUI(
        component.applicationManager,
    )
}

/**
 * Initialize the GUI component of the application.
 */
suspend fun initializeGUI(applicationManager: ApplicationManager) {
    val components = object : KoinComponent {
        val profileViewModel: ProfileViewModelInterface by inject()
        val friendsViewModel: FriendListViewModelInterface by inject()
        val statsListViewModel: StatListViewModelInterface by inject()
        val killListViewModel: KillListViewModelInterface by inject()
        val weaponListViewModel: WeaponListViewModelInterface by inject()
        val profileAddViewModel: ProfileAddViewModelInterface by inject()
        val outfitViewModel: OutfitViewModelInterface by inject()
        val onlineMembersViewModel: OnlineMembersViewModelInterface by inject()
        val membersViewModel: MembersViewModelInterface by inject()
        val outfitAddViewModel: OutfitAddViewModelInterface by inject()
        val settingsScreenViewModel: SettingsScreenViewModelInterface by inject()
        val trackerViewModel: TrackerViewModelInterface by inject()
        val scope: CoroutineScope by inject()
    }

    components.profileViewModel.configureEvents(applicationManager)
    components.friendsViewModel.configureEvents(applicationManager)
    components.statsListViewModel.configureEvents(applicationManager)
    components.killListViewModel.configureEvents(applicationManager)
    components.weaponListViewModel.configureEvents(applicationManager)
    components.profileAddViewModel.configureEvents(applicationManager)
    components.outfitViewModel.configureEvents(applicationManager)
    components.onlineMembersViewModel.configureEvents(applicationManager)
    components.membersViewModel.configureEvents(applicationManager)
    components.outfitAddViewModel.configureEvents(applicationManager)
    components.trackerViewModel.configureEvents(applicationManager)
    components.settingsScreenViewModel.onStart()

    components.settingsScreenViewModel.onApplicationUIModelUpdated(applicationManager.uiModel.value)

    components.scope.launch {
        applicationManager.uiModel.collect {
            components.settingsScreenViewModel.onApplicationUIModelUpdated(it)
        }
    }

    applicationManager.registerCallback(object : ApplicationManagerCallback {
        override fun onServerEventPayload(payload: ServerEventPayload) = Unit
        override fun onProgramModeChanged(programMode: ProgramMode) = Unit
        override fun onCharacterSelected(characterId: String, namespace: Namespace) {
            components.profileViewModel.setUp(characterId, namespace)
            components.friendsViewModel.setUp(characterId, namespace)
            components.statsListViewModel.setUp(characterId, namespace)
            components.killListViewModel.setUp(characterId, namespace)
            components.weaponListViewModel.setUp(characterId, namespace)
        }
        override fun onOutfitSelected(outfitId: String, namespace: Namespace) {
            components.outfitViewModel.setUp(outfitId, namespace)
            components.onlineMembersViewModel.setUp(outfitId, namespace)
            components.membersViewModel.setUp(outfitId, namespace)
        }
        override fun onTrackedCharacterSelected(characterId: String, namespace: Namespace) = Unit
        override fun onFileLogActive(fileLog: FileLog) = Unit
    })

    applicationManager.startApplication()

    application {
        val uiModel by applicationManager.uiModel.collectAsState()

        ApplicationGUI(
            applicationManager,
            uiModel,
        )
    }
}

private suspend fun BaseViewModel.configureEvents(
    applicationManager: ApplicationManager,
) {
    GlobalScope.launch {
        events.collect {
            when (it) {
                is BasePS2Event.OpenProfile -> {
                    applicationManager.onProfilesSelected(it.characterId, it.namespace)
                    applicationManager.onTabSelected(
                        ApplicationTab.Profile(
                            it.characterId,
                            it.namespace
                        )
                    )
                }
                is BasePS2Event.OpenOutfit -> {
                    applicationManager.onOutfitSelected(it.outfitId, it.namespace)
                    applicationManager.onTabSelected(
                        ApplicationTab.Outfit(
                            it.outfitId,
                            it.namespace
                        )
                    )
                }
                BasePS2Event.OpenOutfitList -> Unit
                BasePS2Event.OpenProfileList -> Unit
                BasePS2Event.OpenAbout -> Unit
                BasePS2Event.OpenReddit -> Unit
                BasePS2Event.OpenServerList -> Unit
                BasePS2Event.OpenTwitter -> Unit
                is BasePS2Event.OpenUrl -> {
                    Desktop.getDesktop().browse(URI(it.url))
                }
                BasePS2Event.OpenSettings -> {
                    applicationManager.onTabSelected(ApplicationTab.Settings)
                }
                is BasePS2Event.OpenProfileLiveTracker -> {
                    applicationManager.onTabSelected(
                        ApplicationTab.Tracker(
                            it.characterId,
                            it.namespace,
                            null,
                            null,
                        )
                    )
                }
                is BasePS2Event.OpenOutfitLiveTracker -> {
                    applicationManager.onTabSelected(
                        ApplicationTab.Tracker(
                            null,
                            null,
                            it.outfitId,
                            it.namespace,
                        )
                    )
                }
            }
        }
    }
}
