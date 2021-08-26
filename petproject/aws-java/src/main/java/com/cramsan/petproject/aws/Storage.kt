package com.cramsan.petproject.aws

import software.amazon.awscdk.core.Construct
import software.amazon.awscdk.services.dynamodb.Attribute
import software.amazon.awscdk.services.dynamodb.AttributeType
import software.amazon.awscdk.services.dynamodb.Table
import software.amazon.awscdk.services.dynamodb.TableProps

class Storage(scope: software.constructs.Construct, id: String) : Construct(scope, id) {
    init {
        Table(
            this,
            "Description",
            TableProps.Builder()
                .partitionKey(Attribute.builder().name("id").type(AttributeType.NUMBER).build())
                .build()
        )

        Table(
            this,
            "Plants",
            TableProps.Builder()
                .partitionKey(Attribute.builder().name("id").type(AttributeType.NUMBER).build())
                .build()
        )

        Table(
            this,
            "CommonNames",
            TableProps.Builder()
                .partitionKey(Attribute.builder().name("id").type(AttributeType.NUMBER).build())
                .build()
        )

        Table(
            this,
            "Families",
            TableProps.Builder()
                .partitionKey(Attribute.builder().name("id").type(AttributeType.NUMBER).build())
                .build()
        )

        Table(
            this,
            "MainNames",
            TableProps.Builder()
                .partitionKey(Attribute.builder().name("id").type(AttributeType.NUMBER).build())
                .build()
        )

        Table(
            this,
            "Toxicities",
            TableProps.Builder()
                .partitionKey(Attribute.builder().name("id").type(AttributeType.NUMBER).build())
                .build()
        )
    }
}
