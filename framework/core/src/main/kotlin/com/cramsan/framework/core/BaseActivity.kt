package com.cramsan.framework.core

import android.os.Bundle
import androidx.annotation.CallSuper
import androidx.appcompat.app.AppCompatActivity
import com.cramsan.framework.logging.logD
import com.google.android.material.appbar.MaterialToolbar

abstract class BaseActivity<T : BaseViewModel> : AppCompatActivity() {

    protected lateinit var viewModel: T

    abstract val contentViewLayout: Int
    abstract val toolbarViewId: Int?
    abstract val logTag: String

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        logD(logTag, "onCreate")
        setContentView(contentViewLayout)

        val toolbar = toolbarViewId?.let { findViewById<MaterialToolbar>(it) }
        toolbar?.apply {
            setSupportActionBar(this)
        }
    }

    @CallSuper
    override fun onStart() {
        super.onStart()
        logD(logTag, "onStart")
        viewModel.onStart()
    }

    @CallSuper
    override fun onResume() {
        super.onResume()
        logD(logTag, "onResume")
        viewModel.onResume()
    }

    @CallSuper
    override fun onPause() {
        super.onPause()
        logD(logTag, "onPause")
        viewModel.onPause()
    }

    @CallSuper
    override fun onStop() {
        super.onStop()
        logD(logTag, "onStop")
        viewModel.onStop()
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
