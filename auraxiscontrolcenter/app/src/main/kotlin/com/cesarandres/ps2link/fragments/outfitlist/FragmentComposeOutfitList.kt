package com.cesarandres.ps2link.fragments.outfitlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.cesarandres.ps2link.R
import com.cesarandres.ps2link.base.BaseComposePS2Fragment
import com.cramsan.ps2link.appfrontend.outfitlist.OutfitListCompose
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.collections.immutable.persistentListOf

/**
 * Fragment to display the list of locally stored profiles.
 */
@AndroidEntryPoint
class FragmentComposeOutfitList : BaseComposePS2Fragment<OutfitListAndroidViewModel>() {

    override val logTag = "FragmentComposeOutfitList"
    override val viewModel: OutfitListAndroidViewModel by viewModels()

    @Composable
    override fun CreateComposeContent() {
        val outfitList = viewModel.outfitList.collectAsState(initial = persistentListOf())
        OutfitListCompose(
            outfitItems = outfitList.value,
            eventHandler = viewModel,
        )
    }

    // TODO: Migrate to the new MenuProvider API
    // https://developer.android.com/jetpack/androidx/releases/activity#1.4.0-alpha01
    @Suppress("DEPRECATION")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        setHasOptionsMenu(true)
        return view
    }

    // TODO: Migrate to the new MenuProvider API
    // https://developer.android.com/jetpack/androidx/releases/activity#1.4.0-alpha01
    @Suppress("DEPRECATION")
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.main_menu, menu)
        return
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_search -> {
                val action = FragmentComposeOutfitListDirections.actionFragmentOutfitListToFragmentAddOutfit()
                findNavController().navigate(action)
            }
        }
        return true
    }
}
