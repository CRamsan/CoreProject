package me.cesar.application.frontend.widgets

import androidx.compose.runtime.Composable
import me.cesar.application.frontend.theme.KotlinLibsStyle
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.H2
import org.jetbrains.compose.web.dom.Text

/**
 * @author cramsan
 */
@Composable
fun Header() {
    Div(
        attrs = { classes(KotlinLibsStyle.inlineAlignCenter) },
    ) {
        H2 {
            Text("KotlinLibs")
        }
    }
    Div(
        attrs = { classes(KotlinLibsStyle.inlineAlignCenter) },
    ) {
    }
}
