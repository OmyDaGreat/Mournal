package xyz.malefic.mournal

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.ui.modifiers.minHeight
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxSize
import com.varabyte.kobweb.compose.ui.modifiers.background
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.core.App
import com.varabyte.kobweb.silk.SilkApp
import com.varabyte.kobweb.silk.components.layout.Surface
import com.varabyte.kobweb.silk.style.CssStyle
import com.varabyte.kobweb.silk.style.base
import com.varabyte.kobweb.silk.style.toModifier
import org.jetbrains.compose.web.css.vh
import xyz.malefic.mournal.styles.GalaxyTheme

val AppSurfaceStyle =
    CssStyle.base {
        com.varabyte.kobweb.compose.ui.Modifier
            .background(GalaxyTheme.pageBackground)
            .color(GalaxyTheme.textPrimary)
    }

@App
@Composable
fun AppEntry(content: @Composable () -> Unit) {
    SilkApp {
        Surface(AppSurfaceStyle.toModifier().fillMaxSize().minHeight(100.vh)) {
            content()
        }
    }
}
