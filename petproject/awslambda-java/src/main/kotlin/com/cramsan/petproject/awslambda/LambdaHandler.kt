package com.cramsan.petproject.awslambda

import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.RequestHandler
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent
import com.cramsan.framework.logging.logD
import com.cramsan.framework.logging.logI
import com.cramsan.framework.logging.logW
import com.cramsan.petproject.appcore.storage.implementation.ModelStorage
import com.cramsan.petproject.awslambda.common.Paths
import com.cramsan.petproject.awslambda.common.SystemProperties

class LambdaHandler : RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    lateinit var modelStorage: ModelStorage
    var initalized = false

    override fun handleRequest(
        input: APIGatewayProxyRequestEvent,
        context: Context
    ): APIGatewayProxyResponseEvent {
        if (!initalized) {
            configure()
        }

        logI(TAG, "Starting execution")
        logD(TAG, "resources = ${input.resource}")

        return try {
            when (Paths.fromString(input.resource)) {
                Paths.PLANT_NAME -> handlePlantsApi(input)
                Paths.MAIN_NAMES -> handleMainNamesApi(input)
                Paths.COMMON_NAMES -> handleCommonNamesApi(input)
                Paths.FAMILIES -> handleFamiliesApi(input)
                Paths.DESCRIPTIONS -> handleDescriptionsApi(input)
                Paths.TOXICITIES -> handleToxicitiesApi(input)
                null -> APIGatewayProxyResponseEvent()
                    .withStatusCode(400)
            }
        } catch (throwable: Throwable) {
            logW(TAG, "Unexpected exception: ${throwable.message}", throwable)
            APIGatewayProxyResponseEvent()
                .withStatusCode(500)
        }
    }

    private fun handleToxicitiesApi(input: APIGatewayProxyRequestEvent): APIGatewayProxyResponseEvent {
        val body = modelStorage.getToxicity()
        return APIGatewayProxyResponseEvent()
            .withBody(body.toString())
            .withStatusCode(200)
    }

    private fun handleDescriptionsApi(input: APIGatewayProxyRequestEvent): APIGatewayProxyResponseEvent {
        return APIGatewayProxyResponseEvent().withStatusCode(200)
    }

    private fun handleFamiliesApi(input: APIGatewayProxyRequestEvent): APIGatewayProxyResponseEvent {
        return APIGatewayProxyResponseEvent().withStatusCode(200)
    }

    private fun handleCommonNamesApi(input: APIGatewayProxyRequestEvent): APIGatewayProxyResponseEvent {
        return APIGatewayProxyResponseEvent().withStatusCode(200)
    }

    private fun handleMainNamesApi(input: APIGatewayProxyRequestEvent): APIGatewayProxyResponseEvent {
        return APIGatewayProxyResponseEvent().withStatusCode(200)
    }

    private fun handlePlantsApi(input: APIGatewayProxyRequestEvent): APIGatewayProxyResponseEvent {
        val body = modelStorage.getPlants()
        return APIGatewayProxyResponseEvent()
            .withBody(body.toString())
            .withStatusCode(200)
    }

    private fun configure() {
        val dependencies = DependenciesConfig(
            System.getenv(SystemProperties.PLANT_NAME) ?: "",
            System.getenv(SystemProperties.COMMON_NAMES) ?: "",
            System.getenv(SystemProperties.MAIN_NAMES) ?: "",
            System.getenv(SystemProperties.FAMILIES) ?: "",
            System.getenv(SystemProperties.TOXICITIES) ?: "",
            System.getenv(SystemProperties.DESCRIPTIONS) ?: "",
        )

        logI(TAG, "Configuring handler")
        modelStorage = dependencies.modelStorage
        initalized = true
    }

    companion object {
        const val TAG = "LambdaHandler"
    }
}
