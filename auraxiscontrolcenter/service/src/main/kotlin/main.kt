import com.cramsan.ps2link.appcore.network.requireBody
import com.cramsan.ps2link.core.models.CensusLang
import com.cramsan.ps2link.core.models.Character
import com.cramsan.ps2link.core.models.Namespace
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

fun main() {
    val haltUtil = PS2ServiceModule.provideHaltUtilInterface(PS2ServiceModule.provideHaltUtilDelegate())
    val eventLogger = PS2ServiceModule.provideEventLoggerInterface(PS2ServiceModule.provideEventLoggerDelegate())
    val assertUtil = PS2ServiceModule.provideAssertUtil(eventLogger, haltUtil)
    val threadUtilDelegate = PS2ServiceModule.provideThreadUtilDelegate(assertUtil)
    val threadUtil = PS2ServiceModule.provideThreadUtilInterface(threadUtilDelegate)
    val clock = PS2ServiceModule.provideClock()
    val metrics = PS2ServiceModule.provideMetricsInterface(PS2ServiceModule.provideMetricsDelegate(), eventLogger)
    val dbgCensus = PS2ServiceModule.provideDbgCensus()
    val json = Json {
        ignoreUnknownKeys = true
    }

    console.log("Launch")
    val character = Character(
        "1234567890",
        namespace = Namespace.PS2PC,
        cached = false
    )
    println(greeting(character.toString()))

    val ktoHttpClient = PS2ServiceModule.provideKtorHttpClient(json)
    val httpClient = PS2ServiceModule.provideHttpClient(ktoHttpClient, json)
    val censusServiceClient = PS2ServiceModule.provideDbgServiceClient(dbgCensus, httpClient, clock)

    GlobalScope.launch {
        console.log("Start")
        val profiles = censusServiceClient.getProfile(
            character_id = "5428013610429053697",
            namespace = Namespace.PS2PC,
            currentLang = CensusLang.EN
        )
        console.log(profiles.requireBody())
        console.log("End")
    }
}

fun greeting(name: String) =
    "Hello, $name"
