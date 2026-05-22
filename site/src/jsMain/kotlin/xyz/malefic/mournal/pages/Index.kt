package xyz.malefic.mournal.pages

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.varabyte.kobweb.compose.css.FontWeight
import com.varabyte.kobweb.compose.css.TextShadow
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxSize
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.fontSize
import com.varabyte.kobweb.compose.ui.modifiers.fontWeight
import com.varabyte.kobweb.compose.ui.modifiers.letterSpacing
import com.varabyte.kobweb.compose.ui.modifiers.lineHeight
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.minHeight
import com.varabyte.kobweb.compose.ui.modifiers.onMouseEnter
import com.varabyte.kobweb.compose.ui.modifiers.onMouseLeave
import com.varabyte.kobweb.compose.ui.modifiers.opacity
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.textShadow
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.core.Page
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import org.jetbrains.compose.web.css.em
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.rgba
import org.jetbrains.compose.web.dom.H1
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Text
import xyz.malefic.mournal.api.Entry
import xyz.malefic.mournal.api.MainApi
import xyz.malefic.mournal.components.EntryCard
import xyz.malefic.mournal.components.EntryCardVariant
import xyz.malefic.mournal.components.PText
import xyz.malefic.mournal.styles.GalaxyTheme
import kotlin.time.Duration.Companion.seconds

@Page
@Composable
fun HomePage() {
    var entries by remember { mutableStateOf<List<Entry>>(emptyList()) }
    var error by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var focusedEntryId by remember { mutableStateOf<Long?>(null) }

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

    Box(
        GalaxyTheme.pageFrame,
        contentAlignment = Alignment.TopCenter,
    ) {
        when {
            error != null -> {
                Column(
                    GalaxyTheme.centeredColumn.padding(top = GalaxyTheme.s(2)),
                ) {
                    Text(error!!)
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

                    PText(
                        Modifier
                            .color(GalaxyTheme.textSecondary)
                            .fontSize(14.px)
                            .margin(0.px)
                            .fontWeight(350)
                            .letterSpacing(.02.em),
                        entries.first().date,
                    )

                    Column(
                        Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(16.px),
                    ) {
                        entries.forEach { entry ->
                            val isFocused = focusedEntryId == entry.id
                            Box(
                                GalaxyTheme
                                    .interactivePanel(isFocused)
                                    .minHeight(200.px)
                                    .fillMaxWidth()
                                    .onMouseEnter { focusedEntryId = entry.id }
                                    .onMouseLeave { focusedEntryId = null },
                            ) {
                                EntryCard(entry, EntryCardVariant.DEFAULT)
                            }
                        }
                    }
                }
            }
        }
    }
}
