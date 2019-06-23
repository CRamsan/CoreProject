package com.cramsan.petproject

import android.app.Application
import com.cramsan.petproject.appcore.framework.CoreFramework
import com.cramsan.petproject.appcore.storage.implementation.ModelStoragePlatformInitializer

class PetProjectApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        CoreFramework.initEventLogger()
        CoreFramework.initThreadUtil()
        CoreFramework.initModelStorage(ModelStoragePlatformInitializer(this))
    }
}