package me.cesar.application.frontend.theme

import org.jetbrains.compose.web.css.AlignItems
import org.jetbrains.compose.web.css.DisplayStyle
import org.jetbrains.compose.web.css.FlexWrap
import org.jetbrains.compose.web.css.JustifyContent
import org.jetbrains.compose.web.css.LineStyle
import org.jetbrains.compose.web.css.StyleSheet
import org.jetbrains.compose.web.css.alignItems
import org.jetbrains.compose.web.css.backgroundColor
import org.jetbrains.compose.web.css.border
import org.jetbrains.compose.web.css.borderRadius
import org.jetbrains.compose.web.css.color
import org.jetbrains.compose.web.css.display
import org.jetbrains.compose.web.css.flexWrap
import org.jetbrains.compose.web.css.height
import org.jetbrains.compose.web.css.justifyContent
import org.jetbrains.compose.web.css.keywords.auto
import org.jetbrains.compose.web.css.margin
import org.jetbrains.compose.web.css.maxWidth
import org.jetbrains.compose.web.css.overflow
import org.jetbrains.compose.web.css.padding
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.textDecoration
import org.jetbrains.compose.web.css.width

/**
 * @author cramsan
 */
object KotlinLibsStyle : StyleSheet() {

    init {
        "a" style {
            color(Colors.onPrimary)
            textDecoration("none")
        }
        "h1, h2, h3" style {
            margin(4.px, 0.px)
        }
    }

    val header by style {
        backgroundColor(Colors.primary)
        color(Colors.onPrimary)
        padding(Padding.none)
        display(DisplayStyle.Flex)
        flexWrap(FlexWrap.Wrap)
        justifyContent(JustifyContent.SpaceAround)

        "h1, h2, h3" style {
            margin(Padding.large, Padding.large)
        }
    }

    val footer by style {
        backgroundColor(Colors.primary)
        color(Colors.onPrimary)
        padding(Size.large)
    }

    val inlineAlignCenter by style {
        display(DisplayStyle.Flex)
        alignItems(AlignItems.Center)
    }

    val snippet by style {
        width(100.percent)
        maxWidth(400.px)
        borderRadius(25.px)
        margin(10.px)
        overflow("hidden")
        border(
            width = 2.px,
            style = LineStyle.Solid,
            color = Colors.primary,
        )
    }

    val snippetContent by style {
        padding(Size.large)
        overflow("hidden")
    }

    val snippedList by style {
        display(DisplayStyle.Flex)
        flexWrap(FlexWrap.Wrap)
        justifyContent(JustifyContent.SpaceAround)
    }

    val thumbnail by style {
        width(100.percent)
        height(auto)
    }
}