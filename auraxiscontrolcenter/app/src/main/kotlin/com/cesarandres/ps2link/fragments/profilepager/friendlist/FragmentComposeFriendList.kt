package com.cesarandres.ps2link.fragments.profilepager.friendlist

import android.os.Bundle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.fragment.app.viewModels
import com.cesarandres.ps2link.base.BaseComposePS2Fragment
import com.cramsan.ps2link.core.models.Namespace
import dagger.hilt.android.AndroidEntryPoint

/**
 * Fragment to display the list of locally stored profiles.
 */
@AndroidEntryPoint
class FragmentComposeFriendList : BaseComposePS2Fragment<FriendListViewModel>() {

    override val logTag = "FragmentComposeProfile"
    override val viewModel: FriendListViewModel by viewModels()

    @Suppress("DEPRECATION")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val characterId = arguments?.getString(CHARACTER_ID_KEY)
        val namespace = arguments?.getSerializable(NAMESPACE_KEY) as Namespace?

        viewModel.setUp(characterId, namespace)
    }

    @Composable
    override fun CreateComposeContent() {
        val friendList = viewModel.friendList.collectAsState(emptyList())
        val isLoading = viewModel.isLoading.collectAsState()
        val isError = viewModel.isError.collectAsState()
        FriendListCompose(
            friendList = friendList.value,
            isLoading = isLoading.value,
            isError = isError.value,
            eventHandler = viewModel,
        )
    }

    companion object {

        private const val CHARACTER_ID_KEY = "characterId"
        private const val NAMESPACE_KEY = "namespace"

        fun instance(characterId: String, namespace: Namespace): FragmentComposeFriendList {
            val bundle = Bundle().apply {
                putString(CHARACTER_ID_KEY, characterId)
                putSerializable(NAMESPACE_KEY, namespace)
            }
            return FragmentComposeFriendList().apply {
                arguments = bundle
            }
        }
    }
}
