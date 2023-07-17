package com.cramsan.ps2link.network.ws.testgui.di

import com.cramsan.ps2link.appcore.preferences.PS2Settings
import com.cramsan.ps2link.appcore.preferences.PS2SettingsImpl
import com.cramsan.ps2link.appcore.repository.PS2LinkRepository
import com.cramsan.ps2link.appcore.repository.PS2LinkRepositoryImpl
import com.cramsan.ps2link.appfrontend.LanguageProvider
import com.cramsan.ps2link.core.models.Namespace
import com.cramsan.ps2link.network.ws.testgui.application.ApplicationManager
import com.cramsan.ps2link.network.ws.testgui.application.GameSessionManager
import com.cramsan.ps2link.network.ws.testgui.hoykeys.HotKeyManager
import com.cramsan.ps2link.network.ws.testgui.ui.tabs.OutfitsTabEventHandler
import com.cramsan.ps2link.network.ws.testgui.ui.tabs.ProfilesTabEventHandler
import com.cramsan.ps2link.network.ws.testgui.ui.tabs.TrackerTabEventHandler
import org.koin.dsl.module
import org.ocpsoft.prettytime.PrettyTime

/**
 * Initialized instances to be used at the application layer. The classes here pertain to the bejaviour of this
 * application.
 */
val ApplicationModule = module {

    single<HotKeyManager> {
        HotKeyManager(
            json = get(),
            preferences = get(),
        )
    }

    single<GameSessionManager> {
        GameSessionManager(get(), get())
    }

    single<ApplicationManager> {
        ApplicationManager(
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
        )
    }

    single<PrettyTime> {
        PrettyTime()
    }

    single<PS2LinkRepository> {
        PS2LinkRepositoryImpl(
            get(),
            get(),
            get(),
            get(),
        )
    }

    single<PS2Settings> {
        PS2SettingsImpl(get())
    }

    single<LanguageProvider> {
        LanguageProvider()
    }

    single<ProfilesTabEventHandler> {
        val applicationManager: ApplicationManager = get()
        object : ProfilesTabEventHandler {
            override fun onOpenSearchProfileDialogSelected() {
                applicationManager.openSearch()
            }
        }
    }

    single<OutfitsTabEventHandler> {
        val applicationManager: ApplicationManager = get()
        object : OutfitsTabEventHandler {
            override fun onOpenSearchOutfitDialogSelected() {
                applicationManager.openSearch()
            }
        }
    }

    single<TrackerTabEventHandler> {
        val applicationManager: ApplicationManager = get()
        object : TrackerTabEventHandler {
            override fun onOpenSearchProfileDialogSelected() {
                applicationManager.openSearch()
            }
        }
    }

    single {
        listOf(Namespace.PS2PC)
    }
}
