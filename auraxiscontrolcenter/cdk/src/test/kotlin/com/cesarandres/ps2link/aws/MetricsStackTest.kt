package com.cesarandres.ps2link.aws

import com.cramsan.cdkrepo.metrics.MetricsStack
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import org.junit.Test
import software.amazon.awscdk.core.App
import java.io.IOException
import kotlin.test.assertTrue

class MetricsStackTest {
    @Test
    @Throws(IOException::class)
    fun testStack() {
        val app = App()
        val stack = MetricsStack(app, "test")

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
