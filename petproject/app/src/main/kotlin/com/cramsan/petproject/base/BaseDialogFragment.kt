package com.cramsan.petproject.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment
import com.cramsan.framework.core.BaseViewModel
import com.cramsan.framework.logging.EventLoggerInterface
import com.cramsan.framework.logging.Severity
import com.cramsan.framework.userevents.UserEventsInterface
import javax.inject.Inject

abstract class BaseDialogFragment<T : BaseViewModel, U : ViewDataBinding> : DialogFragment() {

    @Inject
    lateinit var eventLogger: EventLoggerInterface

    @Inject
    lateinit var userEvents: UserEventsInterface

    abstract val logTag: String
    protected lateinit var viewModel: T
    protected lateinit var dataBinding: U
    abstract val contentViewLayout: Int

    @CallSuper
    override fun onAttach(context: Context) {
        super.onAttach(context)
        eventLogger.log(Severity.INFO, logTag, "onAttach")
    }

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        eventLogger.log(Severity.INFO, logTag, "onCreate")
    }

    @CallSuper
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        eventLogger.log(Severity.INFO, logTag, "onCreateView")
        dataBinding = DataBindingUtil.inflate(inflater, contentViewLayout, container, false)
        dataBinding.lifecycleOwner = this
        return dataBinding.root
    }

    @CallSuper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        eventLogger.log(Severity.INFO, logTag, "onViewCreated")
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
    override fun onDestroyView() {
        super.onDestroyView()
        eventLogger.log(Severity.INFO, logTag, "onDestroyView")
    }

    @CallSuper
    override fun onDestroy() {
        super.onDestroy()
        eventLogger.log(Severity.INFO, logTag, "onDestroy")
    }

    @CallSuper
    override fun onDetach() {
        super.onDetach()
        eventLogger.log(Severity.INFO, logTag, "onDetach")
    }

    @CallSuper
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        eventLogger.log(Severity.INFO, logTag, "onSaveInstanceState")
    }
}
