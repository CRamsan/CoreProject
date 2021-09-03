package com.cramsan.petproject.aws

import com.cramsan.petproject.awslambda.common.SystemProperties
import software.amazon.awscdk.core.Construct
import software.amazon.awscdk.core.Duration
import software.amazon.awscdk.core.Stack
import software.amazon.awscdk.core.StackProps
import software.amazon.awscdk.services.lambda.Code
import software.amazon.awscdk.services.lambda.Function
import software.amazon.awscdk.services.lambda.Runtime

class TestAwsJavaStack @JvmOverloads constructor(
    scope: Construct?,
    id: String?,
    props: StackProps? = null
) : Stack(scope, id, props) {
    init {
        val storage = Storage(this, "Storage")

        val function = Function.Builder.create(this, "api-lambda")
            .code(Code.fromAsset("../awslambda-java/build/libs/awslambda-java-all.jar"))
            .handler("com.cramsan.petproject.awslambda.LambdaHandler")
            .timeout(Duration.seconds(30))
            .memorySize(1024)
            .runtime(Runtime.JAVA_11)
            .environment(
                mapOf(
                    SystemProperties.PLANT_NAME to storage.plantsTable.tableName,
                    SystemProperties.COMMON_NAMES to storage.commonNamesTable.tableName,
                    SystemProperties.MAIN_NAMES to storage.mainNamesTable.tableName,
                    SystemProperties.FAMILIES to storage.familiesTable.tableName,
                    SystemProperties.TOXICITIES to storage.toxicitiesTable.tableName,
                    SystemProperties.DESCRIPTIONS to storage.descriptionsTable.tableName,
                )
            )
            .build()
        storage.grantReadWriteAccess(function)
        Api(this, "Api", function)
    }
}
