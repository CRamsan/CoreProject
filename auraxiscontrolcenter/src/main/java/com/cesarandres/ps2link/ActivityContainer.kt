package com.cesarandres.ps2link

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.ToggleButton
import androidx.fragment.app.FragmentTransaction
import com.cesarandres.ps2link.ApplicationPS2Link.ActivityMode
import com.cesarandres.ps2link.base.BaseActivity
import com.cesarandres.ps2link.base.BaseFragment
import com.cesarandres.ps2link.base.BaseFragment.FragmentCallbacks
import com.cesarandres.ps2link.fragments.FragmentAbout
import com.cesarandres.ps2link.fragments.FragmentAddOutfit
import com.cesarandres.ps2link.fragments.FragmentMainMenu
import com.cesarandres.ps2link.fragments.FragmentAddProfile
import com.cesarandres.ps2link.fragments.FragmentOutfitList
import com.cesarandres.ps2link.fragments.FragmentProfileList
import com.cesarandres.ps2link.fragments.FragmentServerList
import com.cesarandres.ps2link.fragments.FragmentSettings
import com.cesarandres.ps2link.fragments.FragmentTwitter
import com.cesarandres.ps2link.fragments.holders.FragmentOutfitPager
import com.cesarandres.ps2link.fragments.holders.FragmentProfilePager
import com.cesarandres.ps2link.fragments.holders.FragmentRedditPager
import com.cesarandres.ps2link.module.ObjectDataSource

/**
 * Class that will hold the current fragments. It behaves differently if it is
 * run on a table or a phone. On a phone, every time a new fragment needs to be
 * created, a new instance of this activity will be created. If this activity is
 * running on a table, this tablet will keep a main menu on the left side while
 * new fragments will be swapped on the right side.
 *
 *
 * This activity will also use the @activityMode variable to keep track of the
 * current fragment on top of the stack. This works correctly in phone mode, it
 * has not been tested in tablets yet.
 */
class ActivityContainer : BaseActivity(), FragmentCallbacks {

    protected lateinit var fragmentTitle: Button
    protected lateinit var fragmentProgress: ProgressBar
    protected lateinit var fragmentUpdate: ImageButton
    protected lateinit var fragmentShowOffline: ToggleButton
    protected lateinit var fragmentAdd: ImageButton
    protected lateinit var fragmentStar: ToggleButton
    protected lateinit var fragmentAppend: ToggleButton

