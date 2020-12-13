package com.cramsan.petproject.base

import android.os.Bundle
import androidx.annotation.CallSuper
import androidx.appcompat.widget.Toolbar
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.cramsan.framework.core.BaseActivity
import com.cramsan.framework.core.BaseViewModel
import com.cramsan.petproject.R
import com.google.android.material.appbar.MaterialToolbar

abstract class BaseNavActivity<T : BaseViewModel> : BaseActivity<T>() {

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val toolbar = toolbarViewId?.let { findViewById<MaterialToolbar>(it) }
        toolbar?.apply {
            val navController = findNavController(R.id.nav_host_fragment)
            val appBarConfiguration = AppBarConfiguration(navController.graph)
            setSupportActionBar(this)
            findViewById<Toolbar>(R.id.main_menu_toolbar)
                .setupWithNavController(navController, appBarConfiguration)
        }
    }
}
