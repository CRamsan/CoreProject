package com.cesarandres.ps2link.fragments.outfitpager.outfit

import android.os.Bundle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.fragment.app.viewModels
import com.cesarandres.ps2link.base.BaseComposePS2Fragment
import com.cesarandres.ps2link.fragments.profilepager.profile.ProfileCompose
import com.cesarandres.ps2link.fragments.profilepager.profile.ProfileViewModel
import com.cramsan.ps2link.core.models.LoginStatus
import com.cramsan.ps2link.core.models.Namespace
import dagger.hilt.android.AndroidEntryPoint
import kotlin.time.ExperimentalTime

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

    @OptIn(ExperimentalTime::class)
    @Composable
    override fun CreateComposeContent() {
        val profile = viewModel.profile.collectAsState(null)
        val isLoading = viewModel.isLoading.collectAsState()
        ProfileCompose(
            faction = profile.value?.faction,
            br = profile.value?.battleRank?.toInt(),
            percentToNextBR = profile.value?.percentageToNextBattleRank?.toInt(),
            certs = profile.value?.certs?.toInt(),
            percentToNextCert = profile.value?.percentageToNextCert?.toInt(),
            loginStatus = LoginStatus.UNKNOWN,
            lastLogin = profile.value?.lastLogin?.toString(),
            outfit = null,
            server = profile.value?.server?.serverName,
            hoursPlayed = profile.value?.timePlayed?.inHours,
            isLoading = isLoading.value,
            eventHandler = viewModel
        )
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
