package com.cramsan.framework.core.compose

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.CallSuper
import androidx.compose.runtime.Composable
import androidx.lifecycle.lifecycleScope
import com.cramsan.framework.core.BaseEvent
import com.cramsan.framework.core.BaseViewModel
import com.cramsan.framework.logging.logD
import kotlinx.coroutines.launch

/**
 * This class provides some helpful defaults that should be generally used when implementing new
 * classes that inherit from [Activity]. [viewModel] is considered a good pattern so it is required
 * to specify which one will be used. If a viewModel is not required then [NoopViewModel] can be used.
 * This class is based on the existing [BaseActivity] but removes the decoupling from the View system.
 */
abstract class BaseComposeActivity<T : BaseViewModel> : ComponentActivity() {

    /**
     * ViewModel instance that will be used to manage this [Activity]. The class that extends [BaseComposeActivity] is in
     * charge of providing the implementation.
     */
    protected abstract val viewModel: T

    /**
     * String that identifies this class. Used for logging and telemetry.
     */
    abstract val logTag: String

    @CallSuper
    @Suppress("UndocumentedPublicProperty")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        logD(logTag, "onCreate")

        // Start a coroutine in the lifecycle scope
        lifecycleScope.launch {
            viewModel.events.collect {
                onViewModelEvent(it)
            }
        }

        setContent {
            ApplyTheme {
                CreateComposeContent()
            }
        }
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

    /**
     * Implement this function to start to compose your UI.
     */
    @Composable
    abstract fun CreateComposeContent()

    /**
     * Set the default theme for this activity.
     */
    @Composable
    abstract fun ApplyTheme(content: @Composable () -> Unit)

    /**
     * Handle the [event] in this activity.
     */
    @CallSuper
    protected open fun onViewModelEvent(event: BaseEvent) {
        logD(logTag, "Event: $event")
    }
}
