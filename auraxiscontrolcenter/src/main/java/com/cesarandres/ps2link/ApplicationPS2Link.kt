package com.cesarandres.ps2link

import android.app.Application
import android.graphics.Bitmap
import androidx.test.espresso.idling.CountingIdlingResource
import com.android.volley.RequestQueue
import com.android.volley.toolbox.ImageLoader
import com.android.volley.toolbox.Volley
import com.cesarandres.ps2link.dbg.DBGCensus
import com.cesarandres.ps2link.dbg.volley.BitmapLruCache
import com.cesarandres.ps2link.module.CacheManager
import com.cramsan.framework.assert.AssertUtilInterface
import com.cramsan.framework.assert.implementation.AssertUtil
import com.cramsan.framework.crashehandler.CrashHandlerInterface
import com.cramsan.framework.crashehandler.implementation.AppCenterCrashHandler
import com.cramsan.framework.crashehandler.implementation.CrashHandler
import com.cramsan.framework.halt.HaltUtilInterface
import com.cramsan.framework.halt.implementation.HaltUtil
import com.cramsan.framework.halt.implementation.HaltUtilAndroid
import com.cramsan.framework.logging.EventLoggerInterface
import com.cramsan.framework.logging.Severity
import com.cramsan.framework.logging.implementation.EventLogger
import com.cramsan.framework.logging.implementation.LoggerAndroid
import com.cramsan.framework.metrics.MetricsInterface
import com.cramsan.framework.metrics.implementation.AppCenterMetrics
import com.cramsan.framework.metrics.implementation.Metrics
import com.cramsan.framework.preferences.PreferencesInterface
import com.cramsan.framework.preferences.implementation.Preferences
import com.cramsan.framework.preferences.implementation.PreferencesAndroid
import com.cramsan.framework.thread.ThreadUtilInterface
import com.cramsan.framework.thread.implementation.ThreadUtil
import com.cramsan.framework.thread.implementation.ThreadUtilAndroid
import com.microsoft.appcenter.AppCenter
import java.util.Locale
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.erased.bind
import org.kodein.di.erased.instance
import org.kodein.di.erased.singleton
import org.kodein.di.newInstance
import java.util.Optional

class ApplicationPS2Link : Application(), KodeinAware {

    private val eventLogger: EventLoggerInterface by instance()
    private val crashHandler: CrashHandlerInterface by instance()
    private val cacheManager: CacheManager by instance()
    private val metrics: MetricsInterface by instance()
    private val dbgCensus: DBGCensus by instance()
    val idlingResource: CountingIdlingResource by instance()

    override val kodein = Kodein.lazy {
        import(androidXModule(this@ApplicationPS2Link))

        bind<CrashHandlerInterface>() with singleton {
            CrashHandler(AppCenterCrashHandler())
        }
        bind<MetricsInterface>() with singleton {
            Metrics(AppCenterMetrics())
        }
        bind<EventLoggerInterface>() with singleton {
            val severity: Severity = when (BuildConfig.DEBUG) {
                true -> Severity.DEBUG
                false -> Severity.INFO
            }
            EventLogger(severity, LoggerAndroid())
        }
        bind<HaltUtilInterface>() with singleton {
            HaltUtil(HaltUtilAndroid())
        }
        bind<ThreadUtilInterface>() with singleton {
            val threadUtil by kodein.newInstance { ThreadUtil(ThreadUtilAndroid(instance())) }
            threadUtil
        }
        bind<AssertUtilInterface>() with singleton {
            val assertUtil by kodein.newInstance { AssertUtil(BuildConfig.DEBUG, instance(), instance()) }
            assertUtil
        }
        bind<PreferencesInterface>() with singleton {
            val context = this@ApplicationPS2Link
            val preferences by kodein.newInstance {
                Preferences(PreferencesAndroid(context))
            }
            preferences
        }
        bind<RequestQueue>() with singleton {
            Volley.newRequestQueue(this@ApplicationPS2Link)
        }
        bind<ImageLoader>() with singleton {
            val loader by kodein.newInstance {
                ImageLoader(instance(), BitmapLruCache())
            }
            loader
        }
        bind<CacheManager>() with singleton {
            CacheManager(this@ApplicationPS2Link)
        }
        bind<DBGCensus>() with singleton {
            DBGCensus(this@ApplicationPS2Link)
        }
        bind<CountingIdlingResource>() with singleton {
            CountingIdlingResource(TAG)
        }
    }

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
        metrics.log(TAG, "Application Started")
        GlobalScope.launch {
            if (cacheManager.getCacheSize() > CacheManager.MAX_CACHE_SIZE) {
                cacheManager.clearCache()
            }
        }

        val lang = Locale.getDefault().language
        dbgCensus.currentLang = DBGCensus.CensusLang.EN
        for (clang in DBGCensus.CensusLang.values()) {
            if (lang.equals(clang.name, ignoreCase = true)) {
                dbgCensus.currentLang = clang
                metrics.log(TAG, "Language Set", mapOf("Lang" to clang.name))
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
