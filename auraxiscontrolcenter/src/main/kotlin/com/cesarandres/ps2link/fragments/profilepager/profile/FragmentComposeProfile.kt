package com.cesarandres.ps2link.fragments.profilepager.profile

import android.os.Bundle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.cesarandres.ps2link.R
import com.cesarandres.ps2link.base.BaseComposePS2Fragment
import com.cesarandres.ps2link.fragments.OpenOutfit
import com.cesarandres.ps2link.fragments.profilepager.FragmentProfilePagerDirections
import com.cramsan.framework.core.BaseEvent
import com.cramsan.ps2link.appcore.dbg.LoginStatus
import com.cramsan.ps2link.appcore.dbg.Namespace
import dagger.hilt.android.AndroidEntryPoint

/**
 * Fragment to display the list of locally stored profiles.
 */
@AndroidEntryPoint
class FragmentComposeProfile : BaseComposePS2Fragment<ProfileViewModel>() {

    override val logTag = "FragmentComposeProfile"
    override val viewModel: ProfileViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val characterId = arguments?.getString(CHARACTER_ID_KEY)
        val namespace = arguments?.getSerializable(NAMESPACE_KEY) as Namespace?

        viewModel.setUp(characterId, namespace)
    }

    @Composable
    override fun CreateComposeContent() {
        val profile = viewModel.profile.collectAsState(null)
        val isLoading = viewModel.isLoading.collectAsState()
        requireActivity().title = getString(R.string.title_profiles)
        ProfileCompose(
            faction = profile.value?.factionId,
            br = profile.value?.rank?.toInt(),
            percentToNextBR = profile.value?.percentageToNextRank?.toInt(),
            certs = profile.value?.currentPoints?.toInt(),
            percentToNextCert = profile.value?.percentageToNextCert?.toInt(),
            loginStatus = LoginStatus.UNKNOWN,
            lastLogin = profile.value?.lastLogin?.toString(),
            outfit = null,
            server = profile.value?.worldName,
            hoursPlayed = profile.value?.minutesPlayed?.toInt(),
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

        fun instance(characterId: String, namespace: Namespace): FragmentComposeProfile {
            val bundle = Bundle().apply {
                putString(CHARACTER_ID_KEY, characterId)
                putSerializable(NAMESPACE_KEY, namespace)
            }
            return FragmentComposeProfile().apply {
                arguments = bundle
            }
        }
    }
}
