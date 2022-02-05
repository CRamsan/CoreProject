package com.cramsan.stranded.cli

/**
 * @author cramsan
 */
interface CliScreen {
    fun startScreen()

    fun processInput(): NavigationCommand

    fun stopScreen()
}