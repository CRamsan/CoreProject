package me.cesar.application.enconding

import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * To generate the test inputs and outputs I used https://www.base64encode.org/.
 */
class Base64EncoderTest {

    lateinit var encoder: Encoder

    @BeforeTest
    fun setUp() {
        encoder = Base64Encoder()
    }

    @Test
    fun testEncodingText() {
        val input = "This is a string of known character"
        val expectedOutput = "VGhpcyBpcyBhIHN0cmluZyBvZiBrbm93biBjaGFyYWN0ZXI="

        val output = encoder.encode(input)

        assertEquals(expectedOutput, output)
    }

    @Test
    fun testDecodingText() {
        val input = "VGhpcyBpcyBhIHN0cmluZyBvZiBrbm93biBjaGFyYWN0ZXI="
        val expectedOutput = "This is a string of known character"

        val output = encoder.decode(input)

        assertEquals(expectedOutput, output)
    }
}