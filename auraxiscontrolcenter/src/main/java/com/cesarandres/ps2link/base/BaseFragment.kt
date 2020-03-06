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

import com.cesarandres.ps2link.ActivityContainer
import com.cesarandres.ps2link.ApplicationPS2Link
import com.cesarandres.ps2link.R
import com.cesarandres.ps2link.dbg.util.Logger

/**
 * This class extends fragment to add the support for a callback. All the
 * fragments should extend this class instead of the standard Fragment class.
 * All behavior that is shared across all fragments should also be implemented
 * here.
 */

/**
 * @author cramsan
 */
abstract class BaseFragment : Fragment() {
    protected var mCallbacks = emptyCallbacks
    protected lateinit var fragmentTitle: Button
    protected lateinit var fragmentProgress: ProgressBar
    protected lateinit var fragmentUpdate: ImageButton
    protected lateinit var fragmentShowOffline: ToggleButton
    protected lateinit var fragmentAdd: ImageButton
    protected lateinit var fragmentStar: ToggleButton
    protected lateinit var fragmentAppend: ToggleButton
    protected lateinit var fragmentMyWeapons: ToggleButton
    private var currentTask: AsyncTask<*, *, *>? = null

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
        Logger.log(Log.INFO, this, "Fragment onAttach")
        super.onAttach(activity)
        check(activity is FragmentCallbacks) { "Activity must implement fragment's callbacks." }
        mCallbacks = activity
    }

    /*
     * (non-Javadoc)
     *
     * @see android.support.v4.app.Fragment#onCreate(android.os.Bundle)
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        Logger.log(Log.INFO, this, "Fragment onCreate")
        super.onCreate(savedInstanceState)
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
        Logger.log(Log.INFO, this, "Fragment onCreateView")
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    /*
     * (non-Javadoc)
     *
     * @see android.support.v4.app.Fragment#onActivityCreated(android.os.Bundle)
     */
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        Logger.log(Log.INFO, this, "Fragment onActivityCreated")
        super.onActivityCreated(savedInstanceState)

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
        Logger.log(Log.INFO, this, "Fragment onViewStateRestored")
        super.onViewStateRestored(savedInstanceState)
    }

    /*
     * (non-Javadoc)
     *
     * @see android.support.v4.app.Fragment#onStart()
     */
    override fun onStart() {
        Logger.log(Log.INFO, this, "Fragment onStart")
        super.onStart()
    }

    /*
     * (non-Javadoc)
     *
     * @see android.support.v4.app.Fragment#onResume()
     */
    override fun onResume() {
        Logger.log(Log.INFO, this, "Fragment onResume")
        super.onResume()
    }

    /*
     * (non-Javadoc)
     *
     * @see android.support.v4.app.Fragment#onPause()
     */
    override fun onPause() {
        Logger.log(Log.INFO, this, "Fragment onPause")
        super.onPause()
    }

    /*
     * (non-Javadoc)
     *
     * @see android.support.v4.app.Fragment#onStop()
     */
    override fun onStop() {
        Logger.log(Log.INFO, this, "Fragment onStop")
        super.onStop()
        // When a fragment is stopped all tasks should be cancelled
        ApplicationPS2Link.volley!!.cancelAll(this)
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
        Logger.log(Log.INFO, this, "Fragment onDestroyView")
        super.onDestroyView()
    }

    /*
     * (non-Javadoc)
     *
     * @see android.support.v4.app.Fragment#onDestroy()
     */
    override fun onDestroy() {
        Logger.log(Log.INFO, this, "Fragment onDestroy")
        super.onDestroy()
    }

    /*
     * (non-Javadoc)
     *
     * @see android.support.v4.app.Fragment#onDetach()
     */
    override fun onDetach() {
        Logger.log(Log.INFO, this, "Fragment onDetach")
        super.onDetach()
        mCallbacks = emptyCallbacks
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

        private val emptyCallbacks: FragmentCallbacks = object : FragmentCallbacks {
            override fun onItemSelected(id: String, args: Array<String?>) {
                Logger.log(
                    Log.WARN,
                    this,
                    "Item selected when no activity was set, this should never happen"
                )
            }
        }
    }
}
