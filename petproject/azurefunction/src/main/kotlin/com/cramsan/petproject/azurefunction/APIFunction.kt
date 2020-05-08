package com.cramsan.petproject.azurefunction

import com.microsoft.azure.functions.*
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
        val body = modelStorage.getPlants()
        return request.createResponseBuilder(HttpStatus.OK).body(body).build()
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
        val body = modelStorage.getPlantsFamily()
        return request.createResponseBuilder(HttpStatus.OK).body(body).build()
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
        val body = modelStorage.getPlantsFamily()
        return request.createResponseBuilder(HttpStatus.OK).body(body).build()
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
        return request.createResponseBuilder(HttpStatus.OK).body(body).build()
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
        val body = modelStorage.getPlantsFamily()
        return request.createResponseBuilder(HttpStatus.OK).body(body).build()
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
        val body = modelStorage.getPlantsFamily()
        return request.createResponseBuilder(HttpStatus.OK).body(body).build()
    }

    companion object {
        private val dependenciesConfig = DependenciesConfig()
        val modelStorage = dependenciesConfig.modelStorage
    }
}