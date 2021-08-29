package com.cramsan.petproject.awslambda.dao

import com.cramsan.petproject.appcore.storage.Plant
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey

@DynamoDbBean
class PlantImp() : Plant {

    @get:DynamoDbPartitionKey
    @get:DynamoDbAttribute(value = "id")
    var _id: Long = -1
    @get:DynamoDbAttribute(value = "scientificName")
    lateinit var _scientificName: String
    @get:DynamoDbAttribute(value = "imageUrl")
    lateinit var _imageUrl: String

    constructor(
        id: Long,
        scientificName: String,
        imageUrl: String,
    ) : this() {
        _id = id
        _scientificName = scientificName
        _imageUrl = imageUrl
    }

    override val id: Long = _id
    override val scientificName: String = _scientificName
    override val imageUrl: String = _imageUrl
}
