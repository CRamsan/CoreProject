package com.cramsan.framework.core

import android.app.ActionBar
import android.app.Activity
import android.os.Bundle
import androidx.annotation.CallSuper
import androidx.appcompat.app.AppCompatActivity
import com.cramsan.framework.logging.logD
import com.google.android.material.appbar.MaterialToolbar

/**
 * This class provides some helpful defaults that should be generally used when implementing new
 * classes that inherit from [Activity]. [viewModel] is considered a good pattern so it is required
 * to specify which one will be used. If a viewModel is not required then [NoopViewModel] can be used.
 * A [contentViewLayout] is required and it should be a valid layout file. The [toolbarViewId] is
 * optional. If provided, this toolbar will be set as the [ActionBar] for this activity. [logTag] is
 * required so we can identify the source of the events.
 */
abstract class BaseActivity<T : BaseViewModel> : AppCompatActivity() {

    /**
     * ViewModel instance that will be used to manage this [Activity]. The class that extends [BaseActivity] is in
     * charge of providing the implementation.
     */
    protected abstract val viewModel: T

    /**
     * Id of the layout that will be automatically inflated by this [Activity].
     */
    protected abstract val contentViewLayout: Int

    /**
     * Id of the [MaterialToolbar] in the layout [contentViewLayout].
     */
    protected abstract val toolbarViewId: Int?

    /**
     * String that identifies this class. Used for logging and telemetry.
     */
    abstract val logTag: String

    /**
     * [MaterialToolbar] managed by the [BaseActivity]. The instance is set by providing the [toolbarViewId]. If the
     * layout does not contain a [MaterialToolbar] with id [toolbarViewId], then this instance will be null.
     */
    protected var toolbar: MaterialToolbar? = null
        private set

    @CallSuper
    @Suppress("UndocumentedPublicProperty")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        logD(logTag, "onCreate")
        setContentView(contentViewLayout)

        toolbar = toolbarViewId?.let { findViewById(it) }
        toolbar?.let { setSupportActionBar(it) }
    }

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
