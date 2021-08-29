package com.cramsan.petproject.awslambda.dao

import com.cramsan.petproject.appcore.model.AnimalType
import com.cramsan.petproject.appcore.storage.Description
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey

@DynamoDbBean
data class DescriptionImpl(
    @get:DynamoDbPartitionKey
    override val id: Long,
    override val plantId: Long,
    override val animalId: AnimalType,
    override val locale: String,
    override val description: String
) : Description
