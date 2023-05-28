package com.cramsan.framework.core

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.fragment.app.Fragment
import com.cramsan.framework.logging.logD

/**
 * This class provides some helpful defaults that should be generally used when implementing new
 * classes that inherit from [Fragment]. [logTag] is required so we can identify the source of the
 * events.
 *
 * TODO: Refactor the viewModel from [ComposeBaseFragment] and [BaseDatabindingFragment] into this class.
 */
@Suppress("TooManyFunctions")
abstract class BaseFragment : Fragment() {

    /**
     * String that identifies this class. Used for logging and telemetry.
     */
    abstract val logTag: String

    @CallSuper
    @Suppress("UndocumentedPublicProperty")
    override fun onAttach(context: Context) {
        super.onAttach(context)
        logD(logTag, "onAttach")
    }

    @CallSuper
    @Suppress("UndocumentedPublicProperty")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        logD(logTag, "onCreate")
    }

    @CallSuper
    @Suppress("UndocumentedPublicProperty")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        logD(logTag, "onCreateView")
        return view
    }

    @CallSuper
    @Suppress("UndocumentedPublicProperty")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        logD(logTag, "onViewCreated")
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
    override fun onDestroyView() {
        super.onDestroyView()
        logD(logTag, "onDestroyView")
    }

    @CallSuper
    @Suppress("UndocumentedPublicProperty")
    override fun onDestroy() {
        super.onDestroy()
        logD(logTag, "onDestroy")
    }

    @CallSuper
    @Suppress("UndocumentedPublicProperty")
    override fun onDetach() {
        super.onDetach()
        logD(logTag, "onDetach")
    }

    @CallSuper
    @Suppress("UndocumentedPublicProperty")
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        logD(logTag, "onSaveInstanceState")
    }

    @CallSuper
    @Suppress("UndocumentedPublicProperty")
    protected open fun onViewModelEvent(event: BaseEvent) {
        logD(logTag, "Event: $event")
    }
}
