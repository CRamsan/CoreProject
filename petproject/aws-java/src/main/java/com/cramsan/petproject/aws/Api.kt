package com.cramsan.petproject.aws

import software.amazon.awscdk.core.Construct
import software.amazon.awscdk.services.apigateway.LambdaIntegration
import software.amazon.awscdk.services.apigateway.LambdaIntegrationOptions
import software.amazon.awscdk.services.apigateway.RestApi
import software.amazon.awscdk.services.apigateway.RestApiProps
import software.amazon.awscdk.services.lambda.IFunction

class Api(scope: software.constructs.Construct, id: String, lambda: IFunction) : Construct(scope, id) {
    init {
        val api = RestApi(
            this, "Api",
            RestApiProps.Builder()
                .restApiName("SafeForMyPets API")
                .build()
        )

        val integration = LambdaIntegration(lambda, LambdaIntegrationOptions.builder().build())
        api.root.addMethod("GET", integration)
    }
}
