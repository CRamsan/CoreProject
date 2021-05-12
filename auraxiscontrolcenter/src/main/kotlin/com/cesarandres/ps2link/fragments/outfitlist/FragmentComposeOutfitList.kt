package com.cesarandres.ps2link.fragments.outfitlist

import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.cesarandres.ps2link.R
import com.cesarandres.ps2link.base.BaseComposePS2Fragment
import dagger.hilt.android.AndroidEntryPoint

/**
 * Fragment to display the list of locally stored profiles.
 */
@AndroidEntryPoint
class FragmentComposeOutfitList : BaseComposePS2Fragment<OutfitListViewModel>() {

    override val logTag = "FragmentComposeOutfitList"
    override val viewModel: OutfitListViewModel by viewModels()

    @Composable
    override fun CreateComposeContent() {
        val outfitList = viewModel.outfitList.observeAsState(emptyList())
        OutfitListCompose(
            outfitItems = outfitList.value,
            eventHandler = viewModel,
        )
    }

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
