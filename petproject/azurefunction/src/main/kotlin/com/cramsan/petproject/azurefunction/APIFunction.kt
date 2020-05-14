package com.cramsan.petproject.azurefunction

import com.cramsan.petproject.appcore.storage.Plant
import com.fasterxml.jackson.databind.ObjectMapper
import com.microsoft.azure.functions.ExecutionContext
import com.microsoft.azure.functions.HttpMethod
import com.microsoft.azure.functions.HttpRequestMessage
import com.microsoft.azure.functions.HttpResponseMessage
import com.microsoft.azure.functions.HttpStatus
import com.microsoft.azure.functions.annotation.AuthorizationLevel
import com.microsoft.azure.functions.annotation.FunctionName
import com.microsoft.azure.functions.annotation.HttpTrigger

class APIFunction {

    @FunctionName("plants")
    fun plants(
        @HttpTrigger(
            name = "req",
            methods = [HttpMethod.GET],
            authLevel = AuthorizationLevel.FUNCTION
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
            authLevel = AuthorizationLevel.FUNCTION
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

    @FunctionName("commonnames")
    fun commonNames(
        @HttpTrigger(
            name = "req",
            methods = [HttpMethod.GET],
            authLevel = AuthorizationLevel.FUNCTION
        ) request: HttpRequestMessage<String?>,
        context: ExecutionContext
    ): HttpResponseMessage {
        val body = modelStorage.getPlantsCommonNames()
        val bodyString = mapper.writeValueAsString(body)
        return request.createResponseBuilder(HttpStatus.OK)
            .header("Content-Type", "application/json")
            .body(bodyString)
            .build()
    }

    @FunctionName("families")
    fun families(
        @HttpTrigger(
            name = "req",
            methods = [HttpMethod.GET],
            authLevel = AuthorizationLevel.FUNCTION
        ) request: HttpRequestMessage<String?>,
        context: ExecutionContext
    ): HttpResponseMessage {
        val body = modelStorage.getPlantsFamily()
        val bodyString = mapper.writeValueAsString(body)
        return request.createResponseBuilder(HttpStatus.OK)
            .header("Content-Type", "application/json")
            .body(bodyString)
            .build()
    }

    @FunctionName("descriptions")
    fun descriptions(
        @HttpTrigger(
            name = "req",
            methods = [HttpMethod.GET],
            authLevel = AuthorizationLevel.FUNCTION
        ) request: HttpRequestMessage<String?>,
        context: ExecutionContext
    ): HttpResponseMessage {
        val body = modelStorage.getDescription()
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
            authLevel = AuthorizationLevel.FUNCTION
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
