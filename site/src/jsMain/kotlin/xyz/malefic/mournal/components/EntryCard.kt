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
import org.jetbrains.compose.web.css.em
import org.jetbrains.compose.web.css.px
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
                PText(
                    Modifier
                        .fontWeight(FontWeight.Bold)
                        .margin(0.px)
                        .fontSize(15.px)
                        .letterSpacing(.02.em),
                    entry.author,
                )

                PText(
                    Modifier
                        .color(GalaxyTheme.textSecondary)
                        .margin(0.px)
                        .fontSize(13.px)
                        .fontWeight(350)
                        .letterSpacing(.04.em),
                    entry.date,
                )
            }

            EntryCardVariant.COMPACT -> {
                PText(
                    Modifier
                        .fontWeight(FontWeight.Bold)
                        .margin(0.px)
                        .fontSize(14.px)
                        .letterSpacing(.02.em),
                    "${entry.author} · ${entry.date} · #${entry.id}",
                )
            }
        }

        PText(
            Modifier
                .margin(0.px)
                .fontSize(15.px)
                .fontWeight(350)
                .lineHeight(1.5),
            entry.text,
        )

        entry.song?.let { song ->
            PText(
                Modifier
                    .color(GalaxyTheme.lavender)
                    .margin(0.px)
                    .fontSize(13.px)
                    .fontWeight(500)
                    .letterSpacing(.01.em),
                "♪ ${song.name} — ${
                    song.artists.joinToString { it.name }
                        .replaceAfterLast(", ", " & ${entry.song.artists.last()}")
                }",
            )
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
