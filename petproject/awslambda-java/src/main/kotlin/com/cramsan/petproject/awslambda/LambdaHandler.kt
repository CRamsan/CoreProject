package com.cramsan.petproject.awslambda

import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.RequestHandler

class LambdaHandler : RequestHandler<Map<String, Any>, String> {
    override fun handleRequest(input: Map<String, Any>, context: Context): String {
        return "{}"
    }
}
