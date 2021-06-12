package com.cramsan.ps2link.appcore

import com.cramsan.framework.test.TestBase
import com.cramsan.ps2link.appcore.census.DBGCensus
import com.cramsan.ps2link.appcore.census.DBGServiceClient
import com.cramsan.ps2link.appcore.census.DBGServiceClientImpl
import com.cramsan.ps2link.appcore.network.HttpClient
import io.mockk.impl.annotations.MockK
import kotlinx.datetime.Clock

class DBGServiceClientImplTest : TestBase() {

    @MockK
    lateinit var http: HttpClient

    @MockK
    lateinit var clock: Clock

    lateinit var dbgServiceClient: DBGServiceClient

    override fun setupTest() {
        super.setupTest()
        dbgServiceClient = DBGServiceClientImpl(DBGCensus(), http, clock)
    }

    fun `test searching for a profile by name`() = runBlockingTest {
        /*
        val response: HttpResponse = mockk()
        every { response.status } returns HttpStatusCode.OK
        coEvery { http.get<HttpResponse>(any<Url>()) } returns response

        dbgServiceClient.getProfiles("test", Namespace.PS2PC, CensusLang.ES)
         */
    }
}
