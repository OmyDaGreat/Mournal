package xyz.malefic.mournal.styles

import com.varabyte.kobweb.compose.css.Cursor
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.background
import com.varabyte.kobweb.compose.ui.modifiers.border
import com.varabyte.kobweb.compose.ui.modifiers.borderRadius
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.cursor
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxSize
import com.varabyte.kobweb.compose.ui.modifiers.fontFamily
import com.varabyte.kobweb.compose.ui.modifiers.fontWeight
import com.varabyte.kobweb.compose.ui.modifiers.gap
import com.varabyte.kobweb.compose.ui.modifiers.alignItems
import com.varabyte.kobweb.compose.ui.modifiers.letterSpacing
import com.varabyte.kobweb.compose.ui.modifiers.maxWidth
import com.varabyte.kobweb.compose.ui.modifiers.minHeight
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.transform
import com.varabyte.kobweb.compose.ui.modifiers.transition
import com.varabyte.kobweb.compose.ui.modifiers.width
import com.varabyte.kobweb.compose.ui.styleModifier
import org.jetbrains.compose.web.css.AnimationTimingFunction
import org.jetbrains.compose.web.css.Color
import org.jetbrains.compose.web.css.LineStyle
import org.jetbrains.compose.web.css.em
import org.jetbrains.compose.web.css.ms
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.vh
import org.jetbrains.compose.web.css.AlignItems

object GalaxyTheme {
    val deepPurple = Color("#2b1e3e")
    val cosmicBlue = Color("#4a4e8f")
    val lavender = Color("#a490c2")
    val silver = Color("#e6e6fa")

    val pageBackground = Color("#070b16")
    val pagePanel = Color("#10172b")
    val pagePanelMuted = Color("#0c1324")
    val panelBorder = Color("#2f3655")
    val textPrimary = Color("#f2f0ff")
    val textSecondary = Color("#aeb5d3")

    fun s(step: Int) = (step * 8).px

    val pageFrame =
        Modifier
            .fillMaxSize()
            .minHeight(100.vh)
            .background(pageBackground)
            .padding(s(4))

    val centeredColumn =
        Modifier
            .width(100.percent)
            .maxWidth(1120.px)
            .alignItems(AlignItems.Center)
            .gap(s(2))

    val panelModifier =
        Modifier
            .background(pagePanel)
            .border(1.px, LineStyle.Solid, panelBorder)
            .borderRadius(8.px)
            .padding(s(2))

    fun interactivePanel(isFocused: Boolean): Modifier =
        Modifier
            .background(pagePanel)
            .border(1.px, LineStyle.Solid, if (isFocused) lavender else panelBorder)
            .borderRadius(8.px)
            .padding(s(2))
            .cursor(Cursor.Pointer)
            .transform { if (isFocused) scale(1.03) else scale(.95) }
            .transition {
                property("transform", "border-color", "box-shadow")
                duration(180.ms)
                timingFunction(AnimationTimingFunction.Ease)
            }.styleModifier {
                property(
                    "box-shadow",
                    if (isFocused) {
                        "0 0 0 1px rgba(164, 144, 194, 0.35), 0 14px 28px rgba(74, 78, 143, 0.22)"
                    } else {
                        "0 8px 14px rgba(3, 5, 11, 0.35)"
                    },
                )
            }

    fun actionButtonModifier(isPrimary: Boolean = false): Modifier =
        Modifier
            .background(if (isPrimary) cosmicBlue else pagePanelMuted)
            .border(1.px, LineStyle.Solid, if (isPrimary) lavender else panelBorder)
            .borderRadius(6.px)
            .padding(s(1), s(2))
            .color(textPrimary)
            .cursor(Cursor.Pointer)
            .fontFamily("Inter", "Avenir Next", "Segoe UI", "sans-serif")
            .fontWeight(600)
            .letterSpacing(.02.em)
            .transform { scale(.94) }
            .transition {
                property("transform", "background-color", "border-color")
                duration(180.ms)
                timingFunction(AnimationTimingFunction.Ease)
            }
}
