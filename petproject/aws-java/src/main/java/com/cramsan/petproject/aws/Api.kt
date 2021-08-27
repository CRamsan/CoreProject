package com.cramsan.petproject.aws

import software.amazon.awscdk.core.Construct
import software.amazon.awscdk.services.apigateway.LambdaIntegration
import software.amazon.awscdk.services.apigateway.LambdaIntegrationOptions
import software.amazon.awscdk.services.apigateway.RestApi
import software.amazon.awscdk.services.apigateway.RestApiProps
import software.amazon.awscdk.services.lambda.IFunction

class Api(scope: software.constructs.Construct, id: String, lambda: IFunction) : Construct(scope, id) {
    init {
        val api = RestApi.Builder.create(this, "Api")
            .restApiName("SafeForMyPets API")
            .build()
        val templates = mapOf("application/json" to "{\"statusCode\":\"200\"}")
        val integration = LambdaIntegration.Builder.create(lambda)
            .requestTemplates(templates)
            .build()
        api.root.addResource("test")
            .addMethod("GET", integration)
    }
}
