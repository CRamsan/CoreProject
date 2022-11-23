package com.cramsan.framework.preferences.implementation

import com.cramsan.framework.preferences.PreferencesTest
import kotlinx.coroutines.ExperimentalCoroutinesApi

/**
 * @Author cramsan
 * @created 1/16/2021
 */
@OptIn(ExperimentalCoroutinesApi::class)
class PreferencesImplTest : PreferencesTest() {

    override fun setupTest() {
        val preferencesDelegate = InMemoryPreferencesDelegate()
        preferences = PreferencesImpl(preferencesDelegate)
    }
}
