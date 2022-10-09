package me.cesar.application.frontend.home

import androidx.compose.runtime.Composable
import me.cesar.application.common.model.Article
import me.cesar.application.frontend.theme.KotlinLibsStyle
import me.cesar.application.frontend.widgets.Snippet
import org.jetbrains.compose.web.dom.Button
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Text

/**
 * @author cramsan
 */
@Suppress("LongMethod", "FunctionNaming")
@Composable
fun HomeScreen(
    articles: List<Article>,
    showButtonNext: Boolean,
    showButtonPrevious: Boolean,
    isLoading: Boolean,
) {
    if (isLoading) {
        Text("Loading...")
    }
    Div(
        attrs = { classes(KotlinLibsStyle.snippedList) },
    ) {
        articles.forEach {
            Snippet(it)
        }
    }
    if (showButtonPrevious) {
        Button(attrs = {
            onClick { }
        },) {
            Text("Previous")
        }
    }
    if (showButtonNext) {
        Button(attrs = {
            onClick { }
        },) {
            Text("Next")
        }
    }
}
