package me.cesar.application.frontend.theme

import org.jetbrains.compose.web.css.Color

private val primaryColor = Color("#27282cFF")
private val primaryLightColor = Color("#428e92FF")
private val primaryDarkColor = Color("#00363aFF")
private val secondaryColor = Color("#26c6daFF")
private val secondaryLightColor = Color("#6ff9ffFF")
private val secondaryDarkColor = Color("#0095a8FF")
private val primaryTextColor = Color("#FFFFFFFF")
private val secondaryTextColor = Color("#000000FF")
private val errorColor = Color("#FF0000FF")

/**
 * Primary color palette
 */
object Colors {
    val primary = primaryColor
    val primaryVariant = primaryLightColor
    val secondary = secondaryColor
    val secondaryVariant = secondaryLightColor
    val background = primaryDarkColor
    val surface = primaryDarkColor
    val onPrimary = primaryTextColor
    val onSecondary = secondaryTextColor
    val onBackground = primaryTextColor
    val onSurface = primaryTextColor
    val error = errorColor
    val onError = primaryTextColor
}
