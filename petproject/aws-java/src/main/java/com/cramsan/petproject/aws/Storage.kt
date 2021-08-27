package com.cramsan.petproject.aws

import software.amazon.awscdk.core.Construct
import software.amazon.awscdk.services.dynamodb.Attribute
import software.amazon.awscdk.services.dynamodb.AttributeType
import software.amazon.awscdk.services.dynamodb.Table
import software.amazon.awscdk.services.dynamodb.TableProps

class Storage(scope: software.constructs.Construct, id: String) : Construct(scope, id) {
    init {
        Table.Builder.create(this, "Description")
            .partitionKey(Attribute.builder().name("id").type(AttributeType.NUMBER).build())
            .build()
        Table.Builder.create(this, "Plants")
            .partitionKey(Attribute.builder().name("id").type(AttributeType.NUMBER).build())
            .build()
        Table.Builder.create(this, "CommonNames")
            .partitionKey(Attribute.builder().name("id").type(AttributeType.NUMBER).build())
            .build()
        Table.Builder.create(this, "Families")
            .partitionKey(Attribute.builder().name("id").type(AttributeType.NUMBER).build())
            .build()
        Table.Builder.create(this, "MainNames")
            .partitionKey(Attribute.builder().name("id").type(AttributeType.NUMBER).build())
            .build()
        Table.Builder.create(this, "Toxicities")
            .partitionKey(Attribute.builder().name("id").type(AttributeType.NUMBER).build())
            .build()
    }
}
