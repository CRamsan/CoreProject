package com.cesarandres.ps2link

import android.R
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Rect
import android.os.Build
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.View
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiObjectNotFoundException
import androidx.test.uiautomator.UiSelector

private const val PERMISSIONS_DIALOG_DELAY = 3000
private const val GRANT_BUTTON_INDEX = 0

fun allowPermissionsIfNeeded(permissionNeeded: String) {
    try {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !hasNeededPermission(permissionNeeded)) {
            sleep(PERMISSIONS_DIALOG_DELAY.toLong())
            val device = UiDevice.getInstance(getInstrumentation())
            val allowPermissions = device.findObject(
                UiSelector()
                    .clickable(true)
                    .checkable(false)
                    .index(GRANT_BUTTON_INDEX)
            )
            if (allowPermissions.exists()) {
                allowPermissions.click()
            }
        }
    } catch (e: UiObjectNotFoundException) {
        println("There is no permissions dialog to interact with")
    }
}

private fun hasNeededPermission(permissionNeeded: String): Boolean {
    val context: Context = getInstrumentation().context
    val permissionStatus = ContextCompat.checkSelfPermission(context, permissionNeeded)
    return permissionStatus == PackageManager.PERMISSION_GRANTED
}

private fun sleep(millis: Long) {
    try {
        Thread.sleep(millis)
    } catch (e: InterruptedException) {
        throw RuntimeException("Cannot execute Thread.sleep()")
    }
}

fun clearPreferences(context: Context) {
    val sharedPrefs: SharedPreferences = context.getSharedPreferences("PREFERENCES", 0)
    val editor: SharedPreferences.Editor = sharedPrefs.edit()
    editor.clear()
    editor.commit()
}

fun clearAppData(context: Context) {
    context.cacheDir.deleteRecursively()
    context.dataDir.deleteRecursively()
    context.codeCacheDir.deleteRecursively()
    context.externalCacheDir?.deleteRecursively()
    context.noBackupFilesDir?.deleteRecursively()
    context.filesDir.deleteRecursively()
}

fun getVisibilityAmount(view: View): Int {
    val visibleParts = Rect()
    val visibleAtAll: Boolean = view.getGlobalVisibleRect(visibleParts)
    if (!visibleAtAll) {
        return 0
    }

    val screen: Rect = getScreenWithoutStatusBarActionBar(view)

    var viewHeight =
        if (view.getHeight() > screen.height()) screen.height().toFloat() else view.getHeight().toFloat()
    var viewWidth =
        if (view.getWidth() > screen.width()) screen.width().toFloat() else view.getWidth().toFloat()

    if (Build.VERSION.SDK_INT >= 11) { // For API level 11 and above, factor in the View's scaleX and scaleY properties.
        viewHeight =
            Math.min(view.getHeight() * view.getScaleY(), screen.height().toFloat())
        viewWidth = Math.min(view.getWidth() * view.getScaleX(), screen.width().toFloat())
    }

    val maxArea = viewHeight * viewWidth.toDouble()
    val visibleArea = visibleParts.height() * visibleParts.width().toDouble()
    val displayedPercentage = (visibleArea / maxArea * 100).toInt()

    return displayedPercentage
}

@Suppress("DEPRECATION")
private fun getScreenWithoutStatusBarActionBar(view: View): Rect {
    val m = DisplayMetrics()
    (view.context.getSystemService(Context.WINDOW_SERVICE) as WindowManager)
        .defaultDisplay
        .getMetrics(m)
    // Get status bar height
    val resourceId =
        view.context.resources.getIdentifier("status_bar_height", "dimen", "android")
    val statusBarHeight =
        if (resourceId > 0) view.context.resources.getDimensionPixelSize(resourceId) else 0
    // Get action bar height
    val tv = TypedValue()
    val actionBarHeight = if (view.context.theme.resolveAttribute(R.attr.actionBarSize, tv, true)
    ) TypedValue.complexToDimensionPixelSize(
        tv.data,
        view.context.resources.displayMetrics
    ) else 0
    return Rect(
        0,
        0,
        m.widthPixels,
        m.heightPixels - (statusBarHeight + actionBarHeight)
    )
}
