package com.cramsan.ps2link.appcore.repository

import com.cramsan.framework.core.DispatcherProvider
import com.cramsan.framework.remoteconfig.RemoteConfig
import com.cramsan.framework.test.TestBase
import com.cramsan.ps2link.appcore.twitter.TwitterClient
import com.cramsan.ps2link.remoteconfig.RemoteConfigData
import io.mockk.impl.annotations.MockK

class TwitterRepositoryImplTest : TestBase() {

    @MockK
    lateinit var twitterRepository: TwitterRepository

    @MockK
    lateinit var twitterClient: TwitterClient

    @MockK
    lateinit var remoteConfig: RemoteConfig<RemoteConfigData>

    @MockK
    lateinit var testDispatcherProvider: DispatcherProvider

    override fun setupTest() {
        twitterRepository = TwitterRepositoryImpl(
            twitterClient,
            remoteConfig,
            testDispatcherProvider,
        )
    }
}
