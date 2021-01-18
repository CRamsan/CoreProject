package com.cramsan.ps2link.appcore

import com.cramsan.framework.test.TestBase
import com.cramsan.ps2link.appcore.dbg.DBGCensus
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.mockk.impl.annotations.MockK

class DBGServiceClientImplTest : TestBase() {

    @MockK
    lateinit var http: HttpClient

    lateinit var dbgServiceClient: DBGServiceClient

    override fun setupTest() {
        super.setupTest()
        dbgServiceClient = DBGServiceClientImpl(DBGCensus(), http)
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