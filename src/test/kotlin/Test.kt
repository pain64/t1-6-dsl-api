import java.io.OutputStream
import kotlin.test.Test
import kotlin.test.assertEquals

class Test {
    private val server = InMemoryHttpServer {
        // Just for test
        port = 3333

        get("/") {
            // TODO: This line must not work!
            // port = 4004

            StringResponse("hello!")
        }
        post("/echo") { req ->
            // TODO: write input to output prepending all headers in format `name` -> `value`
            object : Response {
                // TODO
                override fun write(out: OutputStream) {
                    TODO()
                }

            }
        }
    }

    @Test
    fun `test simple get request`() {
        assertEquals(
            server.handle(
                InMemoryRequest(
                    HttpMethod.Get, "/", mapOf(), byteArrayOf().asData
                )
            ),
            InMemoryResponse(200, mapOf(), "hello!".asData)
        )
    }

    @Test
    fun `test route not found`() {
        assertEquals(
            server.handle(
                InMemoryRequest(
                    HttpMethod.Get, "/xxx", mapOf(), byteArrayOf().asData
                )
            ),
            InMemoryResponse(404, mapOf(), "not found".asData)
        )
    }

    @Test
    fun `test echo request`() {
        assertEquals(
            server.handle(
                InMemoryRequest(
                    HttpMethod.Post, "/echo",
                    mapOf(
                        "ContentType" to "text/plain",
                        "X-Real-IP" to "10.0.0.3",
                    ),
                    "streaming request data".asData
                )
            ),
            InMemoryResponse(
                200, mapOf(),
                """
                ContentType -> text/plain
                X-Real-IP -> 10.0.0.3
                streaming request data
                """.trimIndent().asData
            )
        )
    }
}