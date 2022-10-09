package me.cesar.application.frontend.theme

import androidx.compose.runtime.Composable
import org.jetbrains.compose.web.css.Style
import org.jetbrains.compose.web.dom.ContentBuilder
import org.jetbrains.compose.web.dom.Div
import org.w3c.dom.HTMLDivElement

/**
 * @author cramsan
 */
@Composable
fun KotlinLibsTheme(
    header: ContentBuilder<HTMLDivElement>?,
    footer: ContentBuilder<HTMLDivElement>?,
    content: ContentBuilder<HTMLDivElement>?,
) {
    Style(KotlinLibsStyle)
    Div(
        attrs = { classes(KotlinLibsStyle.header) },
        content = header,
    )
    Div(content = content)
    Div(
        attrs = { classes(KotlinLibsStyle.footer) },
        content = footer,
    )
}
