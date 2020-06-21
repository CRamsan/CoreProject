package com.cramsan.petproject.base

import android.os.Bundle
import androidx.annotation.CallSuper
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import com.cramsan.framework.logging.EventLoggerInterface
import com.cramsan.framework.logging.Severity
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.erased.factory
import org.kodein.di.erased.instance

abstract class BaseActivity : AppCompatActivity(),
    KodeinAware {

    override val kodein by kodein()
    protected val eventLogger: EventLoggerInterface by instance()
    private val vmFactory: (ViewModelStoreOwner) -> ViewModelProvider by factory()

    lateinit var viewModelProvider: ViewModelProvider
    abstract val contentViewLayout: Int
    abstract val titleResource: Int?
    abstract val toolbar: Toolbar?
    abstract val tag: String
    open val enableUp: Boolean = true

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        eventLogger.log(Severity.INFO, tag, "onCreate")

        setContentView(contentViewLayout)

        toolbar?.apply {
            setSupportActionBar(this)
            supportActionBar?.setDisplayHomeAsUpEnabled(enableUp)
            titleResource?.apply {
                actionBar?.setTitle(this)
            }
        }
    }

    @CallSuper
    override fun onStart() {
        super.onStart()
        eventLogger.log(Severity.INFO, tag, "onStart")
    }

    @CallSuper
    override fun onResume() {
        super.onResume()
        eventLogger.log(Severity.INFO, tag, "onResume")
    }

    @CallSuper
    override fun onPause() {
        super.onPause()
        eventLogger.log(Severity.INFO, tag, "onPause")
    }

    @CallSuper
    override fun onStop() {
        super.onStop()
        eventLogger.log(Severity.INFO, tag, "onStop")
    }

    @CallSuper
    override fun onRestart() {
        super.onRestart()
        eventLogger.log(Severity.INFO, tag, "onRestart")
    }

    @CallSuper
    override fun onDestroy() {
        super.onDestroy()
        eventLogger.log(Severity.INFO, tag, "onDestroy")
    }

    protected fun <T : ViewModel> getViewModel(modelClass: Class<T>): T {
        return vmFactory.invoke(this).get(modelClass)
    }
}
