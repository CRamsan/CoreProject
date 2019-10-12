package com.cramsan.petproject.mainmenu

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import com.cramsan.petproject.R
import com.cramsan.petproject.about.AboutActivity
import com.cramsan.petproject.appcore.model.AnimalType
import com.cramsan.petproject.base.BaseFragment
import com.cramsan.petproject.dialog.DownloadDialogActivity
import com.cramsan.petproject.plantslist.PlantsListActivity
import com.cramsan.petproject.plantslist.PlantsListFragment.Companion.ANIMAL_TYPE
import kotlinx.android.synthetic.main.fragment_main_menu.main_menu_about
import kotlinx.android.synthetic.main.fragment_main_menu.main_menu_cats
import kotlinx.android.synthetic.main.fragment_main_menu.main_menu_dogs

class MainMenuFragment : BaseFragment() {

    private lateinit var viewModel: DownloadCatalogViewModel

    override val contentViewLayout: Int
        get() = R.layout.fragment_main_menu

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProviders.of(this).get(DownloadCatalogViewModel::class.java)
        main_menu_cats.setOnClickListener {
            val intent = Intent(requireContext(), PlantsListActivity::class.java)
            intent.putExtra(ANIMAL_TYPE, AnimalType.CAT.ordinal)
            startActivity(intent)
        }
        main_menu_dogs.setOnClickListener {
            val intent = Intent(requireContext(), PlantsListActivity::class.java)
            intent.putExtra(ANIMAL_TYPE, AnimalType.DOG.ordinal)
            startActivity(intent)
        }
        main_menu_about.setOnClickListener {
            val intent = Intent(requireContext(), AboutActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        if (!viewModel.isCatalogReady()) {
            val intent = Intent(requireContext(), DownloadDialogActivity::class.java)
            startActivity(intent)
        }
    }
}
