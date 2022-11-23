package com.cramsan.framework.preferences.implementation

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.cramsan.framework.preferences.PreferencesDelegateTest
import io.mockk.every
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import org.robolectric.RobolectricTestRunner
import kotlin.test.Test

/**
 * @Author cramsan
 * @created 1/16/2021
 */

@RunWith(RobolectricTestRunner::class)
@OptIn(ExperimentalCoroutinesApi::class)
class AndroidPreferencesDelegateTest : PreferencesDelegateTest() {

    override fun setupTest() {
        val mockContext: Context = ApplicationProvider.getApplicationContext()
        preferencesDelegate = PreferencesAndroid(mockContext)
    }
}
