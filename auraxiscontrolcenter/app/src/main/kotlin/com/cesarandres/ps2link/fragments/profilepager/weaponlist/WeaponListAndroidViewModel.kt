package com.cesarandres.ps2link.fragments.profilepager.weaponlist

import com.cramsan.ps2link.appfrontend.BasePS2AndroidViewModel
import com.cramsan.ps2link.appfrontend.profilepager.weaponlist.WeaponListEventHandler
import com.cramsan.ps2link.appfrontend.profilepager.weaponlist.WeaponListViewModel
import com.cramsan.ps2link.appfrontend.profilepager.weaponlist.WeaponListViewModelInterface
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@Suppress("UndocumentedPublicClass")
@HiltViewModel
class WeaponListAndroidViewModel @Inject constructor(
    viewModel: WeaponListViewModel,
) : BasePS2AndroidViewModel<WeaponListViewModel>(
    viewModel,
),
    WeaponListEventHandler by viewModel,
    WeaponListViewModelInterface by viewModel {

    override val logTag: String
        get() = "WeaponListViewModel"
}
