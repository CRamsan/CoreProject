package me.cesar.application.frontend.widgets

import androidx.compose.runtime.Composable
import me.cesar.application.frontend.theme.KotlinLibsStyle
import org.jetbrains.compose.web.dom.A
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.H1
import org.jetbrains.compose.web.dom.Img
import org.jetbrains.compose.web.dom.Text

/**
 * @author cramsan
 */
@Composable
fun Header() {
    Div {
        Img(
            attrs = { classes(KotlinLibsStyle.logo) },
            src = "/logo.svg",
        )
    }
    Div {
        H1 {
            A(
                href = "/",
            ) {
                Text("KotlinLibs")
            }
        }
    }
}
