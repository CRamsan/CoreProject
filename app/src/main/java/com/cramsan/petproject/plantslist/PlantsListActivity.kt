package com.cramsan.petproject.plantslist

import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import com.cramsan.framework.logging.Severity
import com.cramsan.framework.logging.classTag
import com.cramsan.petproject.R
import com.cramsan.petproject.base.BaseActivity
import kotlinx.android.synthetic.main.activity_plants_list.plant_list_toolbar

class PlantsListActivity : BaseActivity(), PlantsListFragment.OnListFragmentInteractionListener {
    override val contentViewLayout: Int
        get() = R.layout.activity_plants_list
    override val titleResource: Int?
        get() = null
    override val toolbar: Toolbar?
        get() = plant_list_toolbar

    private var queryTextListener: SearchView.OnQueryTextListener? = null

    override fun onNewSearchable(listener: SearchView.OnQueryTextListener) {
        eventLogger.log(Severity.INFO, classTag(), "onNewSearchable")
        queryTextListener = listener
    }
}
