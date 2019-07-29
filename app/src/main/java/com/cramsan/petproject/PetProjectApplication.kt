package com.cramsan.petproject

import android.app.Application
import com.cramsan.framework.logging.Severity
import com.cramsan.framework.logging.classTag
import com.cramsan.petproject.appcore.framework.CoreFrameworkAPI
import com.cramsan.petproject.appcore.provider.implementation.ModelProviderPlatformInitializer
import com.cramsan.petproject.appcore.storage.implementation.ModelStoragePlatformInitializer

class PetProjectApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        CoreFrameworkAPI.init()
        if (BuildConfig.DEBUG) {
            CoreFrameworkAPI.initEventLogger(Severity.DEBUG)
        } else {
            CoreFrameworkAPI.initEventLogger(Severity.INFO)
        }
        CoreFrameworkAPI.initThreadUtil()
        CoreFrameworkAPI.initHaltUtil()
        CoreFrameworkAPI.initModelStorage(ModelStoragePlatformInitializer(this))
        CoreFrameworkAPI.initModelProvider(ModelProviderPlatformInitializer())
        CoreFrameworkAPI.eventLogger.log(Severity.INFO, classTag(), "onCreate called")
    }
}