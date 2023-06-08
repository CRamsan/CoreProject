package com.cramsan.framework.core

import android.app.Activity
import android.os.Bundle
import androidx.annotation.CallSuper
import androidx.appcompat.app.AppCompatActivity
import com.cramsan.framework.logging.logD

/**
 * This class provides some helpful defaults that should be generally used when implementing new
 * classes that inherit from [Activity]. [viewModel] is considered a good pattern so it is required
 * to specify which one will be used. If a viewModel is not required then [NoopViewModel] can be used.
 */
abstract class BaseActivity<T : BaseAndroidViewModel> : AppCompatActivity() {

    /**
     * ViewModel instance that will be used to manage this [Activity]. The class that extends [BaseActivity] is in
     * charge of providing the implementation.
     */
    protected abstract val viewModel: T

    /**
     * String that identifies this class. Used for logging and telemetry.
     */
    abstract val logTag: String

    @CallSuper
    @Suppress("UndocumentedPublicProperty")
    override fun onStart() {
        super.onStart()
        logD(logTag, "onStart")
    }

    @CallSuper
    @Suppress("UndocumentedPublicProperty")
    override fun onResume() {
        super.onResume()
        logD(logTag, "onResume")
    }

    @CallSuper
    @Suppress("UndocumentedPublicProperty")
    override fun onPause() {
        super.onPause()
        logD(logTag, "onPause")
    }

    @CallSuper
    @Suppress("UndocumentedPublicProperty")
    override fun onStop() {
        super.onStop()
        logD(logTag, "onStop")
    }

    @CallSuper
    @Suppress("UndocumentedPublicProperty")
    override fun onRestart() {
        super.onRestart()
        logD(logTag, "onRestart")
    }

    @CallSuper
    @Suppress("UndocumentedPublicProperty")
    override fun onDestroy() {
        super.onDestroy()
        logD(logTag, "onDestroy")
    }

    @CallSuper
    @Suppress("UndocumentedPublicProperty")
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        logD(logTag, "onSaveInstanceState")
    }

    @CallSuper
    @Suppress("UndocumentedPublicProperty")
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        logD(logTag, "onRestoreInstanceState")
    }
}
