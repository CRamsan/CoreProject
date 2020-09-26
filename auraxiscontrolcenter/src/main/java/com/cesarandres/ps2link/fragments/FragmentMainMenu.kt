package com.cesarandres.ps2link.fragments

import android.Manifest
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.IBinder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ImageView.ScaleType
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.cesarandres.ps2link.ApplicationPS2Link
import com.cesarandres.ps2link.ApplicationPS2Link.ActivityMode
import com.cesarandres.ps2link.R
import com.cesarandres.ps2link.base.BaseFragment
import com.cesarandres.ps2link.dbg.DBGCensus.Namespace
import com.cesarandres.ps2link.module.BitmapWorkerTask

/**
 * This fragment is very static, it has all the buttons for most of the main
 * fragments. It will also display the Preferred Character and Preferred Outfit
 * buttons if those have been set.
 */
class FragmentMainMenu : BaseFragment() {
    private var mServiceConn: ServiceConnection? = null

    /*
     * (non-Javadoc)
     *
     * @see com.cesarandres.ps2link.base.BaseFragment#onCreateView(android.view.
     * LayoutInflater, android.view.ViewGroup, android.os.Bundle)
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main_menu, container, false)
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.cesarandres.ps2link.base.BaseFragment#onActivityCreated(android.os
     * .Bundle)
     */
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        this.fragmentTitle.text = getString(R.string.app_name_capital)

        val buttonCharacters = activity!!.findViewById<View>(R.id.buttonCharacters) as Button
        val buttonServers = activity!!.findViewById<View>(R.id.buttonServers) as Button
        val buttonOutfit = activity!!.findViewById<View>(R.id.buttonOutfit) as Button
        val buttonNews = activity!!.findViewById<View>(R.id.buttonNews) as Button
        val buttonTwitter = activity!!.findViewById<View>(R.id.buttonTwitter) as Button
        val buttonReddit = activity!!.findViewById<View>(R.id.buttonRedditFragment) as Button
        val buttonDonate = activity!!.findViewById<View>(R.id.buttonDonate) as Button
        val buttonAbout = activity!!.findViewById<View>(R.id.buttonAbout) as Button
        val buttonSettings = activity!!.findViewById<View>(R.id.buttonSettings) as Button

        buttonCharacters.setOnClickListener {
            metrics.log(TAG, "Open Profile")
            mCallbacks!!.onItemSelected(
                ActivityMode.ACTIVITY_PROFILE_LIST.toString(),
                emptyArray()
            )
        }
        buttonServers.setOnClickListener {
            metrics.log(TAG, "Open Servers")
            mCallbacks!!.onItemSelected(
                ActivityMode.ACTIVITY_SERVER_LIST.toString(),
                emptyArray()
            )
        }
        buttonOutfit.setOnClickListener {
            metrics.log(TAG, "Open Outfit")
            mCallbacks!!.onItemSelected(
                ActivityMode.ACTIVITY_OUTFIT_LIST.toString(),
                emptyArray()
            )
        }
        buttonNews.setOnClickListener {
            metrics.log(TAG, "Open News")
            mCallbacks!!.onItemSelected(
                ActivityMode.ACTIVITY_LINK_MENU.toString(),
                emptyArray()
            )
        }
        buttonTwitter.setOnClickListener {
            metrics.log(TAG, "Open Twitter")
            mCallbacks!!.onItemSelected(
                ActivityMode.ACTIVITY_TWITTER.toString(),
                emptyArray()
            )
        }
        buttonReddit.setOnClickListener {
            metrics.log(TAG, "Open Reddit")
            mCallbacks!!.onItemSelected(
                ActivityMode.ACTIVITY_REDDIT.toString(),
                emptyArray()
            )
        }
        buttonAbout.setOnClickListener {
            metrics.log(TAG, "Open About")
            mCallbacks!!.onItemSelected(
                ActivityMode.ACTIVITY_ABOUT.toString(),
                emptyArray()
            )
        }
        buttonDonate.setOnClickListener {
            metrics.log(TAG, "Open Donate")
        }
        buttonSettings.setOnClickListener {
            metrics.log(TAG, "Open Settings")
            mCallbacks!!.onItemSelected(
                ActivityMode.ACTIVITY_SETTINGS.toString(),
                emptyArray()
            )
        }

        val buttonPS2Background = activity!!.findViewById<View>(R.id.buttonPS2) as ImageButton
        buttonPS2Background.setOnClickListener {
            metrics.log(TAG, "Select Default Background")
            val background = activity!!.findViewById<View>(R.id.imageViewBackground) as ImageView
            background.setImageResource(R.drawable.ps2_activity_background)
            background.scaleType = ScaleType.FIT_START

            val settings = activity!!.getSharedPreferences("PREFERENCES", 0)
            val editor = settings.edit()
            editor.putString("preferedWallpaper", ApplicationPS2Link.WallPaperMode.PS2.toString())
            editor.commit()
        }

