package com.cesarandres.ps2link.fragments.outfitpager

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.cesarandres.ps2link.R
import com.cesarandres.ps2link.base.BasePS2FragmentPager
import com.cesarandres.ps2link.fragments.outfitpager.members.FragmentComposeMembers
import com.cesarandres.ps2link.fragments.outfitpager.online.FragmentComposeOnlineMembers
import com.cesarandres.ps2link.fragments.outfitpager.outfit.FragmentComposeOutfit
import com.cramsan.ps2link.core.models.Namespace
import dagger.hilt.android.AndroidEntryPoint

/**
 * This fragment holds a view pager for all the profile related fragments
 */
@AndroidEntryPoint
class FragmentOutfitPager : BasePS2FragmentPager<OutfitPagerViewModel>() {

    override val viewModel: OutfitPagerViewModel by viewModels()
    override val logTag = "FragmentOutfitPager"
    val args: FragmentOutfitPagerArgs by navArgs()

    private lateinit var outfitId: String
    private lateinit var namespace: Namespace

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        setHasOptionsMenu(true)

        outfitId = args.outfitId
        namespace = args.namespace
        viewModel.setUp(outfitId, namespace)
        return view
    }

    override fun itemCount() = OutfitPage.values().size

    override fun pageTitle(position: Int) = OutfitPage.values()[position].name

    override fun createFragment(position: Int): Fragment {
        return when (OutfitPage.values()[position]) {
            OutfitPage.OUTFIT -> FragmentComposeOutfit.instance(outfitId, namespace)
            OutfitPage.ONLINE -> FragmentComposeOnlineMembers.instance(outfitId, namespace)
            OutfitPage.MEMBERS -> FragmentComposeMembers.instance(outfitId, namespace)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.outfit_menu, menu)
        return
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_add -> {
                viewModel.addOutfit()
            }
            R.id.action_remove -> {
                viewModel.removeOutfit()
            }
            R.id.action_star -> {
                viewModel.pinOutfit()
            }
            R.id.action_unstar -> {
                viewModel.unpinOutfit()
            }
            R.id.action_update -> {
            }
        }
        return true
    }

    private enum class OutfitPage {
        OUTFIT,
        ONLINE,
        MEMBERS,
    }
}
