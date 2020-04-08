package com.cramsan.petproject

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
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
import com.cramsan.petproject.appcore.feedback.FeedbackManagerDAO
import com.cramsan.petproject.appcore.feedback.FeedbackManagerInterface
import com.cramsan.petproject.appcore.feedback.implementation.FeedbackManager
import com.cramsan.petproject.appcore.model.feedback.Feedback
import com.cramsan.petproject.appcore.provider.ModelProviderInterface
import com.cramsan.petproject.appcore.provider.implementation.ModelProvider
import com.cramsan.petproject.appcore.storage.ModelStorageInterface
import com.cramsan.petproject.appcore.storage.implementation.ModelStorage
import com.cramsan.petproject.appcore.storage.ModelStoragePlatformProvider
import com.cramsan.petproject.appcore.storage.implementation.ModelStorageAndroidProvider
import com.microsoft.appcenter.AppCenter
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.erased.bind
import org.kodein.di.erased.factory
import org.kodein.di.erased.instance
import org.kodein.di.erased.singleton
import org.kodein.di.newInstance

class PetProjectApplication : Application(), KodeinAware {

    private val eventLogger: EventLoggerInterface by instance()
    private val crashHandler: CrashHandlerInterface by instance()
    private val metrics: MetricsInterface by instance()

    override val kodein = Kodein.lazy {
        import(androidXModule(this@PetProjectApplication))

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
        bind<ModelStorageInterface>() with singleton {
            val context: Context = this@PetProjectApplication
            val modelStorageDAO = ModelStorageAndroidProvider(
                context
            ).provide()
            val modelStorage by kodein.newInstance {
                ModelStorage(modelStorageDAO,
                instance(),
                instance()) }
            modelStorage
        }
        bind<PreferencesInterface>() with singleton {
            val context = this@PetProjectApplication
            val preferences by kodein.newInstance {
                Preferences(PreferencesAndroid(context))
            }
            preferences
        }
        bind<ModelProviderInterface>() with singleton {
            val modelProvider by kodein.newInstance {
                ModelProvider(instance(),
                    instance(),
                    instance(),
                    instance()) }
            modelProvider
        }
        bind<FeedbackManagerInterface>() with singleton {
            class DummyDAO : FeedbackManagerDAO {
                override fun submitFeedback(feedback: Feedback) {
                    TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
                }
            }
            val feedbackManager by kodein.newInstance {
                FeedbackManager(DummyDAO(),
                    instance(),
                    instance())
            }
            feedbackManager
        }
        bind<ViewModelProvider>() with factory {
            owner: ViewModelStoreOwner -> ViewModelProvider(owner)
        }
    }

    override fun onCreate() {
        super.onCreate()
        eventLogger.log(Severity.INFO, "PetProjectApplication", "onCreate called")
        AppCenter.start(this, "1206f21f-1b20-483f-9385-9b8cbc0e504d")
        crashHandler.initialize()
        metrics.initialize()
    }
}
