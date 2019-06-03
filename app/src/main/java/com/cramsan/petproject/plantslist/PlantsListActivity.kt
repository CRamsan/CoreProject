package com.cramsan.petproject.plantslist

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity;
import com.cramsan.petproject.R
import com.cramsan.petproject.plantslist.dummy.DummyContent

import kotlinx.android.synthetic.main.activity_plants_list.*

class PlantsListActivity : AppCompatActivity(), PlantsListFragment.OnListFragmentInteractionListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_plants_list)
        setSupportActionBar(toolbar_2)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onListFragmentInteraction(item: DummyContent.DummyItem?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
