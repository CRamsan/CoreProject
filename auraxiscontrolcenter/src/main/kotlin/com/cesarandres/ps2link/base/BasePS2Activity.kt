package com.cesarandres.ps2link.base

import com.cramsan.framework.core.BaseActivity
import com.cramsan.framework.core.BaseViewModel
import com.cramsan.ps2link.appcore.DBGServiceClient
import javax.inject.Inject

/**
 * This fragment handles setting the background for all activities.
 */
abstract class BasePS2Activity<T : BaseViewModel> : BaseActivity<T>() {

    @Inject
    protected lateinit var dbgCensus: DBGServiceClient
}