    /**
     * @return current activity mode
     */
    /**
     * @param activityMode set the new mode for this activity
     */
    var activityMode: ActivityMode? = null
        set(activityMode) {
            field = activityMode
            if (activityMode == ActivityMode.ACTIVITY_MAIN_MENU) {
                this.fragmentTitle.setCompoundDrawables(null, null, null, null)
            } else {
                val drawable: Drawable?
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
                    drawable = getDrawable(R.drawable.icon_back)
                } else {
                    drawable = resources.getDrawable(R.drawable.icon_back)
                }
                this.fragmentTitle.setCompoundDrawablesWithIntrinsicBounds(
                    drawable,
                    null,
                    null,
                    null
                )
            }
        }
    /**
     * @return the ObjectDataSource that gives access to the local sqlite db.
     * You should not open or close this, those methods are tied to the
     * activity lyfecycle.
     */
    /**
     * @param data a new ObjectDataSource to be handled by this activity.
     */
    var data: ObjectDataSource? = null
    /**
     * @return if the activity is running on tablet mode
     */
    var isTablet = false
        private set

    /*
     * (non-Javadoc)
     *
     * @see android.support.v4.app.FragmentActivity#onCreate(android.os.Bundle)
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Load the UI
        setContentView(R.layout.activity_panel)

        // Set references to all the buttons in the action bar
        this.fragmentTitle = this.findViewById<View>(R.id.buttonFragmentTitle) as Button
        this.fragmentProgress =
            this.findViewById<View>(R.id.progressBarFragmentTitleLoading) as ProgressBar
        this.fragmentUpdate = this.findViewById<View>(R.id.buttonFragmentUpdate) as ImageButton
        this.fragmentShowOffline =
            this.findViewById<View>(R.id.toggleButtonShowOffline) as ToggleButton
        this.fragmentAdd = this.findViewById<View>(R.id.buttonFragmentAdd) as ImageButton
        this.fragmentStar = this.findViewById<View>(R.id.toggleButtonFragmentStar) as ToggleButton
        this.fragmentAppend =
            this.findViewById<View>(R.id.toggleButtonFragmentAppend) as ToggleButton

        // Check if any activity mode has been set, set it to Main Menu
        // otherwise
        val extras = intent.extras
        if (savedInstanceState != null) {
            activityMode =
                ActivityMode.valueOf(savedInstanceState.getString(ApplicationPS2Link.ACTIVITY_MODE_KEY)!!)
        } else if (extras != null) {
            val mode = extras.getString(ApplicationPS2Link.ACTIVITY_MODE_KEY)
            if (mode == null) {
                activityMode = ActivityMode.ACTIVITY_MAIN_MENU
            } else {
                activityMode = ActivityMode.valueOf(mode)
            }
        } else {
            activityMode = ActivityMode.ACTIVITY_MAIN_MENU
        }

        // Check if the second panel exists
        if (findViewById<View>(R.id.activityMainMenuFragment) != null) {
            isTablet = true
        }

        if (savedInstanceState == null) {
            // This will prevent to populate the second panel when starting in
            // main menu mode on a tablet
            if (!isTablet || activityMode != ActivityMode.ACTIVITY_MAIN_MENU) {
                val fragment = getFragmentByMode(activityMode!!)
                val transaction = supportFragmentManager.beginTransaction()
                transaction.add(R.id.activityFrameLayout, fragment!!)
                transaction.commit()
            }
        }

        this.fragmentTitle.setOnClickListener {
            upNavigation()
        }

        this.supportFragmentManager.addOnBackStackChangedListener {
            if (isTablet && supportFragmentManager.backStackEntryCount == 0) {
                // If the second panel is empty after popping a profile or
                // outfit pager, finish the activity
                if (activityMode == ActivityMode.ACTIVITY_PROFILE || activityMode == ActivityMode.ACTIVITY_MEMBER_LIST) {
                    finish()
                } else { // In any other case, we are back at the first
                    // activity, so just empty the second panel
                    activityMode = ActivityMode.ACTIVITY_MAIN_MENU
                    fragmentTitle.setText(R.string.app_name_capital)
                    clearActionBar()
                }
            }
        }

        // Open the database for all other fragments to use
        data = ObjectDataSource(this, dbgCensus)
        data!!.open()
    }

    public override fun onDestroy() {
        super.onDestroy()
        data!!.close()
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * android.support.v4.app.FragmentActivity#onSaveInstanceState(android.os
     * .Bundle)
     */
    public override fun onSaveInstanceState(savedInstanceState: Bundle) {
        super.onSaveInstanceState(savedInstanceState)
        savedInstanceState.putString(
            ApplicationPS2Link.ACTIVITY_MODE_KEY,
            activityMode!!.toString()
        )
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.cesarandres.ps2link.base.BaseFragment.FragmentCallbacks#onItemSelected
     * (java.lang.String, java.lang.String[])
     */
    override fun onItemSelected(id: String, args: Array<String?>) {
        metrics.log(TAG, "OnItemSelected", mapOf("Mode" to id))
        // Reset the database, this will also force some tasks to end
        val mode = ActivityMode.valueOf(id)
        // Seen as we can't have embedded fragments, we will create a new
        // activity if we want to open a profile or outfit pager. With all other
        // not nested fragments, we can just push a new fragment on top of the
        // stack
        if (isTablet && mode != ActivityMode.ACTIVITY_PROFILE && mode != ActivityMode.ACTIVITY_MEMBER_LIST) {
            // A main menu fragment never goes in the second panel
            if (mode == ActivityMode.ACTIVITY_MAIN_MENU) {
                return
            }

            val transaction: FragmentTransaction
            transaction = this.supportFragmentManager.beginTransaction()

            val newFragment = getFragmentByMode(mode)

            // Build the bundle from the argument list
            val bundle = Bundle()
            if (args != null && args.size > 0) {
                for (i in args.indices) {
                    bundle.putString("PARAM_$i", args[i])
                }
            }

            newFragment!!.arguments = bundle
            transaction.replace(R.id.activityFrameLayout, newFragment)
            transaction.addToBackStack(null)
            clearActionBar()
            transaction.commit()
            activityMode = mode
        } else {
            // On a non-tablet, a new activity gets created every time we need a
            // new fragment
            val newActivityClass = ActivityContainer::class.java
            val intent = Intent(this, newActivityClass)
            if (args != null && args.size > 0) {
                for (i in args.indices) {
                    intent.putExtra("PARAM_$i", args[i])
                }
            }
            intent.putExtra(ApplicationPS2Link.ACTIVITY_MODE_KEY, mode.toString())
            clearActionBar()
            startActivity(intent)
        }
    }

    /**
     * This method will enable all the title bar buttons as well as hide them.
     * This method should be called before showing a new fragment. Each new
     * fragment is in charge of showing the buttons they need.
     */
    private fun clearActionBar() {
        this.fragmentUpdate.isEnabled = true
        this.fragmentProgress.isEnabled = true
        this.fragmentShowOffline.isEnabled = true
        this.fragmentAdd.isEnabled = true
        this.fragmentStar.isEnabled = true
        this.fragmentAppend.isEnabled = true
        this.fragmentUpdate.visibility = View.GONE
        this.fragmentProgress.visibility = View.GONE
        this.fragmentShowOffline.visibility = View.GONE
        this.fragmentAdd.visibility = View.GONE
        this.fragmentStar.visibility = View.GONE
        this.fragmentAppend.visibility = View.GONE
    }

    /**
     * @param activityMode Activity mode
     * @return the fragment corresponding to the activity mode requested. If the
     * activity mode does not belong to any class, this method will
     * return null.
     */
    private fun getFragmentByMode(activityMode: ActivityMode): BaseFragment? {
        var newFragment: BaseFragment? = null
        when (activityMode) {
            ApplicationPS2Link.ActivityMode.ACTIVITY_ADD_OUTFIT -> newFragment = FragmentAddOutfit()
            ApplicationPS2Link.ActivityMode.ACTIVITY_ADD_PROFILE -> newFragment =
                FragmentAddProfile()
            ApplicationPS2Link.ActivityMode.ACTIVITY_MEMBER_LIST -> newFragment =
                FragmentOutfitPager()
            ApplicationPS2Link.ActivityMode.ACTIVITY_OUTFIT_LIST -> newFragment =
                FragmentOutfitList()
            ApplicationPS2Link.ActivityMode.ACTIVITY_PROFILE_LIST -> newFragment =
                FragmentProfileList()
            ApplicationPS2Link.ActivityMode.ACTIVITY_SERVER_LIST -> newFragment =
                FragmentServerList()
            ApplicationPS2Link.ActivityMode.ACTIVITY_TWITTER -> newFragment = FragmentTwitter()
            ApplicationPS2Link.ActivityMode.ACTIVITY_MAIN_MENU -> newFragment = FragmentMainMenu()
            ApplicationPS2Link.ActivityMode.ACTIVITY_PROFILE -> newFragment = FragmentProfilePager()
            ApplicationPS2Link.ActivityMode.ACTIVITY_REDDIT -> newFragment = FragmentRedditPager()
            ApplicationPS2Link.ActivityMode.ACTIVITY_ABOUT -> newFragment = FragmentAbout()
            ApplicationPS2Link.ActivityMode.ACTIVITY_SETTINGS -> newFragment = FragmentSettings()
            else -> {
            }
        }
        return newFragment
    }

    /**
     * If a main menu fragment can be located. It will refresh the state of the
     * preferred buttons for both profile and outfit
     */
    fun checkPreferedButtons() {
        val mainMenu = supportFragmentManager.findFragmentById(R.id.activityMainMenuFragment)
        if (mainMenu != null) {
            (mainMenu as FragmentMainMenu).checkPreferedButtons()
        }
    }

    /**
     * This should simulate the back button behavior. If we are on tablet or phone
     * mode, we will go back on different ways. On normal phone mode we end the activity,
     * if we are on tablet mode we will pop the top of the stack.
     */
    fun upNavigation() {
        metrics.log(TAG, "Up Navigation")
        if (this.activityMode != ActivityMode.ACTIVITY_MAIN_MENU) {
            if (isTablet) {
                if (supportFragmentManager.backStackEntryCount > 0) {
                    supportFragmentManager.popBackStack()

                    val currentFrag =
                        supportFragmentManager.findFragmentById(R.id.activityFrameLayout)
                    if (currentFrag != null) {
                        val removeTransaction = supportFragmentManager.beginTransaction()
                        removeTransaction.remove(currentFrag)
                        removeTransaction.commit()
                    }
                } else {
                    finish()
                }
            } else {
                finish()
            }
        }
    }

    companion object {
        const val TAG = "ActivityContainer"
    }
}
