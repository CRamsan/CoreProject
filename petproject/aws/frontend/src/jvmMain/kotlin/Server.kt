
import com.cramsan.framework.logging.logE
import com.cramsan.framework.logging.logI
import com.cramsan.petproject.appcore.storage.Toxicity
import com.cramsan.petproject.appcore.storage.implementation.ModelStorage
import com.cramsan.petproject.awslambda.DependenciesConfig
import com.cramsan.petproject.awslambda.common.Paths
import com.cramsan.petproject.awslambda.common.SystemProperties
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.content.TextContent
import io.ktor.features.CORS
import io.ktor.features.Compression
import io.ktor.features.ContentNegotiation
import io.ktor.features.StatusPages
import io.ktor.features.gzip
import io.ktor.gson.gson
import io.ktor.http.ContentType
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.http.content.resources
import io.ktor.http.content.static
import io.ktor.http.withCharset
import io.ktor.request.path
import io.ktor.response.respond
import io.ktor.response.respondText
import io.ktor.routing.get
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty

val shoppingList = mutableListOf(
    ShoppingListItem("Cucumbers 🥒", 1),
    ShoppingListItem("Tomatoes 🍅", 2),
    ShoppingListItem("Orange Juice 🍊", 3)
)

lateinit var modelStorage: ModelStorage
var initalized = false

private fun configure() {
    val dependencies = DependenciesConfig(
        System.getenv(SystemProperties.PLANT_NAME) ?: "TestAwsJavaStack-StoragePlants5B3D2D22-YWJ5FDW4KQXC",
        System.getenv(SystemProperties.COMMON_NAMES) ?: "",
        System.getenv(SystemProperties.MAIN_NAMES) ?: "",
        System.getenv(SystemProperties.FAMILIES) ?: "",
        System.getenv(SystemProperties.TOXICITIES) ?: "",
        System.getenv(SystemProperties.DESCRIPTIONS) ?: "",
    )

    logI(TAG, "Configuring handler")
    modelStorage = dependencies.modelStorage
    initalized = true
}

private fun handleToxicitiesApi(): List<Toxicity> = modelStorage.getToxicity()

private fun handleDescriptionsApi() = 200

private fun handleFamiliesApi() = 200

private fun handleCommonNamesApi() = 200

private fun handleMainNamesApi() = 200

private fun handlePlantsApi() = modelStorage.getPlants()

private fun handleAddToxicitiesApi() = Unit
private fun handleAddDescriptionsApi() = Unit
private fun handleAddFamiliesApi() = Unit
private fun handleAddCommonNamesApi() = Unit
private fun handleAddMainNamesApi() = Unit
private fun handleAddPlantsApi() = Unit

private fun handleDeleteToxicitiesApi() = Unit
private fun handleDeleteDescriptionsApi() = Unit
private fun handleDeleteFamiliesApi() = Unit
private fun handleDeleteCommonNamesApi() = Unit
private fun handleDeleteMainNamesApi() = Unit
private fun handleDeletePlantsApi() = Unit

fun main() {
    configure()

    logI(TAG, "Starting execution")
    val port = System.getenv("PORT")?.toInt() ?: 9090
    embeddedServer(Netty, port) {
        install(ContentNegotiation) {
            gson {
                setPrettyPrinting()
                disableHtmlEscaping()
            }
        }
        install(CORS) {
            method(HttpMethod.Get)
            method(HttpMethod.Post)
            method(HttpMethod.Delete)
            anyHost()
        }
        install(Compression) {
            gzip()
        }
        install(StatusPages) {
            status(HttpStatusCode.NotFound) {
                logI(TAG, "404 resulted from call to ${call.request.path()}")
                call.respond(TextContent("${it.value} ${it.description}", ContentType.Text.Plain.withCharset(Charsets.UTF_8), it))
            }
            exception<Throwable> { cause ->
                logE(TAG, "Internal failure due to: ${cause.localizedMessage}")
                cause.printStackTrace()
                call.respond(HttpStatusCode.InternalServerError, "Internal Server Error")
                throw cause
            }
        }

        routing {
            get("/") {
                call.respondText(
                    this::class.java.classLoader.getResource("index.html")!!.readText(),
                    ContentType.Text.Html
                )
            }
            static("/") {
                resources("")
            }

            Paths.values().forEach { path ->
                get(path.value) {
                    when (Paths.fromString(call.request.path())) {
                        Paths.PLANT_NAME -> call.respond(HttpStatusCode.OK, handlePlantsApi())
                        Paths.MAIN_NAMES -> call.respond(HttpStatusCode.OK, handleMainNamesApi())
                        Paths.COMMON_NAMES -> call.respond(HttpStatusCode.OK, handleCommonNamesApi())
                        Paths.FAMILIES -> call.respond(HttpStatusCode.OK, handleFamiliesApi())
                        Paths.DESCRIPTIONS -> call.respond(HttpStatusCode.OK, handleDescriptionsApi())
                        Paths.TOXICITIES -> call.respond(HttpStatusCode.OK, handleToxicitiesApi())
                        null -> call.respond(HttpStatusCode.NotFound)
                    }
                }
            }
        }
    }.start(wait = true)
}

const val TAG = "Server"
