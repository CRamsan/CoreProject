package me.cesar.application.spring

import com.fasterxml.jackson.module.kotlin.readValue
import me.cesar.application.common.model.Article
import me.cesar.application.common.network.PageResponse
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

/**
 * @author cramsan
 */
internal class SpringMapperKtTest {

    private val mapper = createObjectMapper()

    @Test
    fun `test serializing a PageResponse`() {
        val input = """
            {
               "content":[
                  {
                     "id":"id1",
                     "title":"title 1",
                     "sourceId":"12345",
                     "content":"This is an example",
                     "publishedTime":100000
                  },
                  {
                     "id":"id2",
                     "title":"title 2",
                     "sourceId":"6789",
                     "content":"This is another example",
                     "publishedTime":200000
                  }
               ],
               "number":0,
               "size":2,
               "totalElements":2,
               "last":true,
               "totalPages":1,
               "first":true,
               "numberOfElements":2
            }
        """.trimIndent()
        val body: PageResponse<Article> = mapper.readValue(input)
        assertEquals(0, body.number)
        assertEquals(2, body.size)
        assertEquals(2, body.totalElements)
        assertEquals(true, body.last)
        assertEquals(1, body.totalPages)
        assertEquals(true, body.first)
        assertEquals(2, body.numberOfElements)
        assertEquals(2, body.content.size)
    }

}