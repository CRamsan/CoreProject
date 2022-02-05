package com.cramsan.stranded.cli.mainmenu

/**
 * @author cramsan
 */
enum class MainMenuCommand(val friendlyName: String) {
    SET_NAME("setname"),
    LIST("list"),
    JOIN("join"),
    LEAVE("leave"),
    CREATE("create"),
    SET_READY("setready"),
    SET_NOT_READY("setnotready"),
    START_GAME("startgame"),
    HELP("help"),
    EXIT("exit"),
}

fun commandFromString(friendlyName: String): MainMenuCommand? {
    return MainMenuCommand.values().toList().firstOrNull { it.friendlyName == friendlyName }
}

fun MainMenuCommand.getCommandString() = this.friendlyName

fun MainMenuCommand.getInstructions() = "command: ${getCommandString()}\n" +
        "argument: ${when (this) {
            MainMenuCommand.SET_NAME -> "[new player name]"
            MainMenuCommand.LIST -> "[NONE]"
            MainMenuCommand.JOIN -> "[lobby id]"
            MainMenuCommand.LEAVE -> "[NONE]"
            MainMenuCommand.CREATE -> "[lobby name]"
            MainMenuCommand.SET_READY -> "[NONE]"
            MainMenuCommand.SET_NOT_READY -> "[NONE]"
            MainMenuCommand.START_GAME -> "[NONE]"
            MainMenuCommand.HELP -> "[NONE]"
            MainMenuCommand.EXIT -> "[NONE]"
        }}"

fun availableCommandsMessage() = "Available commands: ${
    MainMenuCommand.values().toList().joinToString { it.getCommandString() }
}"
