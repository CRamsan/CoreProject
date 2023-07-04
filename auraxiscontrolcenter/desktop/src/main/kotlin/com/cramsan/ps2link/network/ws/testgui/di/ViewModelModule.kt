package com.cramsan.ps2link.network.ws.testgui.di

import com.cramsan.ps2link.appfrontend.addoutfit.OutfitAddViewModel
import com.cramsan.ps2link.appfrontend.addoutfit.OutfitAddViewModelInterface
import com.cramsan.ps2link.appfrontend.addprofile.ProfileAddViewModel
import com.cramsan.ps2link.appfrontend.addprofile.ProfileAddViewModelInterface
import com.cramsan.ps2link.appfrontend.outfitpager.members.MembersViewModel
import com.cramsan.ps2link.appfrontend.outfitpager.members.MembersViewModelInterface
import com.cramsan.ps2link.appfrontend.outfitpager.online.OnlineMembersViewModel
import com.cramsan.ps2link.appfrontend.outfitpager.online.OnlineMembersViewModelInterface
import com.cramsan.ps2link.appfrontend.outfitpager.outfit.OutfitViewModel
import com.cramsan.ps2link.appfrontend.outfitpager.outfit.OutfitViewModelInterface
import com.cramsan.ps2link.appfrontend.profilepager.friendlist.FriendListViewModel
import com.cramsan.ps2link.appfrontend.profilepager.friendlist.FriendListViewModelInterface
import com.cramsan.ps2link.appfrontend.profilepager.killlist.KillListViewModel
import com.cramsan.ps2link.appfrontend.profilepager.killlist.KillListViewModelInterface
import com.cramsan.ps2link.appfrontend.profilepager.profile.ProfileViewModel
import com.cramsan.ps2link.appfrontend.profilepager.profile.ProfileViewModelInterface
import com.cramsan.ps2link.appfrontend.profilepager.statlist.StatListViewModel
import com.cramsan.ps2link.appfrontend.profilepager.statlist.StatListViewModelInterface
import com.cramsan.ps2link.appfrontend.profilepager.weaponlist.WeaponListViewModel
import com.cramsan.ps2link.appfrontend.profilepager.weaponlist.WeaponListViewModelInterface
import com.cramsan.ps2link.network.ws.testgui.ui.screens.settings.SettingsScreenViewModel
import com.cramsan.ps2link.network.ws.testgui.ui.screens.settings.SettingsScreenViewModelInterface
import com.cramsan.ps2link.network.ws.testgui.ui.screens.tracker.TrackerViewModel
import com.cramsan.ps2link.network.ws.testgui.ui.screens.tracker.TrackerViewModelInterface
import org.koin.dsl.module

val ViewModelModule = module {

    single<ProfileViewModelInterface> {
        ProfileViewModel(
            get(),
            get(),
            get(),
            get(),
        )
    }

    single<FriendListViewModelInterface> {
        FriendListViewModel(
            get(),
            get(),
            get(),
            get(),
        )
    }

    single<StatListViewModelInterface> {
        StatListViewModel(
            get(),
            get(),
            get(),
            get(),
        )
    }

    single<KillListViewModelInterface> {
        KillListViewModel(
            get(),
            get(),
            get(),
            get(),
        )
    }

    single<WeaponListViewModelInterface> {
        WeaponListViewModel(
            get(),
            get(),
            get(),
            get(),
        )
    }

    single<ProfileAddViewModelInterface> {
        ProfileAddViewModel(
            get(),
            get(),
            get(),
            get(),
        )
    }

    single<OutfitAddViewModelInterface> {
        OutfitAddViewModel(
            get(),
            get(),
            get(),
            get(),
        )
    }

    single<OutfitViewModelInterface> {
        OutfitViewModel(
            get(),
            get(),
            get(),
            get(),
        )
    }

    single<OnlineMembersViewModelInterface> {
        OnlineMembersViewModel(
            get(),
            get(),
            get(),
            get(),
        )
    }

    single<MembersViewModelInterface> {
        MembersViewModel(
            get(),
            get(),
            get(),
            get(),
        )
    }

    single<SettingsScreenViewModelInterface> {
        SettingsScreenViewModel(
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
        )
    }

    single<TrackerViewModelInterface> {
        TrackerViewModel(
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
        )
    }
}
