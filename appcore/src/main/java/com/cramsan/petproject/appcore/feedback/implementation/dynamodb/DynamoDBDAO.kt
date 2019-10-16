package com.cramsan.petproject.appcore.feedback.implementation.dynamodb

import com.cramsan.petproject.appcore.feedback.FeedbackManagerDAO
import com.cramsan.petproject.appcore.model.feedback.Feedback
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.dynamodb.DynamoDbClient
import software.amazon.awssdk.services.dynamodb.model.AttributeValue
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest

class DynamoDBDAO : FeedbackManagerDAO {

    private val ddb: DynamoDbClient = DynamoDbClient.builder()
        .region(Region.US_WEST_2)
        .build()
    private val tableName: String = "feedback"

    override fun submitFeedback(feedback: Feedback) {
        val item_values = HashMap<String, AttributeValue>()

        item_values.put("type", AttributeValue.builder().s(feedback.type.name).build())
        item_values.put("suggestion", AttributeValue.builder().s(feedback.suggestion).build())
        item_values.put("reference", AttributeValue.builder().s(feedback.referenceId.toString()).build())

        val request = PutItemRequest.builder()
            .tableName(tableName)
            .item(item_values)
            .build()
        try {
            ddb.putItem(request)
        } catch (e: DynamoDbException) {
            System.err.println(e.message)
        }
    }
}
