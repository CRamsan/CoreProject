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

class APIFunction {

    @FunctionName("plants")
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
        private val dependenciesConfig = DependenciesConfig()
        val modelStorage = dependenciesConfig.modelStorage
        val mapper = ObjectMapper()
    }
}
