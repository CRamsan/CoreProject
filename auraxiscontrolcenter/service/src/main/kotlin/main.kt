import com.cramsan.ps2link.appcore.network.requireBody
import com.cramsan.ps2link.appcore.repository.PS2LinkRepository
import com.cramsan.ps2link.core.models.CensusLang
import com.jessecorbett.diskord.bot.bot
import com.jessecorbett.diskord.bot.classicCommands
import kotlinx.serialization.json.Json
import ui.fullCharacter

external val process: Process
external interface Process {
    val env: dynamic
}

lateinit var botToken: String
lateinit var ps2Repository: PS2LinkRepository

fun configure() {
    val haltUtil = PS2ServiceModule.provideHaltUtilInterface(PS2ServiceModule.provideHaltUtilDelegate())
    val eventLogger = PS2ServiceModule.provideEventLoggerInterface(PS2ServiceModule.provideEventLoggerDelegate())
    val assertUtil = PS2ServiceModule.provideAssertUtil(eventLogger, haltUtil)
    val threadUtilDelegate = PS2ServiceModule.provideThreadUtilDelegate(assertUtil)
    val threadUtil = PS2ServiceModule.provideThreadUtilInterface(threadUtilDelegate)
    val clock = PS2ServiceModule.provideClock()
    val metrics = PS2ServiceModule.provideMetricsInterface(PS2ServiceModule.provideMetricsDelegate(), eventLogger)
    val serviceId = PS2ServiceModule.provideServiceId()
    val dbgCensus = PS2ServiceModule.provideDbgCensus(serviceId)
    val json = Json {
        ignoreUnknownKeys = true
    }
    val ktoHttpClient = PS2ServiceModule.provideKtorHttpClient(json)
    val httpClient = PS2ServiceModule.provideHttpClient(ktoHttpClient, json)
    botToken = PS2ServiceModule.provideBotToken()
    val censusServiceClient = PS2ServiceModule.provideDbgServiceClient(dbgCensus, httpClient, clock)
    ps2Repository = PS2ServiceModule.providePS2LinkRepository(censusServiceClient, clock)
}

suspend fun main() {
    configure()
    bot(botToken) {
        classicCommands(".") { // "." is the default, but is provided here anyway for example purposes
            command("character") { message ->
                val args = message.content.split(" ")
                val characterName = args[1]
                val characters = ps2Repository.searchForCharacter(characterName, CensusLang.EN).requireBody()

                if (characters.isEmpty()) {
                    message.reply("No characters found starting with the name $characterName")
                    return@command
                }

                val character = characters.first()
                if (characters.size == 1) {
                    message.replyEmbed(
                        message = character.name.toString(),
                        block = {
                            fullCharacter(character)
                        }
                    )
                } else {
                    message.replyEmbed(
                        message = character.name.toString(),
                        block = {
                            fullCharacter(character)
                        }
                    )
                }
            }
        }
    }
}
