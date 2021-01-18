package com.cramsan.framework.core

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.cramsan.framework.logging.logD

abstract class BaseFragment<VM : BaseViewModel, DB : ViewDataBinding> : Fragment() {

    abstract val logTag: String
    abstract val viewModel: VM
    protected lateinit var dataBinding: DB
    abstract val contentViewLayout: Int

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
        super.onCreateView(inflater, container, savedInstanceState)
        logD(logTag, "onCreateView")
        dataBinding = DataBindingUtil.inflate(inflater, contentViewLayout, container, false)
        dataBinding.lifecycleOwner = this
        return dataBinding.root
    }

    @CallSuper
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        logD(logTag, "onActivityCreated")
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
}
