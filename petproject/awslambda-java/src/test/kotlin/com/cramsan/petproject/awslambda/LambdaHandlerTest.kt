package com.cramsan.petproject.awslambda

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent
import com.cramsan.petproject.awslambda.common.PLANTS_PATH
import io.mockk.mockk
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class LambdaHandlerTest {
    @Before
    fun setUp() { }

    @Test
    fun simpleTest() {
        val input = APIGatewayProxyRequestEvent()
        input.resource = "/$PLANTS_PATH"

        val handler = LambdaHandler()
        val result = handler.handleRequest(input, mockk())
        assertEquals(500, result.statusCode)
    }
}
