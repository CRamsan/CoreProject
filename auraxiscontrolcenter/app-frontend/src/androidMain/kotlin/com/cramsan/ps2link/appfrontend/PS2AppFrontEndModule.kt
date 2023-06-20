package com.cramsan.ps2link.appfrontend

import com.cramsan.framework.core.DispatcherProvider
import com.cramsan.framework.core.StringProvider
import com.cramsan.ps2link.appcore.preferences.PS2Settings
import com.cramsan.ps2link.appcore.repository.PS2LinkRepository
import com.cramsan.ps2link.appcore.repository.RedditRepository
import com.cramsan.ps2link.appcore.repository.TwitterRepository
import com.cramsan.ps2link.appfrontend.about.AboutViewModel
import com.cramsan.ps2link.appfrontend.addoutfit.OutfitAddViewModel
import com.cramsan.ps2link.appfrontend.addprofile.ProfileAddViewModel
import com.cramsan.ps2link.appfrontend.mainmenu.MainMenuViewModel
import com.cramsan.ps2link.appfrontend.outfitlist.OutfitListViewModel
import com.cramsan.ps2link.appfrontend.outfitpager.members.MembersViewModel
import com.cramsan.ps2link.appfrontend.outfitpager.online.OnlineMembersViewModel
import com.cramsan.ps2link.appfrontend.outfitpager.outfit.OutfitViewModel
import com.cramsan.ps2link.appfrontend.profilelist.ProfileListViewModel
import com.cramsan.ps2link.appfrontend.profilepager.friendlist.FriendListViewModel
import com.cramsan.ps2link.appfrontend.profilepager.killlist.KillListViewModel
import com.cramsan.ps2link.appfrontend.profilepager.profile.ProfileViewModel
import com.cramsan.ps2link.appfrontend.profilepager.statlist.StatListViewModel
import com.cramsan.ps2link.appfrontend.profilepager.weaponlist.WeaponListViewModel
import com.cramsan.ps2link.appfrontend.redditpager.RedditViewModel
import com.cramsan.ps2link.appfrontend.serverlist.ServerListViewModel
import com.cramsan.ps2link.appfrontend.twitter.TwitterListViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/**
 *
 */
@Module
@InstallIn(SingletonComponent::class)
object PS2AppFrontEndModule {

    @Provides
    fun providesAboutViewModel(
        pS2LinkRepository: PS2LinkRepository,
        pS2Settings: PS2Settings,
        dispatcherProvider: DispatcherProvider,
        languageProvider: LanguageProvider,
        stringProvider: StringProvider,
    ): AboutViewModel = AboutViewModel(
        pS2LinkRepository,
        pS2Settings,
        dispatcherProvider,
        languageProvider,
        stringProvider,
    )

    @Provides
    fun providesOutfitAddViewModel(
        pS2LinkRepository: PS2LinkRepository,
        pS2Settings: PS2Settings,
        languageProvider: LanguageProvider,
        dispatcherProvider: DispatcherProvider,
    ): OutfitAddViewModel = OutfitAddViewModel(
        pS2LinkRepository,
        pS2Settings,
        languageProvider,
        dispatcherProvider,
    )

    @Provides
    fun providesProfileAddViewModel(
        pS2LinkRepository: PS2LinkRepository,
        pS2Settings: PS2Settings,
        languageProvider: LanguageProvider,
        dispatcherProvider: DispatcherProvider,
    ): ProfileAddViewModel = ProfileAddViewModel(
        pS2LinkRepository,
        pS2Settings,
        languageProvider,
        dispatcherProvider,
    )

    @Provides
    fun providesMainMenuViewModel(
        pS2LinkRepository: PS2LinkRepository,
        pS2Settings: PS2Settings,
        languageProvider: LanguageProvider,
        dispatcherProvider: DispatcherProvider,
    ): MainMenuViewModel = MainMenuViewModel(
        pS2LinkRepository,
        pS2Settings,
        languageProvider,
        dispatcherProvider,
    )

    @Provides
    fun providesOutfitListViewModel(
        pS2LinkRepository: PS2LinkRepository,
        pS2Settings: PS2Settings,
        languageProvider: LanguageProvider,
        dispatcherProvider: DispatcherProvider,
    ): OutfitListViewModel = OutfitListViewModel(
        pS2LinkRepository,
        pS2Settings,
        languageProvider,
        dispatcherProvider,
    )

    @Provides
    fun providesMembersViewModel(
        pS2LinkRepository: PS2LinkRepository,
        pS2Settings: PS2Settings,
        languageProvider: LanguageProvider,
        dispatcherProvider: DispatcherProvider,
    ): MembersViewModel = MembersViewModel(
        pS2LinkRepository,
        pS2Settings,
        languageProvider,
        dispatcherProvider,
    )

