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
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxSize
import com.varabyte.kobweb.compose.ui.modifiers.fontSize
import com.varabyte.kobweb.compose.ui.modifiers.fontWeight
import com.varabyte.kobweb.compose.ui.modifiers.gap
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.width
import com.varabyte.kobweb.compose.ui.styleModifier
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.core.Page
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.H1
import org.jetbrains.compose.web.dom.P
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Text
import xyz.malefic.mournal.data.DailyMaleficApi
import xyz.malefic.mournal.data.Entry
import xyz.malefic.mournal.data.todayIsoDate
import xyz.malefic.mournal.styles.GalaxyTheme

@Page
@Composable
fun HomePage() {
    var entries by mutableStateOf<List<Entry>>(emptyList())
    var error by mutableStateOf<String?>(null)
    var isLoading by mutableStateOf(true)
    var focusedEntryId by mutableStateOf<String?>(null)
    val today = todayIsoDate()

    LaunchedEffect(Unit) {
        isLoading = true
        error = null
        entries =
            runCatching { DailyMaleficApi.getEntriesForDate(today) }
                .onFailure { error = it.message ?: "Could not load today's entries." }
                .getOrDefault(emptyList())
        isLoading = false
    }

    LaunchedEffect(entries) {
        if (entries.isNotEmpty() && entries.none { it.id == focusedEntryId }) {
            focusedEntryId = entries.first().id
        }
    }

    Box(
        GalaxyTheme.pageFrame,
        contentAlignment = Alignment.TopCenter,
    ) {
        when {
            isLoading -> {
                Column(
                    GalaxyTheme.centeredColumn.padding(top = GalaxyTheme.s(2)),
                ) {
                    P { Text("Loading today's transmissions...") }
                }
            }

            error != null -> {
                Column(
                    GalaxyTheme.centeredColumn.padding(top = GalaxyTheme.s(2)),
                ) {
                    P { Text(error ?: "Unknown error") }
                }
            }

            entries.isEmpty() -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Span(
                        attrs =
                            Modifier
                                .fontSize(220.px)
                                .fontWeight(FontWeight.Black)
                                .color(GalaxyTheme.lavender)
                                .styleModifier {
                                    property("line-height", "0.9")
                                    property("opacity", "0.92")
                                    property("text-shadow", "0 0 34px rgba(164, 144, 194, 0.45)")
                                }.toAttrs(),
                    ) {
                        Text("?")
                    }
                }
            }

            else -> {
                Column(
                    GalaxyTheme.centeredColumn.padding(top = GalaxyTheme.s(2)),
                ) {
                    H1(
                        attrs =
                            Modifier
                                .fontWeight(FontWeight.Black)
                                .fontSize(44.px)
                                .styleModifier {
                                    property("letter-spacing", "0.02em")
                                    property("margin", "0")
                                }.toAttrs(),
                    ) {
                        Text("TODAY")
                    }

                    P(
                        attrs =
                            Modifier
                                .color(GalaxyTheme.textSecondary)
                                .fontSize(14.px)
                                .styleModifier {
                                    property("margin", "0")
                                    property("font-weight", "350")
                                    property("letter-spacing", "0.02em")
                                }.toAttrs(),
                    ) {
                        Text(today)
                    }

                    Div(
                        attrs =
                            Modifier
                                .width(100.percent)
                                .padding(top = GalaxyTheme.s(2))
                                .styleModifier {
                                    property("display", "grid")
                                    property("grid-template-columns", "repeat(auto-fit, minmax(260px, 1fr))")
                                    property("gap", "16px")
                                    property("align-items", "start")
                                }.toAttrs(),
                    ) {
                        entries.forEach { entry ->
                            val isFocused = focusedEntryId == entry.id
                            Div(
                                attrs =
                                    GalaxyTheme
                                        .interactivePanel(isFocused)
                                        .styleModifier {
                                            property("min-height", "200px")
                                            property("display", "flex")
                                            property("flex-direction", "column")
                                            property("justify-content", "space-between")
                                        }.toAttrs {
                                            onMouseEnter { focusedEntryId = entry.id }
                                        },
                            ) {
                                EntryCardContent(entry)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun EntryCardContent(entry: Entry) {
    Column(Modifier.gap(GalaxyTheme.s(1))) {
        P(
            attrs =
                Modifier
                    .fontWeight(FontWeight.Bold)
                    .styleModifier {
                        property("margin", "0")
                        property("font-size", "15px")
                        property("letter-spacing", "0.02em")
                    }.toAttrs(),
        ) {
            Text(entry.author)
        }
        P(
            attrs =
                Modifier
                    .color(GalaxyTheme.textSecondary)
                    .styleModifier {
                        property("margin", "0")
                        property("font-size", "13px")
                        property("font-weight", "350")
                        property("letter-spacing", "0.04em")
                    }.toAttrs(),
        ) {
            Text(entry.date)
        }
        P(
            attrs =
                Modifier
                    .styleModifier {
                        property("margin", "0")
                        property("font-size", "15px")
                        property("font-weight", "350")
                        property("line-height", "1.5")
                    }.toAttrs(),
        ) {
            Text(entry.text)
        }
        entry.song?.let { song ->
            P(
                attrs =
                    Modifier
                        .color(GalaxyTheme.lavender)
                        .styleModifier {
                            property("margin", "0")
                            property("font-size", "13px")
                            property("font-weight", "500")
                            property("letter-spacing", "0.01em")
                        }.toAttrs(),
            ) {
                Text("♪ ${song.name} — ${song.artists.joinToString { it.name }}")
            }
        }
    }
}
