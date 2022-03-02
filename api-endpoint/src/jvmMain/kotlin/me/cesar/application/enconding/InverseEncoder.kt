package me.cesar.application.enconding

/**
 * Encoder that inverses the string.
 */
class InverseEncoder : Encoder {

    override fun encode(content: String, cipher: String?, seed: Int?): String {
        return content.reversed()
    }

    override fun decode(content: String, cipher: String?, seed: Int?): String {
        return content.reversed()
    }
}