    @Provides
    fun providesOnlineMembersViewModel(
        pS2LinkRepository: PS2LinkRepository,
        pS2Settings: PS2Settings,
        languageProvider: LanguageProvider,
        dispatcherProvider: DispatcherProvider,
    ): OnlineMembersViewModel = OnlineMembersViewModel(
        pS2LinkRepository,
        pS2Settings,
        languageProvider,
        dispatcherProvider,
    )

    @Provides
    fun providesOutfitViewModel(
        pS2LinkRepository: PS2LinkRepository,
        pS2Settings: PS2Settings,
        languageProvider: LanguageProvider,
        dispatcherProvider: DispatcherProvider,
    ): OutfitViewModel = OutfitViewModel(
        pS2LinkRepository,
        pS2Settings,
        languageProvider,
        dispatcherProvider,
    )

    @Provides
    fun providesProfileListViewModel(
        pS2LinkRepository: PS2LinkRepository,
        pS2Settings: PS2Settings,
        languageProvider: LanguageProvider,
        dispatcherProvider: DispatcherProvider,
    ): ProfileListViewModel = ProfileListViewModel(
        pS2LinkRepository,
        pS2Settings,
        languageProvider,
        dispatcherProvider,
    )

    @Provides
    fun providesFriendListViewModel(
        pS2LinkRepository: PS2LinkRepository,
        pS2Settings: PS2Settings,
        languageProvider: LanguageProvider,
        dispatcherProvider: DispatcherProvider,
    ): FriendListViewModel = FriendListViewModel(
        pS2LinkRepository,
        pS2Settings,
        dispatcherProvider,
        languageProvider,
    )

    @Provides
    fun providesKillListViewModel(
        pS2LinkRepository: PS2LinkRepository,
        pS2Settings: PS2Settings,
        languageProvider: LanguageProvider,
        dispatcherProvider: DispatcherProvider,
    ): KillListViewModel = KillListViewModel(
        pS2LinkRepository,
        pS2Settings,
        languageProvider,
        dispatcherProvider,
    )

    @Provides
    fun providesProfileViewModel(
        pS2LinkRepository: PS2LinkRepository,
        pS2Settings: PS2Settings,
        languageProvider: LanguageProvider,
        dispatcherProvider: DispatcherProvider,
    ): ProfileViewModel = ProfileViewModel(
        pS2LinkRepository,
        pS2Settings,
        languageProvider,
        dispatcherProvider,
    )

    @Provides
    fun providesStatListViewModel(
        pS2LinkRepository: PS2LinkRepository,
        pS2Settings: PS2Settings,
        languageProvider: LanguageProvider,
        dispatcherProvider: DispatcherProvider,
    ): StatListViewModel = StatListViewModel(
        pS2LinkRepository,
        pS2Settings,
        languageProvider,
        dispatcherProvider,
    )

    @Provides
    fun providesWeaponListViewModel(
        pS2LinkRepository: PS2LinkRepository,
        pS2Settings: PS2Settings,
        languageProvider: LanguageProvider,
        dispatcherProvider: DispatcherProvider,
    ): WeaponListViewModel = WeaponListViewModel(
        pS2LinkRepository,
        pS2Settings,
        languageProvider,
        dispatcherProvider,
    )

    @Provides
    fun providesRedditViewModel(
        pS2LinkRepository: PS2LinkRepository,
        pS2Settings: PS2Settings,
        redditRepository: RedditRepository,
        languageProvider: LanguageProvider,
        dispatcherProvider: DispatcherProvider,
    ): RedditViewModel = RedditViewModel(
        pS2LinkRepository,
        pS2Settings,
        languageProvider,
        redditRepository,
        dispatcherProvider,
    )

    @Provides
    fun providesServerListViewModel(
        pS2LinkRepository: PS2LinkRepository,
        pS2Settings: PS2Settings,
        languageProvider: LanguageProvider,
        dispatcherProvider: DispatcherProvider,
    ): ServerListViewModel = ServerListViewModel(
        pS2LinkRepository,
        pS2Settings,
        languageProvider,
        dispatcherProvider,
    )

    @Provides
    fun providesTwitterListViewModel(
        pS2LinkRepository: PS2LinkRepository,
        pS2Settings: PS2Settings,
        twitterRepository: TwitterRepository,
        languageProvider: LanguageProvider,
        dispatcherProvider: DispatcherProvider,
    ): TwitterListViewModel = TwitterListViewModel(
        pS2LinkRepository,
        pS2Settings,
        languageProvider,
        dispatcherProvider,
        twitterRepository,
    )

    @Provides
    fun provideNoopViewModel(
        pS2LinkRepository: PS2LinkRepository,
        pS2Settings: PS2Settings,
        languageProvider: LanguageProvider,
        dispatcherProvider: DispatcherProvider,
    ): NoopPS2ViewModel = NoopPS2ViewModel(
        pS2LinkRepository,
        pS2Settings,
        dispatcherProvider,
        languageProvider,
    )
}
