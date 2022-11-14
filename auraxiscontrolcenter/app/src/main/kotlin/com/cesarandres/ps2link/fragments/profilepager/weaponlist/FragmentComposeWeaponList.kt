package com.cesarandres.ps2link.fragments.profilepager.weaponlist

import android.os.Bundle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.fragment.app.viewModels
import com.cesarandres.ps2link.base.BaseComposePS2Fragment
import com.cramsan.ps2link.core.models.Faction
import com.cramsan.ps2link.core.models.Namespace
import dagger.hilt.android.AndroidEntryPoint

/**
 * Fragment to display the list of locally stored profiles.
 */
@AndroidEntryPoint
class FragmentComposeWeaponList : BaseComposePS2Fragment<WeaponListViewModel>() {

    override val logTag = "FragmentComposeProfile"
    override val viewModel: WeaponListViewModel by viewModels()

    @Suppress("DEPRECATION")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val characterId = arguments?.getString(CHARACTER_ID_KEY)
        val namespace = arguments?.getSerializable(NAMESPACE_KEY) as Namespace?

        viewModel.setUp(characterId, namespace)
    }

    @Composable
    override fun CreateComposeContent() {
        val weaponList = viewModel.weaponList.collectAsState()
        val faction = viewModel.faction.collectAsState(Faction.UNKNOWN)
        val isLoading = viewModel.isLoading.collectAsState()
        val isError = viewModel.isError.collectAsState()
        WeaponListCompose(
            faction = faction.value,
            weaponList = weaponList.value,
            isError = isError.value,
            isLoading = isLoading.value,
            eventHandler = viewModel,
        )
    }

    companion object {

        private const val CHARACTER_ID_KEY = "characterId"
        private const val NAMESPACE_KEY = "namespace"

        fun instance(characterId: String, namespace: Namespace): FragmentComposeWeaponList {
            val bundle = Bundle().apply {
                putString(CHARACTER_ID_KEY, characterId)
                putSerializable(NAMESPACE_KEY, namespace)
            }
            return FragmentComposeWeaponList().apply {
                arguments = bundle
            }
        }
    }
}
