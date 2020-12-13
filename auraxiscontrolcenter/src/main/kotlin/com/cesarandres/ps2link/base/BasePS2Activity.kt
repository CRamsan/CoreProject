package com.cesarandres.ps2link.base

import android.app.Activity
import android.content.Intent
import android.view.View
import android.widget.ImageView
import android.widget.ImageView.ScaleType
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import com.android.volley.RequestQueue
import com.android.volley.toolbox.ImageLoader
import com.cesarandres.ps2link.ApplicationPS2Link
import com.cesarandres.ps2link.ApplicationPS2Link.WallPaperMode
import com.cesarandres.ps2link.R
import com.cesarandres.ps2link.module.BitmapWorkerTask
import com.cramsan.framework.core.BaseActivity
import com.cramsan.framework.core.BaseFragment
import com.cramsan.framework.core.BaseViewModel
import com.cramsan.framework.logging.EventLoggerInterface
import com.cramsan.framework.logging.Severity
import com.cramsan.framework.metrics.MetricsInterface
import com.cramsan.ps2link.appcore.DBGServiceClientImpl
import javax.inject.Inject
import org.json.JSONException
import org.json.JSONObject
import org.kodein.di.DIAware
import org.kodein.di.instance

/**
 * This fragment handles setting the background for all activities.
 */
abstract class BasePS2Activity<T : BaseViewModel> : BaseActivity<T>() {

    @Inject
    protected lateinit var metrics: MetricsInterface

    @Inject
    protected lateinit var dbgCensus: DBGServiceClientImpl

}
