package com.cramsan.petproject.awslambda.dao

import com.cramsan.petproject.appcore.storage.PlantCommonName
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey

@DynamoDbBean
class PlantCommonNameImpl() : PlantCommonName {

    @get:DynamoDbPartitionKey
    @get:DynamoDbAttribute(value = "id")
    var _id: Long = -1
    @get:DynamoDbAttribute(value = "commonName")
    lateinit var _commonName: String
    @get:DynamoDbAttribute(value = "plantId")
    var _plantId: Long = -1
    @get:DynamoDbAttribute(value = "locale")
    lateinit var _locale: String

    constructor(
        id: Long,
        commonName: String,
        plantId: Long,
        locale: String,
    ) : this() {
        _id = id
        _commonName = commonName
        _plantId = plantId
        _locale = locale
    }

    override val id: Long = _id
    override val commonName: String = _commonName
    override val plantId: Long = _plantId
    override val locale: String = _locale
}
