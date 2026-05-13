package xyz.malefic.mournal.components.layouts

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.varabyte.kobweb.compose.css.Cursor
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.background
import com.varabyte.kobweb.compose.ui.modifiers.border
import com.varabyte.kobweb.compose.ui.modifiers.borderRadius
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.cursor
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxHeight
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxSize
import com.varabyte.kobweb.compose.ui.modifiers.flexGrow
import com.varabyte.kobweb.compose.ui.modifiers.fontSize
import com.varabyte.kobweb.compose.ui.modifiers.fontWeight
import com.varabyte.kobweb.compose.ui.modifiers.gap
import com.varabyte.kobweb.compose.ui.modifiers.height
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.width
import com.varabyte.kobweb.compose.ui.styleModifier
import com.varabyte.kobweb.core.layout.Layout
import com.varabyte.kobweb.core.rememberPageContext
import com.varabyte.kobweb.silk.components.navigation.Link
import org.jetbrains.compose.web.css.Color
import org.jetbrains.compose.web.css.LineStyle
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Text
import xyz.malefic.mournal.styles.GalaxyTheme
import xyz.malefic.mournal.util.Pages

@Layout
@Composable
fun NavBarLayout(content: @Composable () -> Unit) {
    val ctx = rememberPageContext()
    val currentRoute = ctx.route.path
    var focusedRoute by mutableStateOf(currentRoute.ifBlank { "/" })

    Row(Modifier.fillMaxSize()) {
        Box(
            Modifier
                .fillMaxHeight()
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
                    val shortLabel = page.value.take(1).uppercase()

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
                                    .styleModifier {
                                        property("display", "flex")
                                        property("align-items", "center")
                                        property("justify-content", "center")
                                        property("text-decoration", "none")
                                        property("letter-spacing", "0.08em")
                                        property("transform", if (isFocused) "scale(1.08)" else "scale(0.88)")
                                        property(
                                            "box-shadow",
                                            if (isFocused) {
                                                "0 0 0 1px rgba(164,144,194,0.25), 0 10px 20px rgba(33,40,76,0.35)"
                                            } else {
                                                "0 6px 10px rgba(0,0,0,0.28)"
                                            },
                                        )
                                        property("transition", "transform 180ms ease, box-shadow 180ms ease, border-color 180ms ease")
                                    },
                        ) {
                            Text(shortLabel)
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
                .styleModifier {
                    property("font-family", "'Inter', 'Avenir Next', 'Segoe UI', sans-serif")
                    property("line-height", "1.45")
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
