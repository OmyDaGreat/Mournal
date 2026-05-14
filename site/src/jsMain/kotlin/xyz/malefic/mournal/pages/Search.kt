package xyz.malefic.mournal.pages

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
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
import com.varabyte.kobweb.compose.ui.modifiers.display
import com.varabyte.kobweb.compose.ui.modifiers.fontFamily
import com.varabyte.kobweb.compose.ui.modifiers.fontSize
import com.varabyte.kobweb.compose.ui.modifiers.fontWeight
import com.varabyte.kobweb.compose.ui.modifiers.gap
import com.varabyte.kobweb.compose.ui.modifiers.letterSpacing
import com.varabyte.kobweb.compose.ui.modifiers.lineHeight
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.transition
import com.varabyte.kobweb.compose.ui.modifiers.width
import com.varabyte.kobweb.compose.ui.styleModifier
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.core.Page
import org.jetbrains.compose.web.attributes.InputType
import org.jetbrains.compose.web.attributes.placeholder
import org.jetbrains.compose.web.css.AnimationTimingFunction
import org.jetbrains.compose.web.css.Color
import org.jetbrains.compose.web.css.DisplayStyle
import org.jetbrains.compose.web.css.LineStyle
import org.jetbrains.compose.web.css.em
import org.jetbrains.compose.web.css.ms
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.H1
import org.jetbrains.compose.web.dom.Input
import org.jetbrains.compose.web.dom.P
import org.jetbrains.compose.web.dom.Text
import xyz.malefic.mournal.api.DailyMaleficApi
import xyz.malefic.mournal.api.Entry
import xyz.malefic.mournal.styles.GalaxyTheme

@Page("/search")
@Composable
fun SearchPage() {
    var allEntries by mutableStateOf<List<Entry>>(emptyList())
    var query by mutableStateOf("")
    var isLoading by mutableStateOf(true)
    var error by mutableStateOf<String?>(null)
    var focusedEntryId by mutableStateOf<Long?>(null)

    LaunchedEffect(Unit) {
        isLoading = true
        error = null
        allEntries =
            runCatching { DailyMaleficApi.getHistory() }
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

    LaunchedEffect(filteredEntries) {
        if (filteredEntries.isNotEmpty() && filteredEntries.none { it.id == focusedEntryId }) {
            focusedEntryId = filteredEntries.first().id
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
                    P { Text("Loading history...") }
                }

                error != null -> {
                    P { Text(error ?: "Unknown error") }
                }

                filteredEntries.isEmpty() -> {
                    P(
                        attrs =
                            Modifier
                                .color(GalaxyTheme.textSecondary)
                                .fontSize(14.px)
                                .toAttrs(),
                    ) {
                        Text("No entries found.")
                    }
                }

                else -> {
                    P(
                        attrs =
                            Modifier
                                .color(GalaxyTheme.textSecondary)
                                .fontSize(13.px)
                                .margin(0.px)
                                .fontWeight(350)
                                .letterSpacing(.04.em)
                                .toAttrs(),
                    ) {
                        Text("FOUND ${filteredEntries.size}")
                    }

                    Div(
                        attrs =
                            Modifier
                                .width(100.percent)
                                .display(DisplayStyle.Grid)
                                .styleModifier {
                                    property("grid-template-columns", "repeat(auto-fit, minmax(280px, 1fr))")
                                }.gap(16.px)
                                .toAttrs(),
                    ) {
                        filteredEntries.forEach { entry ->
                            val isFocused = focusedEntryId == entry.id
                            Div(
                                attrs =
                                    GalaxyTheme
                                        .interactivePanel(isFocused)
                                        .toAttrs {
                                            onMouseEnter { focusedEntryId = entry.id }
                                        },
                            ) {
                                Column(Modifier.gap(GalaxyTheme.s(1))) {
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
                                    P(
                                        attrs =
                                            Modifier
                                                .margin(0.px)
                                                .fontSize(14.px)
                                                .fontWeight(350)
                                                .lineHeight(1.45)
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
                                                    .fontSize(12.px)
                                                    .fontWeight(500)
                                                    .toAttrs(),
                                        ) {
                                            Text("♪ ${song.name}")
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
