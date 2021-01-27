package com.cesarandres.ps2link

import android.app.Application
import android.graphics.Bitmap
import com.cramsan.framework.assert.AssertUtilInterface
import com.cramsan.framework.crashehandler.CrashHandler
import com.cramsan.framework.halt.HaltUtil
import com.cramsan.framework.logging.EventLoggerInterface
import com.cramsan.framework.logging.Severity
import com.cramsan.framework.metrics.MetricsInterface
import com.cramsan.framework.metrics.logMetric
import com.cramsan.framework.thread.ThreadUtilInterface
import com.cramsan.ps2link.appcore.dbg.CensusLang
import com.microsoft.appcenter.AppCenter
import dagger.hilt.android.HiltAndroidApp
import java.util.Locale
import javax.inject.Inject

@HiltAndroidApp
class ApplicationPS2Link : Application() {

    @Inject
    lateinit var eventLogger: EventLoggerInterface

    @Inject
    lateinit var crashHandler: CrashHandler

    @Inject
    lateinit var metrics: MetricsInterface

    @Inject
    lateinit var threadUtil: ThreadUtilInterface

    @Inject
    lateinit var haltUtil: HaltUtil

    @Inject
    lateinit var assertUtil: AssertUtilInterface

    /*
     * (non-Javadoc)
     *
     * @see android.app.Application#onCreate()
     */
    override fun onCreate() {
        super.onCreate()
        eventLogger.log(Severity.INFO, TAG, "onCreate called")
        AppCenter.start(this, "2cbdd11d-e4ef-4626-b09f-2a7deb82664a")
        crashHandler.initialize()
        metrics.initialize()
        // TODO: Hilt is not injecting the AssertUtils. This is a workaround for that
        assertUtil = PS2ApplicationModule.provideAssertUtil(eventLogger, haltUtil)
        logMetric(TAG, "Application Started")

        val lang = Locale.getDefault().language
        for (clang in CensusLang.values()) {
            if (lang.equals(clang.name, ignoreCase = true)) {
                //  TODO: Set this property app-wide
                //   dbgCensus.currentLang = clang
                logMetric(TAG, "Language Set", mapOf("Lang" to clang.name))
            }
        }
    }

    /**
     * This enum holds all the different activity modes, there is one for each
     * main fragment that is used.
     */
    enum class ActivityMode {
        ACTIVITY_ADD_OUTFIT,
        ACTIVITY_ADD_PROFILE,
        ACTIVITY_MEMBER_LIST,
        ACTIVITY_OUTFIT_LIST,
        ACTIVITY_PROFILE,
        ACTIVITY_PROFILE_LIST,
        ACTIVITY_SERVER_LIST,
        ACTIVITY_TWITTER,
        ACTIVITY_LINK_MENU,
        ACTIVITY_MAIN_MENU,
        ACTIVITY_REDDIT,
        ACTIVITY_ABOUT,
        ACTIVITY_SETTINGS
    }

    /**
     * This enum holds the reference for all four background types.
     */
    enum class WallPaperMode {
        PS2, NC, TR, VS
    }

    companion object {

        internal val ACTIVITY_MODE_KEY = "activity_mode"
        /**
         * @return the bitmap holding the data for the background for all activities
         */
        /**
         * @param background bitmap that will be used as background for all activities
         */
        var background: Bitmap? = null
        /**
         * @return the current Wallpaper mode
         */
        /**
         * @param wallpaper the wallpaper mode that matches the current wallpaper. This
         * method should only be called after the wallpaper has been set
         */
        var wallpaperMode = WallPaperMode.PS2

        const val TAG = "ApplicationPS2Link"
    }
}
