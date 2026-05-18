package xyz.malefic.mournal.components.layouts

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.varabyte.kobweb.compose.css.BoxShadow
import com.varabyte.kobweb.compose.css.Cursor
import com.varabyte.kobweb.compose.css.Transition
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.alignItems
import com.varabyte.kobweb.compose.ui.modifiers.background
import com.varabyte.kobweb.compose.ui.modifiers.border
import com.varabyte.kobweb.compose.ui.modifiers.borderRadius
import com.varabyte.kobweb.compose.ui.modifiers.boxShadow
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.cursor
import com.varabyte.kobweb.compose.ui.modifiers.display
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxHeight
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxSize
import com.varabyte.kobweb.compose.ui.modifiers.flexGrow
import com.varabyte.kobweb.compose.ui.modifiers.fontFamily
import com.varabyte.kobweb.compose.ui.modifiers.fontSize
import com.varabyte.kobweb.compose.ui.modifiers.fontWeight
import com.varabyte.kobweb.compose.ui.modifiers.gap
import com.varabyte.kobweb.compose.ui.modifiers.height
import com.varabyte.kobweb.compose.ui.modifiers.justifyContent
import com.varabyte.kobweb.compose.ui.modifiers.letterSpacing
import com.varabyte.kobweb.compose.ui.modifiers.lineHeight
import com.varabyte.kobweb.compose.ui.modifiers.minHeight
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.transform
import com.varabyte.kobweb.compose.ui.modifiers.transition
import com.varabyte.kobweb.compose.ui.modifiers.width
import com.varabyte.kobweb.compose.ui.styleModifier
import com.varabyte.kobweb.core.layout.Layout
import com.varabyte.kobweb.core.rememberPageContext
import com.varabyte.kobweb.silk.components.navigation.Link
import org.jetbrains.compose.web.css.AlignItems
import org.jetbrains.compose.web.css.AnimationTimingFunction
import org.jetbrains.compose.web.css.Color
import org.jetbrains.compose.web.css.DisplayStyle
import org.jetbrains.compose.web.css.JustifyContent
import org.jetbrains.compose.web.css.LineStyle
import org.jetbrains.compose.web.css.em
import org.jetbrains.compose.web.css.ms
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.rgba
import org.jetbrains.compose.web.css.vh
import org.jetbrains.compose.web.dom.Div
import xyz.malefic.mournal.components.invoke
import xyz.malefic.mournal.styles.GalaxyTheme
import xyz.malefic.mournal.util.Pages

@Layout
@Composable
fun NavBarLayout(content: @Composable () -> Unit) {
    val ctx = rememberPageContext()
    val currentRoute = ctx.route.path
    var focusedRoute by remember(currentRoute) { mutableStateOf(currentRoute.ifBlank { "/" }) }

    Row(
        Modifier
            .fillMaxSize()
            .minHeight(100.vh),
    ) {
        Box(
            Modifier
                .fillMaxHeight()
                .minHeight(100.vh)
                .width(72.px)
                .background(Color("#090f1d"))
                .border(1.px, LineStyle.Solid, Color("#242d4a"))
                .styleModifier { property("border-left", "none") }
                .padding(topBottom = GalaxyTheme.s(3), leftRight = GalaxyTheme.s(1)),
            contentAlignment = Alignment.TopCenter,
        ) {
            Column(Modifier.gap(GalaxyTheme.s(1)), horizontalAlignment = Alignment.CenterHorizontally) {
                Pages.entries.forEach { page ->
                    val route = page.route
                    val isCurrent = page.isCurrentPage(currentRoute)
                    val isFocused = route == focusedRoute || isCurrent

                    Div(
                        attrs = {
                            title(page.value)
                            onMouseEnter { focusedRoute = route }
                            onMouseLeave { focusedRoute = currentRoute.ifBlank { "/" } }
                        },
                    ) {
                        Link(
                            path = route,
                            modifier =
                                Modifier
                                    .width(48.px)
                                    .height(48.px)
                                    .background(if (isCurrent) Color("#202a4a") else Color("#121a31"))
                                    .border(1.px, LineStyle.Solid, if (isFocused) Color("#a490c2") else Color("#2f3655"))
                                    .borderRadius(6.px)
                                    .color(if (isCurrent) Color("#f7f4ff") else Color("#d0d5ef"))
                                    .fontWeight(if (isCurrent) 700 else 500)
                                    .fontSize(14.px)
                                    .cursor(Cursor.Pointer)
                                    .display(DisplayStyle.Flex)
                                    .alignItems(AlignItems.Center)
                                    .justifyContent(JustifyContent.Center)
                                    .styleModifier {
                                        property("text-decoration", "none")
                                    }.letterSpacing(.08.em)
                                    .transform { if (isFocused) scale(1.08) else scale(0.88) }
                                    .boxShadow(
                                        BoxShadow
                                            .of(0.px, 0.px, 0.px, 1.px, rgba(164, 144, 194, .25f))
                                            .takeUnless { isFocused }
                                            ?: BoxShadow.of(0.px, 6.px, 10.px, color = rgba(0, 0, 0, .28f)),
                                    ).transition(
                                        Transition.of("transform", 180.ms, AnimationTimingFunction.Ease),
                                        Transition.of("box-shadow", 180.ms, AnimationTimingFunction.Ease),
                                        Transition.of("border-color", 180.ms, AnimationTimingFunction.Ease),
                                    ),
                        ) {
                            page.icon()
                        }
                    }
                }
            }
        }

        Box(
            Modifier
                .flexGrow(1)
                .fillMaxHeight()
                .padding(leftRight = GalaxyTheme.s(4), topBottom = GalaxyTheme.s(4))
                .fontFamily("Inter", "Avenir Next", "Segoe UI", "sans-serif")
                .lineHeight(1.45)
                .styleModifier {
                    property(
                        "background-image",
                        "radial-gradient(circle at 12% 18%, rgba(74, 78, 143, 0.26) 0%, rgba(7, 11, 22, 0.08) 36%), " +
                            "radial-gradient(circle at 78% 8%, rgba(164, 144, 194, 0.18) 0%, rgba(7, 11, 22, 0.04) 42%)",
                    )
                },
            contentAlignment = Alignment.TopCenter,
        ) {
            content()
        }
    }
}

private fun Pages.isCurrentPage(currentRoute: String): Boolean =
    when (this) {
        Pages.INDEX -> currentRoute == "" || currentRoute == "/"
        else -> currentRoute == route
    }
