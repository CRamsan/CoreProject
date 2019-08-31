package com.cramsan.petproject.appcore.storage.implementation.azuresql

import com.cramsan.petproject.appcore.storage.ModelStorageDAO
import com.cramsan.petproject.appcore.storage.implementation.ModelStorageInitializer

expect class AzureSQLDAO(initializer: ModelStorageInitializer) : ModelStorageDAO
