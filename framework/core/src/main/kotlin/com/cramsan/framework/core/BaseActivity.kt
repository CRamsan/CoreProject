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

    abstract val viewModel: T
    abstract val contentViewLayout: Int
    abstract val toolbarViewId: Int?
    abstract val logTag: String

    var toolbar: MaterialToolbar? = null

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        logD(logTag, "onCreate")
        setContentView(contentViewLayout)

        toolbar = toolbarViewId?.let { findViewById(it) }
        toolbar?.apply {
            setSupportActionBar(this)
        }
    }

    @CallSuper
    override fun onStart() {
        super.onStart()
        logD(logTag, "onStart")
    }

    @CallSuper
    override fun onResume() {
        super.onResume()
        logD(logTag, "onResume")
    }

    @CallSuper
    override fun onPause() {
        super.onPause()
        logD(logTag, "onPause")
    }

    @CallSuper
    override fun onStop() {
        super.onStop()
        logD(logTag, "onStop")
    }

    @CallSuper
    override fun onRestart() {
        super.onRestart()
        logD(logTag, "onRestart")
    }

    @CallSuper
    override fun onDestroy() {
        super.onDestroy()
        logD(logTag, "onDestroy")
    }

    @CallSuper
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        logD(logTag, "onSaveInstanceState")
    }

    @CallSuper
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        logD(logTag, "onRestoreInstanceState")
    }
}
