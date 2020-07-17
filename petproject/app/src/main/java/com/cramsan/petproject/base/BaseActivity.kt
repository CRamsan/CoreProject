package com.cramsan.petproject.base

import android.os.Bundle
import androidx.annotation.CallSuper
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.cramsan.framework.logging.EventLoggerInterface
import com.cramsan.framework.logging.Severity
import com.google.android.material.appbar.MaterialToolbar
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.erased.instance

abstract class BaseActivity<T : BaseViewModel, U : ViewDataBinding> : AppCompatActivity(),
    KodeinAware {

    override val kodein by kodein()
    protected val eventLogger: EventLoggerInterface by instance()
    protected lateinit var viewModel: T
    protected lateinit var dataBinding: U

    abstract val contentViewLayout: Int
    abstract val toolbarViewId: Int?
    abstract val logTag: String
    abstract val enableUp: Boolean?
    abstract val enableDataBinding: Boolean
    abstract val titleResource: Int?

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        eventLogger.log(Severity.INFO, logTag, "onCreate")

        if (enableDataBinding) {
            dataBinding = DataBindingUtil.setContentView(this, contentViewLayout)
        } else {
            setContentView(contentViewLayout)
        }

        val toolbar = toolbarViewId?.let { findViewById<MaterialToolbar>(it) }
        toolbar?.apply {
            setSupportActionBar(this)
            enableUp?.let { supportActionBar?.setDisplayHomeAsUpEnabled(it) }
            titleResource?.apply {
                supportActionBar?.setTitle(this)
            }
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
