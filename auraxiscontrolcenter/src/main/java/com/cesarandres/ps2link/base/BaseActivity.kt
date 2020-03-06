package com.cesarandres.ps2link.base

import android.app.Activity
import android.content.Intent
import android.view.View
import android.widget.ImageView
import android.widget.ImageView.ScaleType
import android.widget.Toast

import androidx.fragment.app.FragmentActivity

import com.cesarandres.ps2link.ApplicationPS2Link
import com.cesarandres.ps2link.ApplicationPS2Link.WallPaperMode
import com.cesarandres.ps2link.R
import com.cesarandres.ps2link.module.BitmapWorkerTask

import org.json.JSONException
import org.json.JSONObject

/**
 * This fragment handles setting the background for all activities.
 */
abstract class BaseActivity : FragmentActivity() {

    private var currentTask: BitmapWorkerTask? = null

    /*
     * (non-Javadoc)
     *
     * @see android.support.v4.app.FragmentActivity#onStart()
     */
    override fun onStart() {
        super.onStart()

    }

    /*
     * (non-Javadoc)
     *
     * @see android.support.v4.app.FragmentActivity#onResume()
     */
    override fun onResume() {
        super.onResume()

        // Read the current wallpaper from the settings
        val settings = getSharedPreferences("PREFERENCES", 0)
        val preferedWallpaper =
            settings.getString("preferedWallpaper", WallPaperMode.PS2.toString())
        ApplicationPS2Link.wallpaperMode = WallPaperMode.valueOf(preferedWallpaper!!)

        if (ApplicationPS2Link.wallpaperMode != WallPaperMode.PS2) {
            if (ApplicationPS2Link.background == null) {
                // If the wallpaper has been set to some faction specific image
                // but the image has not been loaded, we need to load it first
                val task = BitmapWorkerTask(
                    findViewById<View>(R.id.imageViewBackground) as ImageView,
                    this
                )
                if (currentTask != null) {
                    currentTask!!.cancel(true)
                }
                currentTask = task
                when (WallPaperMode.valueOf(preferedWallpaper)) {
                    ApplicationPS2Link.WallPaperMode.NC -> task.execute("nc_wallpaper.jpg")
                    ApplicationPS2Link.WallPaperMode.TR -> task.execute("tr_wallpaper.jpg")
                    ApplicationPS2Link.WallPaperMode.VS -> task.execute("vs_wallpaper.jpg")
                    else -> {
                    }
                }
            } else {
                // If the image has already been loaded, just apply it.
                val background = findViewById<View>(R.id.imageViewBackground) as ImageView
                background.setImageBitmap(ApplicationPS2Link.background)
                background.scaleType = ScaleType.CENTER_CROP
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1001) {
            //int responseCode = data.getIntExtra("RESPONSE_CODE", 0);
            val purchaseData = data!!.getStringExtra("INAPP_PURCHASE_DATA")
            //String dataSignature = data.getStringExtra("INAPP_DATA_SIGNATURE");

            if (resultCode == Activity.RESULT_OK) {
                try {
                    val jo = JSONObject(purchaseData!!)
                    //We can use this later
                    //String sku = jo.getString("productId");
                    Toast.makeText(
                        this,
                        resources.getString(R.string.text_thanks),
                        Toast.LENGTH_LONG
                    ).show()
                } catch (e: JSONException) {
                    Toast.makeText(
                        this,
                        resources.getString(R.string.text_thanks_failed),
                        Toast.LENGTH_LONG
                    ).show()
                    e.printStackTrace()
                }

            }
        } else {
            Toast.makeText(
                this,
                resources.getString(R.string.text_thanks_failed),
                Toast.LENGTH_LONG
            ).show()
        }
    }
}
