package com.cesarandres.ps2link.fragments.outfitpager.outfit

import android.os.Bundle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.fragment.app.viewModels
import com.cesarandres.ps2link.base.BaseComposePS2Fragment
import com.cramsan.ps2link.core.models.Namespace
import dagger.hilt.android.AndroidEntryPoint
import kotlin.time.ExperimentalTime

/**
 * Fragment to display the list of locally stored profiles.
 */
@AndroidEntryPoint
class FragmentComposeOutfit : BaseComposePS2Fragment<OutfitViewModel>() {

    override val logTag = "FragmentComposeOutfit"
    override val viewModel: OutfitViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val outfitId = arguments?.getString(OUTFIT_ID_KEY)
        val namespace = arguments?.getSerializable(NAMESPACE_KEY) as Namespace?

        viewModel.setUp(outfitId, namespace)
    }

    @OptIn(ExperimentalTime::class)
    @Composable
    override fun CreateComposeContent() {
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
            eventHandler = viewModel
        )
    }

    companion object {

        private const val OUTFIT_ID_KEY = "outfitId"
        private const val NAMESPACE_KEY = "namespace"

        fun instance(outfitId: String, namespace: Namespace): FragmentComposeOutfit {
            val bundle = Bundle().apply {
                putString(OUTFIT_ID_KEY, outfitId)
                putSerializable(NAMESPACE_KEY, namespace)
            }
            return FragmentComposeOutfit().apply {
                arguments = bundle
            }
        }
    }
}
