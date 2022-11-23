package com.cramsan.framework.preferences.implementation

import com.cramsan.framework.preferences.PreferencesDelegateTest
import kotlinx.coroutines.ExperimentalCoroutinesApi

/**
 * @Author cramsan
 * @created 1/16/2021
 */
@OptIn(ExperimentalCoroutinesApi::class)
class InMemoryPreferencesDelegateTest : PreferencesDelegateTest() {

    override fun setupTest() {
        preferencesDelegate = InMemoryPreferencesDelegate()
    }
}
