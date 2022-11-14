package com.cramsan.framework.core

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.google.android.material.appbar.MaterialToolbar

/**
 * This class extends [BaseViewModelFragment] with the capabilities to render a View based screen. The [viewModel]
 * is a required field. If this class does not need a viewModel, then [NoopViewModel] can be used.
 * Since compose if the future for Android UI development, [ComposeBaseFragment] should be used for
 * any new screens.
 *
 * The [contentViewLayout] is the layout that will be inflated for this fragment. The [dataBinding]
 * should be the correct type for the layout, otherwise the fragment will throw an exception and the
 * app will most probably crash.
 */
abstract class BaseDatabindingFragment<VM : BaseViewModel, DB : ViewDataBinding> : BaseViewModelFragment<VM>() {

    /**
     * DataBinding instance that will be used to access the layout [contentViewLayout].
     */
    protected lateinit var dataBinding: DB
        private set

    /**
     * Id of the [MaterialToolbar] in the layout [contentViewLayout].
     */
    protected abstract val contentViewLayout: Int

    @CallSuper
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        dataBinding = DataBindingUtil.inflate(inflater, contentViewLayout, container, false)
        dataBinding.lifecycleOwner = this
        return dataBinding.root
    }
}
