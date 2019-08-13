package com.cramsan.petproject.about

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.cramsan.petproject.R
import kotlinx.android.synthetic.main.activity_about.about_toolbar

class AboutActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)

        setSupportActionBar(about_toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
}
