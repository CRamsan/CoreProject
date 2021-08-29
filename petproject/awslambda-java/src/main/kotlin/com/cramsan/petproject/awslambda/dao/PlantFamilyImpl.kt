package com.cramsan.petproject.awslambda.dao

import com.cramsan.petproject.appcore.storage.PlantFamily
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey

@DynamoDbBean
data class PlantFamilyImpl(
    @get:DynamoDbPartitionKey
    override val id: Long,
    override val family: String,
    override val plantId: Long,
    override val locale: String
) : PlantFamily
