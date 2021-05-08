package com.cesarandres.ps2link.base

import androidx.databinding.ViewDataBinding
import com.cramsan.framework.core.BaseDatabindingFragment
import com.cramsan.framework.core.BaseViewModel
import com.cramsan.ps2link.appcore.census.DBGServiceClient
import javax.inject.Inject

/**
 * All behavior that is shared across all fragments should also be implemented
 * here.
 */

/**
 * @author cramsan
 */
abstract class BasePS2Fragment<VM : BaseViewModel, DB : ViewDataBinding> : BaseDatabindingFragment<VM, DB>() {

    @Inject
    protected lateinit var dbgCensus: DBGServiceClient
}
