package com.cesarandres.ps2link.base

import android.app.Activity
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.ToggleButton

import androidx.fragment.app.Fragment
import com.android.volley.RequestQueue
import com.android.volley.toolbox.ImageLoader

import com.cesarandres.ps2link.ActivityContainer
import com.cesarandres.ps2link.ApplicationPS2Link
import com.cesarandres.ps2link.R
import com.cesarandres.ps2link.dbg.DBGCensus
import com.cramsan.framework.logging.EventLoggerInterface
import com.cramsan.framework.logging.Severity

import com.cramsan.framework.metrics.MetricsInterface
import org.kodein.di.KodeinAware
import org.kodein.di.erased.instance

/**
 * This class extends fragment to add the support for a callback. All the
 * fragments should extend this class instead of the standard Fragment class.
 * All behavior that is shared across all fragments should also be implemented
 * here.
 */

/**
 * @author cramsan
 */
abstract class BaseFragment : Fragment(), KodeinAware {
    protected var mCallbacks: FragmentCallbacks? = null
    protected lateinit var fragmentTitle: Button
    protected lateinit var fragmentProgress: ProgressBar
    protected lateinit var fragmentUpdate: ImageButton
    protected lateinit var fragmentShowOffline: ToggleButton
    protected lateinit var fragmentAdd: ImageButton
    protected lateinit var fragmentStar: ToggleButton
    protected lateinit var fragmentAppend: ToggleButton
    protected lateinit var fragmentMyWeapons: ToggleButton
    private var currentTask: AsyncTask<*, *, *>? = null

    override val kodein by lazy { (requireActivity().application as ApplicationPS2Link).kodein }
    protected val eventLogger: EventLoggerInterface by instance()
    protected val volley: RequestQueue by instance()
    protected val dbgCensus: DBGCensus by instance()
    protected val metrics: MetricsInterface by instance()
    protected val imageLoader: ImageLoader by instance()

    /**
     * @return the ActivityContainer object that this class belongs to
     */
    protected val activityContainer: ActivityContainer
        get() = (activity as ActivityContainer?)!!

    /*
     * (non-Javadoc)
     *
     * @see android.support.v4.app.Fragment#onAttach(android.app.Activity)
     */
    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
        eventLogger.log(Severity.INFO, TAG, "OnAttach")
        check(activity is FragmentCallbacks) { "Activity must implement fragment's callbacks." }
        mCallbacks = activity
    }

    /*
     * (non-Javadoc)
     *
     * @see android.support.v4.app.Fragment#onCreate(android.os.Bundle)
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        eventLogger.log(Severity.INFO, TAG, "OnCreate")
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater,
     * android.view.ViewGroup, android.os.Bundle)
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        eventLogger.log(Severity.INFO, TAG, "OnCreateView")
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    /*
     * (non-Javadoc)
     *
     * @see android.support.v4.app.Fragment#onActivityCreated(android.os.Bundle)
     */
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        eventLogger.log(Severity.INFO, TAG, "OnActivityCreated")

        this.fragmentTitle = activity!!.findViewById<View>(R.id.buttonFragmentTitle) as Button
        this.fragmentProgress =
            activity!!.findViewById<View>(R.id.progressBarFragmentTitleLoading) as ProgressBar
        this.fragmentUpdate =
            activity!!.findViewById<View>(R.id.buttonFragmentUpdate) as ImageButton
        this.fragmentShowOffline =
            activity!!.findViewById<View>(R.id.toggleButtonShowOffline) as ToggleButton
        this.fragmentAdd = activity!!.findViewById<View>(R.id.buttonFragmentAdd) as ImageButton
        this.fragmentStar =
            activity!!.findViewById<View>(R.id.toggleButtonFragmentStar) as ToggleButton
        this.fragmentAppend =
            activity!!.findViewById<View>(R.id.toggleButtonFragmentAppend) as ToggleButton
        this.fragmentMyWeapons =
            activity!!.findViewById<View>(R.id.toggleButtonWeapons) as ToggleButton
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * android.support.v4.app.Fragment#onViewStateRestored(android.os.Bundle)
     */
    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        eventLogger.log(Severity.INFO, TAG, "OnViewStateRestored")
    }

    /*
     * (non-Javadoc)
     *
     * @see android.support.v4.app.Fragment#onStart()
     */
    override fun onStart() {
        super.onStart()
        eventLogger.log(Severity.INFO, TAG, "OnStart")
    }

    /*
     * (non-Javadoc)
     *
     * @see android.support.v4.app.Fragment#onResume()
     */
    override fun onResume() {
        super.onResume()
        eventLogger.log(Severity.INFO, TAG, "OnResume")
    }

    /*
     * (non-Javadoc)
     *
     * @see android.support.v4.app.Fragment#onPause()
     */
    override fun onPause() {
        super.onPause()
        eventLogger.log(Severity.INFO, TAG, "OnPause")
    }

    /*
     * (non-Javadoc)
     *
     * @see android.support.v4.app.Fragment#onStop()
     */
    override fun onStop() {
        super.onStop()
        eventLogger.log(Severity.INFO, TAG, "OnStop")
        // When a fragment is stopped all tasks should be cancelled
        volley.cancelAll(this)
        if (currentTask != null) {
            currentTask!!.cancel(true)
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see android.support.v4.app.Fragment#onDestroyView()
     */
    override fun onDestroyView() {
        super.onDestroyView()
        eventLogger.log(Severity.INFO, TAG, "OnDestroyView")
    }

    /*
     * (non-Javadoc)
     *
     * @see android.support.v4.app.Fragment#onDestroy()
     */
    override fun onDestroy() {
        super.onDestroy()
        eventLogger.log(Severity.INFO, TAG, "OnDestroy")
    }

    /*
     * (non-Javadoc)
     *
     * @see android.support.v4.app.Fragment#onDetach()
     */
    override fun onDetach() {
        super.onDetach()
        eventLogger.log(Severity.INFO, TAG, "OnDetach")
        mCallbacks = null
    }

    /**
     * @param enabled if set to true, the progress view is displayed and the update
     * view is hidden. If set to false, the opposite will happen
     */
    fun setProgressButton(enabled: Boolean) {
        if (enabled) {
            this.fragmentUpdate.visibility = View.GONE
            this.fragmentProgress.visibility = View.VISIBLE
        } else {
            this.fragmentUpdate.visibility = View.VISIBLE
            this.fragmentProgress.visibility = View.GONE
        }
    }

    /**
     * @param currentTask new ASyncTask to be attached to this fragment. If a task is
     * already attached, the old one is cancelled and the new one is
     * set
     */
    fun setCurrentTask(currentTask: AsyncTask<*, *, *>) {
        if (this.currentTask != null) {
            this.currentTask!!.cancel(true)
        }
        this.currentTask = currentTask
    }

    /**
     * If there is a current task set, cancel it.
     */
    fun cancelCurrentTask() {
        if (this.currentTask != null) {
            this.currentTask!!.cancel(true)
        }
    }

    /**
     * This interface is used to send actions from the fragment back to the
     * activity that is attached to
     */
    interface FragmentCallbacks {
        fun onItemSelected(id: String, args: Array<String?>)
    }

    companion object {
        private const val TAG = "BaserFragment"
    }
}
