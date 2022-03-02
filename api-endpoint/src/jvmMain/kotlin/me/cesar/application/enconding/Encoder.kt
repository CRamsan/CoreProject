package me.cesar.application.enconding

/**
 * Common interface for an encoder that is able to encode and decode a string.
 * A string can be encoded and decoded as long as the same parameters are provided.
 */
interface Encoder {

    /**
     * Encode a [content] string with an optional [cipher] and [seed]. The return string
     * will be able to passed to the [decode] function to be decoded back to the original [content].
     */
    fun encode(content: String, cipher: String? = null, seed: Int? = null): String

    /**
     * Decode a [content] string with an optional [cipher] and [seed]. The return string
     * will be able to passed to the [encode] function to be encoded back to the original [content].
     */
    fun decode(content: String, cipher: String? = null, seed: Int? = null): String
}