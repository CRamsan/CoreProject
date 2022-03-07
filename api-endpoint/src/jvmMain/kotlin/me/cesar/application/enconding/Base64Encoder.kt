package me.cesar.application.enconding

import java.util.Base64

/**
 * Encoder that takes uses Base64 to encode and decode a string.
 */
class Base64Encoder : Encoder {

    override fun encode(content: String, cipher: String?, seed: Int?): String {
        val base64Encoder = Base64.getEncoder()
        return base64Encoder.encodeToString(content.toByteArray())
    }

    override fun decode(content: String, cipher: String?, seed: Int?): String {
        val base64Decoder = Base64.getDecoder()
        val resultBytes = base64Decoder.decode(content.toByteArray())
        return String(resultBytes)
    }
}
