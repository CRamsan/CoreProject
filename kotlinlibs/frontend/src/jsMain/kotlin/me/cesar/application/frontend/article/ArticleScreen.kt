package me.cesar.application.frontend.article

import androidx.compose.runtime.Composable
import me.cesar.application.common.model.Article
import me.cesar.application.frontend.theme.KotlinLibsStyle
import me.cesar.application.frontend.widgets.Article
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Text

/**
 * @author cramsan
 */
@Suppress("LongMethod", "FunctionNaming")
@Composable
fun ArticleScreen(
    article: Article?,
    isLoading: Boolean,
) {
    Div(
        attrs = { classes(KotlinLibsStyle.snippedList) },
    ) {
        if (isLoading) {
            Text("Loading")
        }
        if (article != null) {
            Article(article)
        }
    }
}
