package xyz.malefic.mournal.styles

import com.varabyte.kobweb.compose.css.Cursor
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.alignItems
import com.varabyte.kobweb.compose.ui.modifiers.background
import com.varabyte.kobweb.compose.ui.modifiers.border
import com.varabyte.kobweb.compose.ui.modifiers.borderRadius
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.cursor
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxSize
import com.varabyte.kobweb.compose.ui.modifiers.fontFamily
import com.varabyte.kobweb.compose.ui.modifiers.fontWeight
import com.varabyte.kobweb.compose.ui.modifiers.gap
import com.varabyte.kobweb.compose.ui.modifiers.letterSpacing
import com.varabyte.kobweb.compose.ui.modifiers.maxWidth
import com.varabyte.kobweb.compose.ui.modifiers.minHeight
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.transform
import com.varabyte.kobweb.compose.ui.modifiers.transition
import com.varabyte.kobweb.compose.ui.modifiers.width
import com.varabyte.kobweb.compose.ui.styleModifier
import org.jetbrains.compose.web.css.AlignItems
import org.jetbrains.compose.web.css.AnimationTimingFunction
import org.jetbrains.compose.web.css.LineStyle
import org.jetbrains.compose.web.css.em
import org.jetbrains.compose.web.css.ms
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.rgba
import org.jetbrains.compose.web.css.vh

object GalaxyTheme {
    // 4 Core Theme Colors (RGBA)
    val deepPurple = rgba(43, 30, 62, 1f)
    val cosmicBlue = rgba(74, 78, 143, 1f)
    val lavender = rgba(164, 144, 194, 1f)
    val silver = rgba(230, 230, 250, 1f)

    // Component Colors (RGBA)
    val pageBackground = rgba(7, 11, 22, 1f)
    val pagePanel = rgba(16, 23, 43, 1f)
    val pagePanelMuted = rgba(12, 19, 36, 1f)
    val panelBorder = rgba(47, 54, 85, 1f)
    val textPrimary = rgba(242, 240, 255, 1f)
    val textSecondary = rgba(174, 181, 211, 1f)

    // Color Variants (RGBA) for Interactive Elements
    val sidebarBackground = rgba(9, 15, 29, 1f)
    val navItemCurrentBg = rgba(32, 42, 74, 1f)
    val navItemInactiveBg = rgba(18, 26, 49, 1f)
    val navItemCurrentText = rgba(247, 244, 255, 1f)
    val navItemInactiveText = rgba(208, 213, 239, 1f)
    val inputBackground = rgba(15, 21, 40, 1f)
    val silverAccent = rgba(230, 230, 250, 0.15f)

    // Error/Status Colors (RGBA)
    val errorColor = rgba(255, 158, 165, 1f)

    // RGBA Color Variants (with transparency for shadows and glows)
    val lavenderGlow = rgba(164, 144, 194, 0.45f)
    val lavenderShadow = rgba(164, 144, 194, 0.25f)
    val cosmicBlueShadow = rgba(74, 78, 143, 0.26f)
    val cosmicBlueGlow = rgba(74, 78, 143, 0.22f)
    val pageBackgroundFaint = rgba(7, 11, 22, 0.08f)
    val pageBackgroundFainter = rgba(7, 11, 22, 0.04f)
    val deepPurpleShadow = rgba(43, 30, 62, 0.15f)
    val lavenderGradient = rgba(164, 144, 194, 0.18f)
    val cosmicBlueGradient = rgba(74, 78, 143, 0.26f)

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
                        "0 0 0 1px $lavenderShadow, 0 14px 28px $cosmicBlueGlow"
                    } else {
                        "0 8px 14px $pageBackgroundFaint"
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
