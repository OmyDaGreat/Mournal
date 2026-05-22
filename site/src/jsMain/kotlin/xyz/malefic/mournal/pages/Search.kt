package xyz.malefic.mournal.pages

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.varabyte.kobweb.compose.css.FontWeight
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.background
import com.varabyte.kobweb.compose.ui.modifiers.border
import com.varabyte.kobweb.compose.ui.modifiers.borderRadius
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.fontFamily
import com.varabyte.kobweb.compose.ui.modifiers.fontSize
import com.varabyte.kobweb.compose.ui.modifiers.fontWeight
import com.varabyte.kobweb.compose.ui.modifiers.gap
import com.varabyte.kobweb.compose.ui.modifiers.letterSpacing
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.onMouseEnter
import com.varabyte.kobweb.compose.ui.modifiers.onMouseLeave
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.transition
import com.varabyte.kobweb.compose.ui.modifiers.width
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.core.Page
import com.varabyte.kobweb.silk.components.layout.SimpleGrid
import com.varabyte.kobweb.silk.components.layout.numColumns
import org.jetbrains.compose.web.attributes.InputType
import org.jetbrains.compose.web.attributes.placeholder
import org.jetbrains.compose.web.css.AnimationTimingFunction
import org.jetbrains.compose.web.css.Color
import org.jetbrains.compose.web.css.LineStyle
import org.jetbrains.compose.web.css.em
import org.jetbrains.compose.web.css.ms
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.H1
import org.jetbrains.compose.web.dom.Input
import org.jetbrains.compose.web.dom.Text
import xyz.malefic.mournal.api.Entry
import xyz.malefic.mournal.api.MainApi
import xyz.malefic.mournal.components.EntryCard
import xyz.malefic.mournal.components.EntryCardVariant
import xyz.malefic.mournal.components.PText
import xyz.malefic.mournal.styles.GalaxyTheme

@Page("/search")
@Composable
fun SearchPage() {
    var allEntries by remember { mutableStateOf<List<Entry>>(emptyList()) }
    var query by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }
    var focusedEntryId by remember { mutableStateOf<Long?>(null) }

    LaunchedEffect(Unit) {
        isLoading = true
        error = null
        allEntries =
            runCatching { MainApi.getHistory() }
                .onFailure { error = it.message ?: "Could not load history." }
                .getOrDefault(emptyList())
        isLoading = false
    }

    val normalizedQuery = query.trim().lowercase()
    val filteredEntries =
        if (normalizedQuery.isBlank()) {
            allEntries
        } else {
            allEntries.filter { entry ->
                entry.id.toString().contains(normalizedQuery) ||
                    entry.author.lowercase().contains(normalizedQuery) ||
                    entry.text.lowercase().contains(normalizedQuery) ||
                    entry.date.lowercase().contains(normalizedQuery) ||
                    (
                        entry.song
                            ?.name
                            ?.lowercase()
                            ?.contains(normalizedQuery) == true
                    ) ||
                    (entry.song?.artists?.any { it.name.lowercase().contains(normalizedQuery) } == true)
            }
        }

    Box(GalaxyTheme.pageFrame, contentAlignment = Alignment.TopCenter) {
        Column(
            GalaxyTheme.centeredColumn
                .padding(top = GalaxyTheme.s(2)),
        ) {
            H1(
                attrs =
                    Modifier
                        .fontWeight(FontWeight.Black)
                        .fontSize(40.px)
                        .margin(0.px)
                        .letterSpacing(.02.em)
                        .toAttrs(),
            ) {
                Text("SEARCH")
            }

            Input(
                type = InputType.Text,
                attrs =
                    Modifier
                        .width(100.percent)
                        .border(1.px, LineStyle.Solid, GalaxyTheme.panelBorder)
                        .borderRadius(6.px)
                        .padding(GalaxyTheme.s(2))
                        .color(GalaxyTheme.textPrimary)
                        .background(Color("#0f1528"))
                        .fontFamily("Inter", "Avenir Next", "Segoe UI", "sans-serif")
                        .fontSize(14.px)
                        .fontWeight(350)
                        .transition {
                            property("border-color", "transform")
                            duration(180.ms)
                            timingFunction(AnimationTimingFunction.Ease)
                        }.toAttrs {
                            placeholder("Search id, author, date, text, song…")
                            value(query)
                            onInput { event -> query = event.value }
                        },
            )

            when {
                isLoading -> {
                    PText(content = "Loading history...")
                }

                error != null -> {
                    PText(content = error ?: "Unknown error")
                }

                filteredEntries.isEmpty() -> {
                    PText(
                        Modifier
                            .color(GalaxyTheme.textSecondary)
                            .fontSize(14.px),
                        "No entries found.",
                    )
                }

                else -> {
                    PText(
                        Modifier
                            .color(GalaxyTheme.textSecondary)
                            .fontSize(13.px)
                            .margin(0.px)
                            .fontWeight(350)
                            .letterSpacing(.04.em),
                        "FOUND ${filteredEntries.size}",
                    )

                    SimpleGrid(
                        numColumns(base = 1, sm = 2, lg = 3, xl = 4),
                        Modifier.width(100.percent).gap(16.px),
                    ) {
                        filteredEntries.forEach { entry ->
                            val isFocused = focusedEntryId == entry.id
                            Box(
                                GalaxyTheme
                                    .interactivePanel(isFocused)
                                    .onMouseEnter { focusedEntryId = entry.id }
                                    .onMouseLeave { focusedEntryId = null },
                            ) {
                                EntryCard(entry, EntryCardVariant.COMPACT)
                            }
                        }
                    }
                }
            }
        }
    }
}
