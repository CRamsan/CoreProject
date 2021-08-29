package com.cramsan.petproject.awslambda.dao

import com.cramsan.petproject.appcore.model.AnimalType
import com.cramsan.petproject.appcore.model.ToxicityValue
import com.cramsan.petproject.appcore.storage.Toxicity
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey

@DynamoDbBean
data class ToxicityImpl(
    @get:DynamoDbPartitionKey
    override val id: Long,
    override val plantId: Long,
    override val animalId: AnimalType,
    override val toxic: ToxicityValue,
    override val source: String
) : Toxicity
