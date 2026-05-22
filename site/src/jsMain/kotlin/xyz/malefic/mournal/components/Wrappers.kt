package xyz.malefic.mournal.components

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.toAttrs
import org.jetbrains.compose.web.dom.P
import org.jetbrains.compose.web.dom.Text

@Composable
fun PText(
    modifier: Modifier = Modifier,
    content: String,
) {
    P(attrs = modifier.toAttrs()) {
        Text(content)
    }
}
