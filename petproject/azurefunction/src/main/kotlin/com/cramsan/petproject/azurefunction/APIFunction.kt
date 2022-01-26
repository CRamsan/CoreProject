@file:Suppress("UNUSED_PARAMETER")

package com.cramsan.petproject.azurefunction

import com.cramsan.petproject.appcore.storage.Plant
import com.fasterxml.jackson.databind.ObjectMapper
import com.microsoft.azure.functions.ExecutionContext
import com.microsoft.azure.functions.HttpMethod
import com.microsoft.azure.functions.HttpRequestMessage
import com.microsoft.azure.functions.HttpResponseMessage
import com.microsoft.azure.functions.HttpStatus
import com.microsoft.azure.functions.annotation.AuthorizationLevel
import com.microsoft.azure.functions.annotation.BindingName
import com.microsoft.azure.functions.annotation.FunctionName
import com.microsoft.azure.functions.annotation.HttpTrigger

/**
 * This class is the entry point for the Azure function.
 */
class APIFunction(
    dependenciesConfig: DependenciesConfig = DependenciesConfig(),
) {

    private val modelStorage = dependenciesConfig.modelStorage

    @FunctionName("plants")
    @Suppress("UndocumentedPublicFunction")
    fun plants(
        @HttpTrigger(
            name = "req",
            methods = [HttpMethod.GET],
            authLevel = AuthorizationLevel.FUNCTION,
            route = "plants"
        ) request: HttpRequestMessage<String?>,
        context: ExecutionContext
    ): HttpResponseMessage {
        val body: List<Plant> = modelStorage.getPlants()
        val bodyString = mapper.writeValueAsString(body)
        return request.createResponseBuilder(HttpStatus.OK)
            .header("Content-Type", "application/json")
            .body(bodyString)
            .build()
    }

    @FunctionName("mainnames")
    @Suppress("UndocumentedPublicFunction")
    fun mainNames(
        @HttpTrigger(
            name = "req",
            methods = [HttpMethod.GET],
            authLevel = AuthorizationLevel.FUNCTION,
            route = "name/main"
        ) request: HttpRequestMessage<String?>,
        context: ExecutionContext
    ): HttpResponseMessage {
        val body = modelStorage.getPlantsMainName()
        val bodyString = mapper.writeValueAsString(body)
        return request.createResponseBuilder(HttpStatus.OK)
            .header("Content-Type", "application/json")
            .body(bodyString)
            .build()
    }

    @FunctionName("commonNames")
    @Suppress("UndocumentedPublicFunction")
    fun commonNames(
        @HttpTrigger(
            name = "req",
            methods = [HttpMethod.GET],
            authLevel = AuthorizationLevel.FUNCTION,
            route = "name/common/{plantId}"
        ) request: HttpRequestMessage<String?>,
        @BindingName("plantId") plantId: Long,
        context: ExecutionContext
    ): HttpResponseMessage {
        val list = modelStorage.getPlantsCommonNames()
        val body = list.filter { it.plantId == plantId }
        val bodyString = mapper.writeValueAsString(body)
        return request.createResponseBuilder(HttpStatus.OK)
            .header("Content-Type", "application/json")
            .body(bodyString)
            .build()
    }

    @FunctionName("familiy")
    @Suppress("UndocumentedPublicFunction")
    fun familiy(
        @HttpTrigger(
            name = "req",
            methods = [HttpMethod.GET],
            authLevel = AuthorizationLevel.FUNCTION,
            route = "family/{plantId}"
        ) request: HttpRequestMessage<String?>,
        @BindingName("plantId") plantId: Long,
        context: ExecutionContext
    ): HttpResponseMessage {
        val list = modelStorage.getPlantsFamily()
        val body = list.first { it.plantId == plantId }
        val bodyString = mapper.writeValueAsString(body)
        return request.createResponseBuilder(HttpStatus.OK)
            .header("Content-Type", "application/json")
            .body(bodyString)
            .build()
    }

    @FunctionName("description")
    @Suppress("UndocumentedPublicFunction")
    fun description(
        @HttpTrigger(
            name = "req",
            methods = [HttpMethod.GET],
            authLevel = AuthorizationLevel.FUNCTION,
            route = "description/{plantId}/{animalType}"
        ) request: HttpRequestMessage<String?>,
        @BindingName("plantId") plantId: Long,
        @BindingName("animalType") animalType: Int,
        context: ExecutionContext
    ): HttpResponseMessage {
        val list = modelStorage.getDescription()
        val body = list.first { it.plantId == plantId && it.animalId.ordinal == animalType }
        val bodyString = mapper.writeValueAsString(body)
        return request.createResponseBuilder(HttpStatus.OK)
            .header("Content-Type", "application/json")
            .body(bodyString)
            .build()
    }

    @FunctionName("toxicities")
    @Suppress("UndocumentedPublicFunction")
    fun toxicities(
        @HttpTrigger(
            name = "req",
            methods = [HttpMethod.GET],
            authLevel = AuthorizationLevel.FUNCTION,
            route = "toxicity"
        ) request: HttpRequestMessage<String?>,
        context: ExecutionContext
    ): HttpResponseMessage {
        val body = modelStorage.getToxicity()
        val bodyString = mapper.writeValueAsString(body)
        return request.createResponseBuilder(HttpStatus.OK)
            .header("Content-Type", "application/json")
            .body(bodyString)
            .build()
    }

    companion object {
        private val mapper = ObjectMapper()
    }
}
