package com.cramsan.kotlinlib.aws

import com.cramsan.cdkrepo.angular.AngularAmplify
import com.cramsan.cdkrepo.backend.ElasticBeanstalkApplication
import me.cesar.application.common.AttributeNames
import me.cesar.application.common.Environment
import me.cesar.application.lambda.LambdaHandlerController
import software.amazon.awscdk.CfnOutput
import software.amazon.awscdk.Duration
import software.amazon.awscdk.Stack
import software.amazon.awscdk.StackProps
import software.amazon.awscdk.services.dynamodb.Attribute
import software.amazon.awscdk.services.dynamodb.AttributeType
import software.amazon.awscdk.services.dynamodb.GlobalSecondaryIndexProps
import software.amazon.awscdk.services.dynamodb.Table
import software.amazon.awscdk.services.dynamodb.TableProps
import software.amazon.awscdk.services.events.Rule
import software.amazon.awscdk.services.events.RuleProps
import software.amazon.awscdk.services.events.Schedule
import software.amazon.awscdk.services.events.targets.LambdaFunction
import software.amazon.awscdk.services.lambda.Code
import software.amazon.awscdk.services.lambda.Function
import software.amazon.awscdk.services.lambda.Runtime
import software.constructs.Construct

/**
 * This stack will deploy the target application front-end and back-end configurations.
 */
class FullStackApplicationStack @JvmOverloads constructor(
    scope: Construct?,
    id: String?,
    props: StackProps? = null,
    scheduleExpression: String,
    apply: (FullStackApplicationStack.() -> Unit)? = null,
) : Stack(scope, id, props) {
    init {
        AngularAmplify(this, "KotlinLibsFE")

        ElasticBeanstalkApplication(this, "KotlinLibsBE", "../server-jvm/build/libs/server-jvm-all.jar")

        apply?.let { it(this) }

        val articlesTable = Table(
            this,
            "articles-$id",
            TableProps.builder().apply {
                partitionKey(
                    Attribute.builder().apply {
                        name(AttributeNames.SOURCE)
                        type(AttributeType.STRING)
                    }.build(),
                )
                sortKey(
                    Attribute.builder().apply {
                        name(AttributeNames.ID)
                        type(AttributeType.STRING)
                    }.build(),
                )
            }.build(),
        )

        val sourcesTable = Table(
            this,
            "sources-$id",
            TableProps.builder().apply {
                partitionKey(
                    Attribute.builder().apply {
                        name(AttributeNames.SOURCE_TYPE)
                        type(AttributeType.STRING)
                    }.build(),
                )
                sortKey(
                    Attribute.builder().apply {
                        name(AttributeNames.ID)
                        type(AttributeType.STRING)
                    }.build(),
                )
            }.build(),
        )

        articlesTable.addGlobalSecondaryIndex(
            GlobalSecondaryIndexProps.builder().apply {
                indexName("articlesByLastUpdatedForSource")
                partitionKey(
                    Attribute.builder().apply {
                        name(AttributeNames.SOURCE)
                        type(AttributeType.STRING)
                    }.build(),
                )
                sortKey(
                    Attribute.builder().apply {
                        name(AttributeNames.LAST_UPDATED)
                        type(AttributeType.NUMBER)
                    }.build(),
                )
            }.build(),
        )

        articlesTable.addGlobalSecondaryIndex(
            GlobalSecondaryIndexProps.builder().apply {
                indexName("singleArticle")
                partitionKey(
                    Attribute.builder().apply {
                        name(AttributeNames.ID)
                        type(AttributeType.STRING)
                    }.build(),
                )
            }.build(),
        )

        val function = Function.Builder.create(this, "periodic-handler")
            // This is how we would deploy the code through CDK. In our case, we will deploy
            // though a CICD pipeline.
            .code(Code.fromAsset("../lambda-jvm/build/libs/lambda-jvm-aws.jar"))
            .handler("org.springframework.cloud.function.adapter.aws.FunctionInvoker::handleRequest")
            .environment(
                mutableMapOf(
                    // Set the function name in case there are multiple available Functions to run
                    // "FUNCTION_NAME" to "uppercase",
                    "MAIN_CLASS" to LambdaHandlerController::class.qualifiedName,
                    Environment.ARTICLES_TABLE to articlesTable.tableName,
                    Environment.SOURCES_TABLE to sourcesTable.tableName,
                ),
            )
            .timeout(Duration.seconds(30))
            .memorySize(1024)
            .runtime(Runtime.JAVA_11)
            .build()

        Rule(
            this,
            "lambda-periodic",
            RuleProps.builder()
                .apply {
                    enabled(true)
                    schedule(Schedule.expression(scheduleExpression))
                    targets(mutableListOf(LambdaFunction(function)))
                }
                .build(),
        )

        articlesTable.apply {
            grantWriteData(function)
            CfnOutput(this, "ArticlesTableOutput") {
                this.tableName
            }
        }

        sourcesTable.apply {
            grantWriteData(function)
            CfnOutput(this, "SourcesTableOutput") {
                this.tableName
            }
        }
    }
}
