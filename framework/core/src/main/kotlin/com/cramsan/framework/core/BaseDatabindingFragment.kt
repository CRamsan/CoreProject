package com.cramsan.framework.core

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

abstract class BaseDatabindingFragment<VM : BaseViewModel, DB : ViewDataBinding> : BaseFragment() {

    abstract val viewModel: VM
    protected lateinit var dataBinding: DB
    abstract val contentViewLayout: Int

    @CallSuper
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        dataBinding = DataBindingUtil.inflate(inflater, contentViewLayout, container, false)
        dataBinding.lifecycleOwner = this
        return dataBinding.root
    }
}
