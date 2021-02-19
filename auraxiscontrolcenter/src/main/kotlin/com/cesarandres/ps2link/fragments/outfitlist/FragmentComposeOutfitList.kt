package com.cesarandres.ps2link.fragments.outfitlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.cesarandres.ps2link.R
import com.cesarandres.ps2link.base.BaseComposePS2Fragment
import com.cesarandres.ps2link.fragments.OpenOutfit
import com.cramsan.framework.core.BaseEvent
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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        setHasOptionsMenu(true)
        requireActivity().title = getString(R.string.title_profiles)
        return view
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

    override fun onViewModelEvent(event: BaseEvent) {
        super.onViewModelEvent(event)
        when (event) {
            is OpenOutfit -> {
                val action = FragmentComposeOutfitListDirections.actionFragmentOutfitListToFragmentOutfitPager(event.outfitId, event.namespace)
                findNavController().navigate(action)
            }
        }
    }
}
