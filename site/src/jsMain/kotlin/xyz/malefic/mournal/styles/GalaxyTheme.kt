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
import org.jetbrains.compose.web.css.vh
import xyz.malefic.kutint.rgba

object GalaxyTheme {
    /** Deep purple - primary dark color for backgrounds and depth */
    val deepPurple = rgba(43, 30, 62)

    /** Cosmic blue - secondary color for accents and interactive elements */
    val cosmicBlue = rgba(74, 78, 143)

    /** Lavender - tertiary color for highlights and borders */
    val lavender = rgba(164, 144, 194)

    /** Silver - quaternary color for text and light elements */
    val silver = rgba(230, 230, 250)

    // Component Colors (derived from core colors)
    val pageBackground = deepPurple.dim(0.9f)
    val pagePanel = deepPurple.dim(0.8f)
    val pagePanelMuted = deepPurple.dim(0.75f)
    val panelBorder = cosmicBlue.dim(0.5f)
    val textPrimary = silver.tint(0.05f)
    val textSecondary = silver.desaturate(0.3f).dim(0.2f)

    // Color Variants for Interactive Elements
    val sidebarBackground = deepPurple.dim(0.95f)
    val navItemCurrentBg = cosmicBlue.dim(0.55f)
    val navItemInactiveBg = deepPurple.dim(0.7f)
    val navItemCurrentText = lavender.tint(0.15f)
    val navItemInactiveText = silver.desaturate(0.4f)
    val inputBackground = deepPurple.dim(0.7f)
    val silverAccent = silver.withAlpha(0.15f)

    // Error/Status Colors
    val errorColor = lavender.hueRotate(-60).saturate(0.5f).tint(0.3f)

    // Shadow & Glow Colors (with transparency)
    val lavenderGlow = lavender.withAlpha(0.45f)
    val lavenderShadow = lavender.dim(0.3f).withAlpha(0.25f)
    val cosmicBlueShadow = cosmicBlue.dim(0.4f).withAlpha(0.26f)
    val cosmicBlueGlow = cosmicBlue.withAlpha(0.22f)
    val pageBackgroundFaint = deepPurple.dim(0.9f).withAlpha(0.08f)
    val pageBackgroundFainter = deepPurple.dim(0.9f).withAlpha(0.04f)
    val deepPurpleShadow = deepPurple.dim(0.3f).withAlpha(0.15f)
    val lavenderGradient = lavender.withAlpha(0.18f)
    val cosmicBlueGradient = cosmicBlue.withAlpha(0.26f)

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
