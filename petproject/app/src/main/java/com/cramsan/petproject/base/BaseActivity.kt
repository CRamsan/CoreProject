package com.cramsan.petproject.base

import android.os.Bundle
import androidx.annotation.CallSuper
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.cramsan.framework.logging.EventLoggerInterface
import com.cramsan.framework.logging.Severity
import com.cramsan.petproject.R
import com.google.android.material.appbar.MaterialToolbar
import javax.inject.Inject

abstract class BaseActivity<T : BaseViewModel> :
    AppCompatActivity() {

    @Inject
    protected lateinit var eventLogger: EventLoggerInterface
    protected lateinit var viewModel: T

    abstract val contentViewLayout: Int
    abstract val toolbarViewId: Int?
    abstract val logTag: String

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        eventLogger.log(Severity.INFO, logTag, "onCreate")

        setContentView(contentViewLayout)
        val toolbar = toolbarViewId?.let { findViewById<MaterialToolbar>(it) }
        toolbar?.apply {
            val navController = findNavController(R.id.nav_host_fragment)
            val appBarConfiguration = AppBarConfiguration(navController.graph)
            setSupportActionBar(this)
            findViewById<Toolbar>(R.id.main_menu_toolbar)
                .setupWithNavController(navController, appBarConfiguration)
        }
    }

    @CallSuper
    override fun onStart() {
        super.onStart()
        eventLogger.log(Severity.INFO, logTag, "onStart")
    }

    @CallSuper
    override fun onResume() {
        super.onResume()
        eventLogger.log(Severity.INFO, logTag, "onResume")
    }

    @CallSuper
    override fun onPause() {
        super.onPause()
        eventLogger.log(Severity.INFO, logTag, "onPause")
    }

    @CallSuper
    override fun onStop() {
        super.onStop()
        eventLogger.log(Severity.INFO, logTag, "onStop")
    }

    @CallSuper
    override fun onRestart() {
        super.onRestart()
        eventLogger.log(Severity.INFO, logTag, "onRestart")
    }

    @CallSuper
    override fun onDestroy() {
        super.onDestroy()
        eventLogger.log(Severity.INFO, logTag, "onDestroy")
    }

    @CallSuper
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        eventLogger.log(Severity.INFO, logTag, "onSaveInstanceState")
    }

    @CallSuper
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        eventLogger.log(Severity.INFO, logTag, "onRestoreInstanceState")
    }
}