        val buttonNCBackground = activity!!.findViewById<View>(R.id.buttonNC) as ImageButton
        buttonNCBackground.setOnClickListener {
            metrics.log(TAG, "Select NC Background")
            promptForPermissions()
            val task = BitmapWorkerTask(
                activity!!.findViewById<View>(R.id.imageViewBackground) as ImageView,
                activity!!
            )
            task.execute("nc_wallpaper.jpg")
            val settings = activity!!.getSharedPreferences("PREFERENCES", 0)
            val editor = settings.edit()
            editor.putString("preferedWallpaper", ApplicationPS2Link.WallPaperMode.NC.toString())
            editor.commit()
        }

        val buttonTRBackground = activity!!.findViewById<View>(R.id.buttonTR) as ImageButton
        buttonTRBackground.setOnClickListener {
            metrics.log(TAG, "Select TR Background")
            promptForPermissions()
            val task = BitmapWorkerTask(
                activity!!.findViewById<View>(R.id.imageViewBackground) as ImageView,
                activity!!
            )
            task.execute("tr_wallpaper.jpg")
            val settings = activity!!.getSharedPreferences("PREFERENCES", 0)
            val editor = settings.edit()
            editor.putString("preferedWallpaper", ApplicationPS2Link.WallPaperMode.TR.toString())
            editor.commit()
        }

        val buttonVSBackground = activity!!.findViewById<View>(R.id.buttonVS) as ImageButton
        buttonVSBackground.setOnClickListener {
            metrics.log(TAG, "Select VS Background")
            promptForPermissions()
            val task = BitmapWorkerTask(
                activity!!.findViewById<View>(R.id.imageViewBackground) as ImageView,
                activity!!
            )
            task.execute("vs_wallpaper.jpg")
            val settings = activity!!.getSharedPreferences("PREFERENCES", 0)
            val editor = settings.edit()
            editor.putString("preferedWallpaper", ApplicationPS2Link.WallPaperMode.VS.toString())
            editor.commit()
        }

        mServiceConn = object : ServiceConnection {
            override fun onServiceDisconnected(name: ComponentName) {
            }

            override fun onServiceConnected(
                name: ComponentName,
                service: IBinder
            ) {
            }
        }

        val serviceIntent = Intent("com.android.vending.billing.InAppBillingService.BIND")
        serviceIntent.setPackage("com.android.vending")
        activity!!.bindService(serviceIntent, mServiceConn as ServiceConnection, Context.BIND_AUTO_CREATE)
    }

    /*
     * (non-Javadoc)
     *
     * @see com.cesarandres.ps2link.base.BaseFragment#onResume()
     */
    override fun onResume() {
        super.onResume()
        checkPreferedButtons()
    }

    /**
     * This function will check the preferences to see if any profile or outfit
     * has been set as preferred. If any has been set then the respective button
     * will be displayed, they will be hidden otherwise.
     */
    fun checkPreferedButtons() {
        val settings = activity!!.getSharedPreferences("PREFERENCES", 0)

        val preferedProfileId = settings.getString("preferedProfile", "")
        val preferedProfileName = settings.getString("preferedProfileName", "")
        val buttonPreferedProfile =
            activity!!.findViewById<View>(R.id.buttonPreferedProfile) as Button
        if (preferedProfileId != "") {
            buttonPreferedProfile.setOnClickListener {
                metrics.log(TAG, "Open Preferred Profile")
                val settings = activity!!.getSharedPreferences("PREFERENCES", 0)
                mCallbacks!!.onItemSelected(
                    ApplicationPS2Link.ActivityMode.ACTIVITY_PROFILE.toString(),
                    arrayOf(
                        settings.getString("preferedProfile", ""),
                        settings.getString("preferedProfileNamespace", Namespace.PS2PC.name)
                    )
                )
            }
            buttonPreferedProfile.text = preferedProfileName
            buttonPreferedProfile.visibility = View.VISIBLE
        } else {
            buttonPreferedProfile.visibility = View.GONE
        }

        val preferedOutfitId = settings.getString("preferedOutfit", "")
        val preferedOutfitName = settings.getString("preferedOutfitName", "")
        val buttonPreferedOutfit =
            activity!!.findViewById<View>(R.id.buttonPreferedOutfit) as Button
        if (preferedOutfitId != "") {

            buttonPreferedOutfit.setOnClickListener {
                metrics.log(TAG, "Open Preferred Outfit")
                val settings = activity!!.getSharedPreferences("PREFERENCES", 0)
                mCallbacks!!.onItemSelected(
                    ApplicationPS2Link.ActivityMode.ACTIVITY_MEMBER_LIST.toString(),
                    arrayOf(
                        settings.getString("preferedOutfit", ""),
                        settings.getString("preferedOutfitNamespace", Namespace.PS2PC.name)
                    )
                )
            }
            buttonPreferedOutfit.visibility = View.VISIBLE
            buttonPreferedOutfit.text = preferedOutfitName
        } else {
            buttonPreferedOutfit.visibility = View.GONE
        }
    }

    /* (non-Javadoc)
     * @see com.cesarandres.ps2link.base.BaseFragment#onDestroy()
     */
    override fun onDestroy() {
        super.onDestroy()
    }

    fun promptForPermissions() {
        if (ContextCompat.checkSelfPermission(activity!!, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                activity!!,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                0
            )
        }
    }

    companion object {
        const val TAG = "FragmentMainMenu"
    }
}
