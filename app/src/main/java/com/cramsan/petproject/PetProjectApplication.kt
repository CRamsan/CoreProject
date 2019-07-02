package com.cramsan.petproject

import android.app.Application
import com.cramsan.petproject.appcore.framework.CoreFrameworkAPI
import com.cramsan.petproject.appcore.storage.implementation.ModelStoragePlatformInitializer

class PetProjectApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        CoreFrameworkAPI.init()
        CoreFrameworkAPI.initEventLogger()
        CoreFrameworkAPI.initThreadUtil()
        CoreFrameworkAPI.initHaltUtil()
        CoreFrameworkAPI.initModelStorage(ModelStoragePlatformInitializer(this))
    }
}