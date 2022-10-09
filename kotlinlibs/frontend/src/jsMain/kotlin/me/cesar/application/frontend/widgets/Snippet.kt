package me.cesar.application.frontend.widgets

import androidx.compose.runtime.Composable
import me.cesar.application.common.model.Article
import me.cesar.application.frontend.theme.KotlinLibsStyle
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.H3
import org.jetbrains.compose.web.dom.Img
import org.jetbrains.compose.web.dom.Text

/**
 * @author cramsan
 */
@Composable
fun Snippet(article: Article) {
    Div(
        attrs = { classes(KotlinLibsStyle.snippet) },
    ) {
        Img(
            src = article.bannerUrl,
            attrs = { classes(KotlinLibsStyle.thumbnail) },
        )
        Div(
            attrs = { classes(KotlinLibsStyle.snippetContent) },
        ) {
            H3 {
                Text(article.title)
            }
            Text(article.content)
        }
    }
}
