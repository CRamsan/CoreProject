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
abstract class BaseFragment : Fragment() {

    abstract val logTag: String

    @CallSuper
    override fun onAttach(context: Context) {
        super.onAttach(context)
        logD(logTag, "onAttach")
    }

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        logD(logTag, "onCreate")
    }

    @CallSuper
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        logD(logTag, "onCreateView")
        return view
    }

    @CallSuper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        logD(logTag, "onViewCreated")
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
    override fun onDestroyView() {
        super.onDestroyView()
        logD(logTag, "onDestroyView")
    }

    @CallSuper
    override fun onDestroy() {
        super.onDestroy()
        logD(logTag, "onDestroy")
    }

    @CallSuper
    override fun onDetach() {
        super.onDetach()
        logD(logTag, "onDetach")
    }

    @CallSuper
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        logD(logTag, "onSaveInstanceState")
    }

    @CallSuper
    protected open fun onViewModelEvent(event: BaseEvent) {
        logD(logTag, "Event: $event")
    }
}
