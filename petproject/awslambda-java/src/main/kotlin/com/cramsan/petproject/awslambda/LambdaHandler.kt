package com.cramsan.petproject.awslambda

import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.RequestHandler
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent

class LambdaHandler : RequestHandler<Map<String, Any>, APIGatewayProxyResponseEvent> {
    override fun handleRequest(input: Map<String, Any>, context: Context): APIGatewayProxyResponseEvent {
        return APIGatewayProxyResponseEvent()
            .withStatusCode(200)
            .withIsBase64Encoded(false)
            .withBody(input.toString())
    }
}
