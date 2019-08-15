package com.cramsan.petproject.about

import android.os.Bundle
import com.cramsan.petproject.R
import com.cramsan.petproject.base.BaseActivity
import kotlinx.android.synthetic.main.activity_about.about_toolbar

class AboutActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_about)
        setSupportActionBar(about_toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setTitle(R.string.title_activity_about)
    }
}
