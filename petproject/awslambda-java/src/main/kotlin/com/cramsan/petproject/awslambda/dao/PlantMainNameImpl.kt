package com.cramsan.petproject.awslambda.dao

import com.cramsan.petproject.appcore.storage.PlantMainName
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey

@DynamoDbBean
class PlantMainNameImpl() : PlantMainName {
    @get:DynamoDbPartitionKey
    @get:DynamoDbAttribute(value = "id")
    var _id: Long = -1
    @get:DynamoDbAttribute(value = "mainName")
    lateinit var _mainName: String
    @get:DynamoDbAttribute(value = "plantId")
    var _plantId: Long = -1
    @get:DynamoDbAttribute(value = "locale")
    lateinit var _locale: String

    constructor(
        id: Long,
        mainName: String,
        plantId: Long,
        locale: String,
    ) : this() {
        _id = id
        _mainName = mainName
        _plantId = plantId
        _locale = locale
    }

    override val id: Long = _id
    override val mainName: String = _mainName
    override val plantId: Long = _plantId
    override val locale: String = _locale
}
