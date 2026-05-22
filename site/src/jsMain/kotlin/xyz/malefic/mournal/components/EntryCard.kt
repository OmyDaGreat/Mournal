package xyz.malefic.mournal.components

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.css.FontWeight
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxSize
import com.varabyte.kobweb.compose.ui.modifiers.fontSize
import com.varabyte.kobweb.compose.ui.modifiers.fontWeight
import com.varabyte.kobweb.compose.ui.modifiers.gap
import com.varabyte.kobweb.compose.ui.modifiers.letterSpacing
import com.varabyte.kobweb.compose.ui.modifiers.lineHeight
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.toAttrs
import org.jetbrains.compose.web.css.em
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.P
import org.jetbrains.compose.web.dom.Text
import xyz.malefic.mournal.api.Entry
import xyz.malefic.mournal.styles.GalaxyTheme

enum class EntryCardVariant {
    DEFAULT,
    COMPACT,
}

@Composable
fun EntryCard(
    entry: Entry,
    variant: EntryCardVariant = EntryCardVariant.DEFAULT,
    modifier: Modifier = Modifier,
    actionButtons: @Composable (() -> Unit)? = null,
) {
    Column(modifier.gap(GalaxyTheme.s(1))) {
        when (variant) {
            EntryCardVariant.DEFAULT -> {
                P(
                    attrs =
                        Modifier
                            .fontWeight(FontWeight.Bold)
                            .margin(0.px)
                            .fontSize(15.px)
                            .letterSpacing(.02.em)
                            .toAttrs(),
                ) {
                    Text(entry.author)
                }
                P(
                    attrs =
                        Modifier
                            .color(GalaxyTheme.textSecondary)
                            .margin(0.px)
                            .fontSize(13.px)
                            .fontWeight(350)
                            .letterSpacing(.04.em)
                            .toAttrs(),
                ) {
                    Text(entry.date)
                }
            }

            EntryCardVariant.COMPACT -> {
                P(
                    attrs =
                        Modifier
                            .fontWeight(FontWeight.Bold)
                            .margin(0.px)
                            .fontSize(14.px)
                            .letterSpacing(.02.em)
                            .toAttrs(),
                ) {
                    Text("${entry.author} · ${entry.date} · #${entry.id}")
                }
            }
        }

        P(
            attrs =
                Modifier
                    .margin(0.px)
                    .fontSize(15.px)
                    .fontWeight(350)
                    .lineHeight(1.5)
                    .toAttrs(),
        ) {
            Text(entry.text)
        }

        entry.song?.let { song ->
            P(
                attrs =
                    Modifier
                        .color(GalaxyTheme.lavender)
                        .margin(0.px)
                        .fontSize(13.px)
                        .fontWeight(500)
                        .letterSpacing(.01.em)
                        .toAttrs(),
            ) {
                Text(
                    "♪ ${song.name} — ${
                        song.artists.joinToString { it.name }
                            .replaceAfterLast(", ", " & ${entry.song.artists.last()}")
                    }",
                )
            }
        }

        if (actionButtons != null) {
            Box(Modifier.fillMaxSize()) {
                Row(Modifier.align(Alignment.BottomEnd)) {
                    actionButtons()
                }
            }
        }
    }
}
