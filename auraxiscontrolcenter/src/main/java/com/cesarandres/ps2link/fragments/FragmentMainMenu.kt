package com.cesarandres.ps2link.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.app.PendingIntent
import android.content.ComponentName
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.IntentSender.SendIntentException
import android.content.ServiceConnection
import android.os.AsyncTask
import android.os.Bundle
import android.os.IBinder
import android.os.RemoteException
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ImageView.ScaleType
import android.widget.Toast

import com.android.vending.billing.IInAppBillingService
import com.cesarandres.ps2link.ApplicationPS2Link
import com.cesarandres.ps2link.ApplicationPS2Link.ActivityMode
import com.cesarandres.ps2link.R
import com.cesarandres.ps2link.base.BaseFragment
import com.cesarandres.ps2link.dbg.DBGCensus.Namespace
import com.cesarandres.ps2link.module.BitmapWorkerTask

import org.json.JSONException
import org.json.JSONObject

import java.util.ArrayList
import android.*
import android.content.pm.*

import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment

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
            mCallbacks.onItemSelected(
                ActivityMode.ACTIVITY_PROFILE_LIST.toString(),
                null!!
            )
        }
        buttonServers.setOnClickListener {
            mCallbacks.onItemSelected(
                ActivityMode.ACTIVITY_SERVER_LIST.toString(),
                null!!
            )
        }
        buttonOutfit.setOnClickListener {
            mCallbacks.onItemSelected(
                ActivityMode.ACTIVITY_OUTFIT_LIST.toString(),
                null!!
            )
        }
        buttonNews.setOnClickListener {
            mCallbacks.onItemSelected(
                ActivityMode.ACTIVITY_LINK_MENU.toString(),
                null!!
            )
        }
        buttonTwitter.setOnClickListener {
            mCallbacks.onItemSelected(
                ActivityMode.ACTIVITY_TWITTER.toString(),
                null!!
            )
        }
        buttonReddit.setOnClickListener {
            mCallbacks.onItemSelected(
                ActivityMode.ACTIVITY_REDDIT.toString(),
                null!!
            )
        }
        buttonAbout.setOnClickListener {
            mCallbacks.onItemSelected(
                ActivityMode.ACTIVITY_ABOUT.toString(),
                null!!
            )
        }
        buttonDonate.setOnClickListener {
            if (mService != null) {
                val task = DownloadDonationsTask()
                setCurrentTask(task)
                task.execute()
            }
        }
        buttonSettings.setOnClickListener {
            mCallbacks.onItemSelected(
                ActivityMode.ACTIVITY_SETTINGS.toString(),
                null!!
            )
        }

        val buttonPS2Background = activity!!.findViewById<View>(R.id.buttonPS2) as ImageButton
        buttonPS2Background.setOnClickListener {
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
                mService = null
            }

            override fun onServiceConnected(
                name: ComponentName,
                service: IBinder
            ) {
                mService = IInAppBillingService.Stub.asInterface(service)
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
                val settings = activity!!.getSharedPreferences("PREFERENCES", 0)
                mCallbacks.onItemSelected(
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
                val settings = activity!!.getSharedPreferences("PREFERENCES", 0)
                mCallbacks.onItemSelected(
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
        if (mService != null) {
            activity!!.unbindService(this!!.mServiceConn!!)
        }
    }

    fun promptForPermissions() {
        if (ContextCompat.checkSelfPermission(
                activity!!,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                activity!!,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                0
            )
        }
    }

    /**
     * @author cramsan
     */
    private inner class DownloadDonationsTask : AsyncTask<Void, Void, ArrayList<String>>() {

        /* (non-Javadoc)
         * @see android.os.AsyncTask#doInBackground(java.lang.Object[])
         */
        override fun doInBackground(vararg params: Void): ArrayList<String>? {
            var response = -1
            val ownedItems: Bundle

            try {
                ownedItems = mService!!.getPurchases(3, activity!!.packageName, "inapp", null)
                response = ownedItems.getInt("RESPONSE_CODE")
            } catch (e1: RemoteException) {
                e1.printStackTrace()
                return null
            }

            if (response == 0) {
                val purchaseDataList = ownedItems.getStringArrayList("INAPP_PURCHASE_DATA_LIST")

                for (i in purchaseDataList!!.indices) {
                    val purchaseData = purchaseDataList[i]
                    try {
                        val ownedObject: JSONObject
                        ownedObject = JSONObject(purchaseData)
                        val token = ownedObject.getString("purchaseToken")
                        response = mService!!.consumePurchase(3, activity!!.packageName, token)
                    } catch (e: JSONException) {
                        e.printStackTrace()
                        return null
                    } catch (e: RemoteException) {
                        e.printStackTrace()
                        return null
                    }

                }
            }

            val skuList = ArrayList<String>()
            skuList.add("item_donation_1")
            skuList.add("item_donation_2")
            skuList.add("item_donation_3")
            skuList.add("item_donation_4")
            val querySkus = Bundle()
            querySkus.putStringArrayList("ITEM_ID_LIST", skuList)
            val skuDetails: Bundle
            try {
                skuDetails = mService!!.getSkuDetails(3, activity!!.packageName, "inapp", querySkus)
                response = skuDetails.getInt("RESPONSE_CODE")
                return if (response == 0) {
                    skuDetails.getStringArrayList("DETAILS_LIST")
                } else {
                    null
                }
            } catch (e: RemoteException) {
                e.printStackTrace()
                return null
            }

        }

        /* (non-Javadoc)
         * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
         */
        override fun onPostExecute(result: ArrayList<String>?) {
            if (result != null) {
                if (result.size > 0) {
                    val newFragment = DonationsDialogFragment()
                    if (!newFragment.setResponseList(result)) {
                        Toast.makeText(
                            activity,
                            activity!!.resources.getString(R.string.toast_error_server_error),
                            Toast.LENGTH_LONG
                        ).show()
                        return
                    }
                    val manager = activity!!.supportFragmentManager
                    if (manager != null) {
                        newFragment.show(manager, "donations")
                    } else {
                        Toast.makeText(
                            activity,
                            activity!!.resources.getString(R.string.toast_error_empty_response),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                } else {
                    Toast.makeText(
                        activity,
                        activity!!.resources.getString(R.string.toast_error_empty_response),
                        Toast.LENGTH_LONG
                    ).show()
                }
            } else {
                Toast.makeText(
                    activity,
                    activity!!.resources.getString(R.string.toast_error_response_error),
                    Toast.LENGTH_LONG
                ).show()
            }
        }

    }

    class DonationsDialogFragment : DialogFragment() {

        private var responseList: ArrayList<String>? = null
        private var displayData: Array<String?>? = null

        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
            val builder = AlertDialog.Builder(activity)
            builder.setTitle(R.string.text_choose_donation)
                .setItems(displayData, DialogInterface.OnClickListener { dialog, index ->
                    val thisResponse = responseList!![index]
                    val `object`: JSONObject
                    try {
                        `object` = JSONObject(thisResponse)
                        val sku = `object`.getString("productId")
                        val buyIntentBundle =
                            mService!!.getBuyIntent(3, activity!!.packageName, sku, "inapp", "")
                        val pendingIntent =
                            buyIntentBundle.getParcelable<PendingIntent>("BUY_INTENT")
                        activity!!.startIntentSenderForResult(
                            pendingIntent!!.intentSender,
                            1001, Intent(), Integer.valueOf(0)!!, Integer.valueOf(0)!!,
                            Integer.valueOf(0)!!
                        )
                        return@OnClickListener
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    } catch (e: RemoteException) {
                        e.printStackTrace()
                    } catch (e: SendIntentException) {
                        e.printStackTrace()
                    }

                    Toast.makeText(
                        activity,
                        activity!!.resources.getString(R.string.toast_error_error_sending),
                        Toast.LENGTH_LONG
                    ).show()
                    return@OnClickListener
                })
            retainInstance = true
            return builder.create()
        }

        fun setResponseList(responseList: ArrayList<String>): Boolean {
            this.displayData = arrayOfNulls(responseList.size)
            for (i in responseList.indices) {
                val thisResponse = responseList[i]
                try {
                    val `object` = JSONObject(thisResponse)
                    val sku = `object`.getString("title")
                    displayData!![i] = sku
                } catch (e: JSONException) {
                    e.printStackTrace()
                    return false
                }

            }
            this.responseList = responseList
            return true
        }
    }

    companion object {

        private var mService: IInAppBillingService? = null
    }
}
