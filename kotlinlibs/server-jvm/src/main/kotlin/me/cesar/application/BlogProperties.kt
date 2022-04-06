package me.cesar.application

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

/**
 * Data class that loads properties from the application properties resource file.
 */
@ConstructorBinding
@ConfigurationProperties("blog")
data class BlogProperties(
    var title: String,
    val banner: Banner,
) {
    /**
     * Class for demo purpose only.
     */
    data class Banner(val title: String? = null, val content: String)
}
