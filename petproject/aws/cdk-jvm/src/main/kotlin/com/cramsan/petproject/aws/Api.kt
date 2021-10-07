package com.cramsan.petproject.aws

import com.cramsan.petproject.awslambda.common.ANIMAL_TYPE_PARAM
import com.cramsan.petproject.awslambda.common.COMMON_PATH
import com.cramsan.petproject.awslambda.common.DESCRIPTIONS_PATH
import com.cramsan.petproject.awslambda.common.FAMILY_PATH
import com.cramsan.petproject.awslambda.common.MAIN_PATH
import com.cramsan.petproject.awslambda.common.NAME_PATH
import com.cramsan.petproject.awslambda.common.PLANTS_PATH
import com.cramsan.petproject.awslambda.common.PLANT_ID_PARAM
import com.cramsan.petproject.awslambda.common.TOXICITIES_PATH
import software.amazon.awscdk.core.Construct
import software.amazon.awscdk.core.Resource
import software.amazon.awscdk.services.apigateway.LambdaIntegration
import software.amazon.awscdk.services.apigateway.RestApi
import software.amazon.awscdk.services.lambda.IFunction

class Api(scope: software.constructs.Construct, id: String, lambda: IFunction) : Construct(scope, id) {
    private val plantsApi: Resource
    private val commonNamesApi: Resource
    private val mainNamesApi: Resource
    private val familiesApi: Resource
    private val toxicitiesApi: Resource
    private val descriptionsApi: Resource

    init {
        val api = RestApi.Builder.create(this, "Api")
            .restApiName("SafeForMyPets API")
            .build()
        val templates = mapOf("application/json" to "{\"statusCode\":\"200\"}")
        val integration = LambdaIntegration.Builder.create(lambda)
            .requestTemplates(templates)
            .build()
        plantsApi = api.root.addResource(PLANTS_PATH).addMethod(GET_METHOD, integration)

        val names = api.root.addResource(NAME_PATH)
        val commonNames = names.addResource(COMMON_PATH)
        commonNamesApi = commonNames.addResource("{$PLANT_ID_PARAM}").addMethod(GET_METHOD, integration)
        mainNamesApi = names.addResource(MAIN_PATH).addMethod(GET_METHOD, integration)

        val families = api.root.addResource(FAMILY_PATH)
        familiesApi = families.addResource("{$PLANT_ID_PARAM}").addMethod(GET_METHOD, integration)

        val descriptions = api.root.addResource(DESCRIPTIONS_PATH)
        val descriptionsPlantId = descriptions.addResource("{$PLANT_ID_PARAM}")
        descriptionsApi = descriptionsPlantId.addResource("{$ANIMAL_TYPE_PARAM}").addMethod(GET_METHOD, integration)

        toxicitiesApi = api.root.addResource(TOXICITIES_PATH).addMethod(GET_METHOD, integration)
    }
}

const val GET_METHOD = "GET"
