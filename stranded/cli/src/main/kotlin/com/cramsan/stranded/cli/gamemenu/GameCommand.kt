package com.cramsan.stranded.cli.gamemenu

/**
 * @author cramsan
 */
enum class GameCommand(val friendlyName: String) {
    PLAY_CARD("playcard"),
    SPEND_ENERGY("spend"),
    SET_READY("setready"),
    SET_NOT_READY("setnotready"),
    LIST_HAND("listhand"),
    HELP("help"),
    EXIT("exit"),
}

fun commandFromString(friendlyName: String): GameCommand? {
    return GameCommand.values().toList().firstOrNull { it.friendlyName == friendlyName }
}

fun GameCommand.getCommandString() = this.friendlyName

fun GameCommand.getInstructions() = "command: ${getCommandString()}\n" +
        "argument: ${when (this) {
            GameCommand.PLAY_CARD -> "[card id]"
            GameCommand.SPEND_ENERGY -> "[number of hearts]"
            GameCommand.LIST_HAND -> "[NONE]"
            GameCommand.SET_READY -> "[NONE]"
            GameCommand.SET_NOT_READY -> "[NONE]"
            GameCommand.HELP -> "[NONE]"
            GameCommand.EXIT -> "[NONE]"
        }}"

fun availableCommandsMessage() = "Available commands: ${
    GameCommand.values().toList().joinToString { it.getCommandString() }
}"
