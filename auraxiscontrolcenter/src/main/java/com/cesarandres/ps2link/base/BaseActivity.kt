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
import com.cesarandres.ps2link.dbg.DBGCensus
import com.cesarandres.ps2link.module.BitmapWorkerTask
import com.cramsan.framework.logging.EventLoggerInterface
import com.cramsan.framework.logging.Severity
import com.cramsan.framework.metrics.MetricsInterface
import org.json.JSONException
import org.json.JSONObject
import org.kodein.di.KodeinAware
import org.kodein.di.erased.instance

/**
 * This fragment handles setting the background for all activities.
 */
abstract class BaseActivity : FragmentActivity(), KodeinAware {

    override val kodein by lazy { (application as ApplicationPS2Link).kodein }
    protected val eventLogger: EventLoggerInterface by instance()
    protected val metrics: MetricsInterface by instance()
    protected val volley: RequestQueue by instance()
    protected val dbgCensus: DBGCensus by instance()
    protected val imageLoader: ImageLoader by instance()

    private var currentTask: BitmapWorkerTask? = null

    /*
     * (non-Javadoc)
     *
     * @see android.support.v4.app.FragmentActivity#onStart()
     */
    override fun onStart() {
        super.onStart()
        eventLogger.log(Severity.INFO, "BaseActivity", "OnStart")
    }

    override fun onRestart() {
        super.onRestart()
        eventLogger.log(Severity.INFO, "BaseActivity", "OnRestart")
    }

    /*
     * (non-Javadoc)
     *
     * @see android.support.v4.app.FragmentActivity#onResume()
     */
    override fun onResume() {
        super.onResume()
        eventLogger.log(Severity.INFO, "BaseActivity", "OnResume")

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

    override fun onPause() {
        super.onPause()
        eventLogger.log(Severity.INFO, "BaseActivity", "onPause")
    }

    override fun onStop() {
        super.onStop()
        eventLogger.log(Severity.INFO, "BaseActivity", "onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        eventLogger.log(Severity.INFO, "BaseActivity", "onDestroy")
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        eventLogger.log(Severity.INFO, "BaseActivity", "OnActivityResult")

        if (requestCode == 1001) {
            // int responseCode = data.getIntExtra("RESPONSE_CODE", 0);
            val purchaseData = data!!.getStringExtra("INAPP_PURCHASE_DATA")
            // String dataSignature = data.getStringExtra("INAPP_DATA_SIGNATURE");

            if (resultCode == Activity.RESULT_OK) {
                try {
                    val jo = JSONObject(purchaseData!!)
                    // We can use this later
                    // String sku = jo.getString("productId");
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
