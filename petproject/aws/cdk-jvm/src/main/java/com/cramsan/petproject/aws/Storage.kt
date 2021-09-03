package com.cramsan.petproject.aws

import software.amazon.awscdk.core.Construct
import software.amazon.awscdk.services.dynamodb.Attribute
import software.amazon.awscdk.services.dynamodb.AttributeType
import software.amazon.awscdk.services.dynamodb.Table
import software.amazon.awscdk.services.lambda.IFunction

class Storage(scope: software.constructs.Construct, id: String) : Construct(scope, id) {

    var plantsTable: Table = Table.Builder.create(this, "Plants")
        .partitionKey(Attribute.builder().name("id").type(AttributeType.NUMBER).build())
        .build()
    var commonNamesTable: Table = Table.Builder.create(this, "CommonNames")
        .partitionKey(Attribute.builder().name("id").type(AttributeType.NUMBER).build())
        .build()
    var mainNamesTable: Table = Table.Builder.create(this, "MainNames")
        .partitionKey(Attribute.builder().name("id").type(AttributeType.NUMBER).build())
        .build()
    var familiesTable: Table = Table.Builder.create(this, "Families")
        .partitionKey(Attribute.builder().name("id").type(AttributeType.NUMBER).build())
        .build()
    var toxicitiesTable: Table = Table.Builder.create(this, "Toxicities")
        .partitionKey(Attribute.builder().name("id").type(AttributeType.NUMBER).build())
        .build()
    var descriptionsTable: Table = Table.Builder.create(this, "Description")
        .partitionKey(Attribute.builder().name("id").type(AttributeType.NUMBER).build())
        .build()

    fun grantReadWriteAccess(lambda: IFunction) {
        plantsTable.grantReadData(lambda)
        commonNamesTable.grantReadData(lambda)
        mainNamesTable.grantReadData(lambda)
        familiesTable.grantReadData(lambda)
        descriptionsTable.grantReadData(lambda)
        toxicitiesTable.grantReadData(lambda)
    }
}
