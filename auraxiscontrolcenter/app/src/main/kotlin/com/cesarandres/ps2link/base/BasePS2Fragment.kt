package com.cesarandres.ps2link.base

import com.cramsan.framework.core.BaseAndroidViewModel
import com.cramsan.framework.core.BaseViewModelFragment
import com.cramsan.ps2link.appcore.census.DBGServiceClient
import javax.inject.Inject

/**
 * All behavior that is shared across all fragments should also be implemented
 * here.
 */

/**
 * @author cramsan
 */
abstract class BasePS2Fragment<VM : BaseAndroidViewModel> : BaseViewModelFragment<VM>() {

    @Inject
    protected lateinit var dbgCensus: DBGServiceClient
}
