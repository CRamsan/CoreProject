package com.cesarandres.ps2link.fragments.profilepager.statlist

import android.os.Bundle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.cesarandres.ps2link.base.BaseComposePS2Fragment
import com.cesarandres.ps2link.fragments.OpenOutfit
import com.cesarandres.ps2link.fragments.profilepager.FragmentProfilePagerDirections
import com.cramsan.framework.core.BaseEvent
import com.cramsan.ps2link.core.models.Namespace
import dagger.hilt.android.AndroidEntryPoint

/**
 * Fragment to display the list of locally stored profiles.
 */
@AndroidEntryPoint
class FragmentComposeStatList : BaseComposePS2Fragment<StatListViewModel>() {

    override val logTag = "FragmentComposeProfile"
    override val viewModel: StatListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val characterId = arguments?.getString(CHARACTER_ID_KEY)
        val namespace = arguments?.getSerializable(NAMESPACE_KEY) as Namespace?

        viewModel.setUp(characterId, namespace)
    }

    @Composable
    override fun CreateComposeContent() {
        val statList = viewModel.statList.collectAsState(emptyList())
        val isLoading = viewModel.isLoading.collectAsState()
        StatListCompose(
            statList = statList.value,
            isLoading = isLoading.value,
            eventHandler = viewModel
        )
    }

    override fun onViewModelEvent(event: BaseEvent) {
        super.onViewModelEvent(event)
        when (event) {
            is OpenOutfit -> {
                val action =
                    FragmentProfilePagerDirections.actionFragmentProfileToFragmentOutfitPager(
                        event.outfitId,
                        event.namespace
                    )
                findNavController().navigate(action)
            }
        }
    }

    companion object {

        private const val CHARACTER_ID_KEY = "characterId"
        private const val NAMESPACE_KEY = "namespace"

        fun instance(characterId: String, namespace: Namespace): FragmentComposeStatList {
            val bundle = Bundle().apply {
                putString(CHARACTER_ID_KEY, characterId)
                putSerializable(NAMESPACE_KEY, namespace)
            }
            return FragmentComposeStatList().apply {
                arguments = bundle
            }
        }
    }
}