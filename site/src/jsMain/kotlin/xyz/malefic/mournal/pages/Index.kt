package xyz.malefic.mournal.pages

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.varabyte.kobweb.compose.css.FontWeight
import com.varabyte.kobweb.compose.css.JustifyContent
import com.varabyte.kobweb.compose.css.TextShadow
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.alignItems
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.display
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxSize
import com.varabyte.kobweb.compose.ui.modifiers.flexDirection
import com.varabyte.kobweb.compose.ui.modifiers.fontSize
import com.varabyte.kobweb.compose.ui.modifiers.fontWeight
import com.varabyte.kobweb.compose.ui.modifiers.gap
import com.varabyte.kobweb.compose.ui.modifiers.justifyContent
import com.varabyte.kobweb.compose.ui.modifiers.letterSpacing
import com.varabyte.kobweb.compose.ui.modifiers.lineHeight
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.minHeight
import com.varabyte.kobweb.compose.ui.modifiers.opacity
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.textShadow
import com.varabyte.kobweb.compose.ui.modifiers.width
import com.varabyte.kobweb.compose.ui.styleModifier
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.core.Page
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import org.jetbrains.compose.web.css.AlignItems
import org.jetbrains.compose.web.css.DisplayStyle
import org.jetbrains.compose.web.css.FlexDirection
import org.jetbrains.compose.web.css.em
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.rgba
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.H1
import org.jetbrains.compose.web.dom.P
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Text
import xyz.malefic.mournal.api.Entry
import xyz.malefic.mournal.api.MainApi
import xyz.malefic.mournal.styles.GalaxyTheme
import kotlin.time.Duration.Companion.seconds

@Page
@Composable
fun HomePage() {
    var entries by mutableStateOf<List<Entry>>(emptyList())
    var error by mutableStateOf<String?>(null)
    var isLoading by mutableStateOf(true)
    var focusedEntryId by mutableStateOf<Long?>(null)

    LaunchedEffect(Unit) {
        isLoading = true
        while (isActive) {
            runCatching { MainApi.getLatestEntries() }
                .onSuccess {
                    entries = it
                    error = null
                }.onFailure { error = "Could not load the latest entries: ${it.message}" }
            isLoading = false
            delay(15.seconds)
        }
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
            error != null -> {
                Column(
                    GalaxyTheme.centeredColumn.padding(top = GalaxyTheme.s(2)),
                ) {
                    P { Text(error!!) }
                }
            }

            isLoading || entries.isEmpty() -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Span(
                        attrs =
                            Modifier
                                .fontSize(220.px)
                                .fontWeight(FontWeight.Black)
                                .color(GalaxyTheme.lavender)
                                .lineHeight(0.9)
                                .opacity(0.92)
                                .textShadow(TextShadow.of(0.px, 0.px, 34.px, color = rgba(164, 144, 194, .45f)))
                                .toAttrs(),
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
                                .letterSpacing(.02.em)
                                .margin(0.px)
                                .toAttrs(),
                    ) {
                        Text("LATEST ENTRIES")
                    }

                    P(
                        attrs =
                            Modifier
                                .color(GalaxyTheme.textSecondary)
                                .fontSize(14.px)
                                .margin(0.px)
                                .fontWeight(350)
                                .letterSpacing(.02.em)
                                .toAttrs(),
                    ) {
                        Text(entries.first().date)
                    }

                    Div(
                        attrs =
                            Modifier
                                .width(100.percent)
                                .padding(top = GalaxyTheme.s(2))
                                .display(DisplayStyle.Grid)
                                .styleModifier {
                                    property("grid-template-columns", "repeat(auto-fit, minmax(260px, 1fr))")
                                }.gap(16.px)
                                .alignItems(AlignItems.Start)
                                .toAttrs(),
                    ) {
                        entries.forEach { entry ->
                            val isFocused = focusedEntryId == entry.id
                            Div(
                                attrs =
                                    GalaxyTheme
                                        .interactivePanel(isFocused)
                                        .minHeight(200.px)
                                        .display(DisplayStyle.Flex)
                                        .flexDirection(FlexDirection.Column)
                                        .justifyContent(JustifyContent.SpaceBetween)
                                        .toAttrs {
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
                Text("♪ ${song.name} — ${song.artists.joinToString{ it.name }.replaceAfterLast(", ", " & ${entry.song.artists.last()}")}")
            }
        }
    }
}
