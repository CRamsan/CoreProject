package me.cesar.application.common

/**
 * List of constants that will be used in this project.
 */
object NetworkPath {
    private const val API_ROOT = "/api"
    const val ARTICLE_API = "$API_ROOT/article"
}

/**
 * Environment variables
 */
object Environment {
    const val ARTICLES_TABLE = "ARTICLES_TABLE"
    const val SOURCES_TABLE = "SOURCES_TABLE"
}

/**
 * DynamoDB table names
 */
object TableNames {
    const val ARTICLES = "ARTICLE_TABLE_NAME"
    const val SOURCES = "SOURCES_TABLE_NAME"
}

/**
 * DynamoDb table attributes
 */
object AttributeNames {
    const val ID = "id"
    const val SOURCE = "source"
    const val LAST_UPDATED = "lastUpdated"
    const val SOURCE_TYPE = "sourceType"
}
