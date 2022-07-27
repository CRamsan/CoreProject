package com.cramsan.kotlinlib.aws

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import software.amazon.awscdk.App
import java.io.IOException
import kotlin.test.Test
import kotlin.test.assertTrue

class FullStackApplicationStackTest {
    @Test
    @Throws(IOException::class)
    fun testStack() {
        val app = App()
        val stack = FullStackApplicationStack(app, "test")

        // synthesize the stack to a CloudFormation template
        val actual =
            JSON.valueToTree<JsonNode>(app.synth().getStackArtifact(stack.artifactId).template)

        // Update once resources have been added to the stack
        assertTrue(actual["Resources"] != null)
    }

    companion object {
        private val JSON = ObjectMapper().configure(SerializationFeature.INDENT_OUTPUT, true)
    }
}
