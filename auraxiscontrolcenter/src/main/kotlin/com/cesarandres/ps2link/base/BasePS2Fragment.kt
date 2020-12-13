package com.cesarandres.ps2link.base

import android.app.Activity
import android.os.AsyncTask
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.ToggleButton
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.test.espresso.idling.CountingIdlingResource
import com.android.volley.RequestQueue
import com.android.volley.toolbox.ImageLoader
import com.cesarandres.ps2link.ActivityContainer
import com.cesarandres.ps2link.ApplicationPS2Link
import com.cesarandres.ps2link.R
import com.cramsan.framework.core.BaseFragment
import com.cramsan.framework.core.BaseViewModel
import com.cramsan.framework.logging.EventLoggerInterface
import com.cramsan.framework.logging.Severity
import com.cramsan.framework.metrics.MetricsInterface
import com.cramsan.ps2link.appcore.DBGServiceClientImpl
import javax.inject.Inject
import org.kodein.di.DIAware
import org.kodein.di.instance

/**
 * All behavior that is shared across all fragments should also be implemented
 * here.
 */

/**
 * @author cramsan
 */
abstract class BasePS2Fragment<VM : BaseViewModel, DB : ViewDataBinding> : BaseFragment<VM, DB>()  {

}
