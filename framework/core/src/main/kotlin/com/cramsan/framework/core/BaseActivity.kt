package com.cramsan.framework.core

import android.os.Bundle
import androidx.annotation.CallSuper
import androidx.appcompat.app.AppCompatActivity
import com.cramsan.framework.logging.EventLoggerInterface
import com.cramsan.framework.logging.Severity
import com.cramsan.framework.metrics.MetricsInterface
import javax.inject.Inject

abstract class BaseActivity<T : BaseViewModel> : AppCompatActivity() {

    @Inject
    protected lateinit var eventLogger: EventLoggerInterface

    @Inject
    lateinit var metrics: MetricsInterface

    protected lateinit var viewModel: T

    abstract val contentViewLayout: Int
    abstract val toolbarViewId: Int?
    abstract val logTag: String

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        eventLogger.log(Severity.INFO, logTag, "onCreate")
        setContentView(contentViewLayout)
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
