package com.cesarandres.ps2link.base

import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.cesarandres.ps2link.R
import com.cramsan.framework.core.BaseAndroidViewModel
import com.cramsan.framework.core.BaseViewActivity
import com.cramsan.ps2link.appcore.census.DBGServiceClient
import javax.inject.Inject

/**
 * This fragment handles setting the background for all activities.
 */
abstract class BasePS2Activity<T : BaseAndroidViewModel> : BaseViewActivity<T>() {

    @Inject
    protected lateinit var dbgCensus: DBGServiceClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        toolbar?.apply {
            val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
            val navController = navHostFragment.navController
            val appBarConfiguration = AppBarConfiguration(navController.graph)
            setupWithNavController(navController, appBarConfiguration)
        }
    }
}
