package xyz.malefic.mournal.pages

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.varabyte.kobweb.compose.css.Cursor
import com.varabyte.kobweb.compose.css.FontWeight
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.background
import com.varabyte.kobweb.compose.ui.modifiers.border
import com.varabyte.kobweb.compose.ui.modifiers.borderRadius
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.cursor
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxSize
import com.varabyte.kobweb.compose.ui.modifiers.fontFamily
import com.varabyte.kobweb.compose.ui.modifiers.fontSize
import com.varabyte.kobweb.compose.ui.modifiers.fontWeight
import com.varabyte.kobweb.compose.ui.modifiers.gap
import com.varabyte.kobweb.compose.ui.modifiers.letterSpacing
import com.varabyte.kobweb.compose.ui.modifiers.lineHeight
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.minHeight
import com.varabyte.kobweb.compose.ui.modifiers.onMouseEnter
import com.varabyte.kobweb.compose.ui.modifiers.onMouseLeave
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.scale
import com.varabyte.kobweb.compose.ui.modifiers.transform
import com.varabyte.kobweb.compose.ui.modifiers.transition
import com.varabyte.kobweb.compose.ui.modifiers.width
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.core.Page
import com.varabyte.kobweb.silk.components.layout.SimpleGrid
import com.varabyte.kobweb.silk.components.layout.numColumns
import kotlinx.coroutines.launch
import org.jetbrains.compose.web.attributes.InputType
import org.jetbrains.compose.web.attributes.placeholder
import org.jetbrains.compose.web.css.AnimationTimingFunction
import org.jetbrains.compose.web.css.Color
import org.jetbrains.compose.web.css.LineStyle
import org.jetbrains.compose.web.css.em
import org.jetbrains.compose.web.css.ms
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.Button
import org.jetbrains.compose.web.dom.H1
import org.jetbrains.compose.web.dom.H3
import org.jetbrains.compose.web.dom.Input
import org.jetbrains.compose.web.dom.P
import org.jetbrains.compose.web.dom.Text
import org.jetbrains.compose.web.dom.TextArea
import xyz.malefic.mournal.api.Entry
import xyz.malefic.mournal.api.EntryPayload
import xyz.malefic.mournal.api.MainApi
import xyz.malefic.mournal.api.readSavedApiKey
import xyz.malefic.mournal.api.saveApiKey
import xyz.malefic.mournal.api.todayDate
import xyz.malefic.mournal.components.Icon
import xyz.malefic.mournal.components.invoke
import xyz.malefic.mournal.styles.GalaxyTheme

@Page("/manage")
@Composable
fun ManagePage() {
    val scope = rememberCoroutineScope()

    var apiKeyDraft by remember { mutableStateOf(readSavedApiKey()) }
    var activeApiKey by remember { mutableStateOf(readSavedApiKey()) }
    var entries by remember { mutableStateOf<List<Entry>>(emptyList()) }
    var status by remember { mutableStateOf<String?>(null) }
    var error by remember { mutableStateOf<String?>(null) }
    var focusedEntryId by remember { mutableStateOf<Long?>(null) }

    var editingEntryId by remember { mutableStateOf<Long?>(null) }
    var formAuthor by remember { mutableStateOf("") }
    var formText by remember { mutableStateOf("") }
    var formDate by remember { mutableStateOf(todayDate()) }
    var formSongQuery by remember { mutableStateOf("") }

    fun resetForm() {
        editingEntryId = null
        formAuthor = ""
        formText = ""
        formDate = todayDate()
        formSongQuery = ""
    }

    fun loadHistory() {
        scope.launch {
            error = null
            entries =
                runCatching { MainApi.getHistory() }
                    .onFailure { error = it.message ?: "Could not load entries." }
                    .getOrDefault(emptyList())
        }
    }

    LaunchedEffect(activeApiKey) {
        if (activeApiKey.isNotBlank()) {
            loadHistory()
        } else {
            entries = emptyList()
            focusedEntryId = null
        }
    }

    Box(GalaxyTheme.pageFrame, Alignment.TopCenter) {
        Column(
            GalaxyTheme.centeredColumn.padding(top = GalaxyTheme.s(2)),
        ) {
            H1(
                Modifier
                    .fontWeight(FontWeight.Black)
                    .fontSize(40.px)
                    .margin(0.px)
                    .letterSpacing(.02.em)
                    .toAttrs(),
            ) {
                Text("MANAGE")
            }

            Box(GalaxyTheme.panelModifier) {
                Column(Modifier.gap(GalaxyTheme.s(1))) {
                    Input(
                        InputType.Text,
                        inputFieldModifier()
                            .toAttrs {
                                placeholder("X-API-Key")
                                value(apiKeyDraft)
                                onInput { event -> apiKeyDraft = event.value }
                            },
                    )
                    Row(Modifier.gap(GalaxyTheme.s(1))) {
                        IconActionButton(Icon.SAVE, isPrimary = true) {
                            saveApiKey(apiKeyDraft)
                            activeApiKey = apiKeyDraft.trim()
                            status = "API key saved."
                            error = null
                        }
                        IconActionButton(Icon.BACKSPACE) {
                            apiKeyDraft = ""
                            activeApiKey = ""
                            saveApiKey("")
                            status = "API key removed."
                            error = null
                            resetForm()
                        }
                    }
                }
            }

            if (activeApiKey.isBlank()) {
                P(
                    Modifier
                        .color(GalaxyTheme.textSecondary)
                        .fontWeight(350)
                        .toAttrs(),
                ) {
                    Text("Provide an API key to unlock create, edit, and delete.")
                }
            } else {
                Box(GalaxyTheme.panelModifier) {
                    Column(Modifier.gap(GalaxyTheme.s(1))) {
                        H3(
                            Modifier
                                .fontWeight(FontWeight.Bold)
                                .margin(0.px, 0.px, 8.px, 0.px)
                                .fontSize(18.px)
                                .toAttrs(),
                        ) {
                            Text(if (editingEntryId == null) "Create Entry" else "Edit #$editingEntryId")
                        }

                        Input(
                            InputType.Text,
                            inputFieldModifier()
                                .toAttrs {
                                    placeholder("Author")
                                    value(formAuthor)
                                    onInput { event -> formAuthor = event.value }
                                },
                        )
                        TextArea(
                            attrs =
                                inputFieldModifier()
                                    .minHeight(120.px)
                                    .toAttrs {
                                        placeholder("Entry text")
                                        value(formText)
                                        onInput { event -> formText = event.value }
                                    },
                        )
                        Input(
                            InputType.Date,
                            inputFieldModifier()
                                .toAttrs {
                                    value(formDate)
                                    onInput { event -> formDate = event.value }
                                },
                        )
                        Input(
                            InputType.Text,
                            inputFieldModifier()
                                .toAttrs {
                                    placeholder("Song query (optional)")
                                    value(formSongQuery)
                                    onInput { event -> formSongQuery = event.value }
                                },
                        )

                        Row(Modifier.gap(GalaxyTheme.s(1))) {
                            IconActionButton(if (editingEntryId == null) Icon.PLUS else Icon.UPDATE, isPrimary = true) {
                                scope.launch {
                                    error = null
                                    status = null
                                    val payload =
                                        EntryPayload(
                                            id = editingEntryId?.toString(),
                                            author = formAuthor.trim(),
                                            text = formText.trim(),
                                            date = formDate.trim(),
                                            songQuery = formSongQuery.trim().ifBlank { null },
                                        )
                                    runCatching { MainApi.upsertEntry(payload, activeApiKey.trim()) }
                                        .onSuccess { saved ->
                                            if (saved != null) {
                                                status = "Saved #${saved.id}."
                                                resetForm()
                                                loadHistory()
                                            } else {
                                                status = "No entry was returned."
                                            }
                                        }.onFailure {
                                            error = it.message ?: "Could not save entry."
                                        }
                                }
                            }
                            IconActionButton(Icon.RESET) {
                                resetForm()
                                status = "Form reset."
                            }
                        }
                    }
                }

                val availableCols = entries.size.coerceAtLeast(1)

                SimpleGrid(
                    numColumns(base = 1, sm = minOf(2, availableCols), lg = minOf(3, availableCols), xl = minOf(4, availableCols)),
                    modifier = Modifier.width(100.percent).gap(16.px),
                ) {
                    entries.asReversed().forEach { entry ->
                        val isFocused = focusedEntryId == entry.id
                        Box(
                            GalaxyTheme
                                .interactivePanel(isFocused)
                                .onMouseEnter { focusedEntryId = entry.id }
                                .onMouseLeave { focusedEntryId = null }
                                .fillMaxSize(),
                        ) {
                            Column(Modifier.gap(GalaxyTheme.s(1)).fillMaxSize().padding(bottom = GalaxyTheme.s(8))) {
                                P(
                                    Modifier
                                        .fontWeight(FontWeight.Bold)
                                        .margin(0.px)
                                        .fontSize(14.px)
                                        .toAttrs(),
                                ) {
                                    Text("${entry.author} · ${entry.date} · #${entry.id}")
                                }
                                P(
                                    Modifier
                                        .margin(0.px)
                                        .fontSize(14.px)
                                        .fontWeight(350)
                                        .lineHeight(1.45)
                                        .toAttrs(),
                                ) {
                                    Text(entry.text)
                                }
                            }

                            Row(Modifier.gap(GalaxyTheme.s(1)).align(Alignment.BottomEnd)) {
                                IconActionButton(Icon.EDIT) {
                                    editingEntryId = entry.id
                                    formAuthor = entry.author
                                    formText = entry.text
                                    formDate = entry.date
                                    formSongQuery = entry.song?.name ?: ""
                                    status = "Loaded #${entry.id}."
                                    error = null
                                }
                                IconActionButton(Icon.DELETE) {
                                    scope.launch {
                                        runCatching { MainApi.deleteEntry(entry.id, activeApiKey.trim()) }
                                            .onSuccess {
                                                status = "Deleted #${entry.id}."
                                                if (editingEntryId == entry.id) resetForm()
                                                loadHistory()
                                            }.onFailure {
                                                error = it.message ?: "Could not delete entry."
                                            }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            status?.let {
                P(
                    Modifier
                        .color(GalaxyTheme.lavender)
                        .fontSize(13.px)
                        .toAttrs(),
                ) { Text(it) }
            }
            error?.let {
                P(
                    Modifier
                        .color(Color("#ff9ea5"))
                        .fontSize(13.px)
                        .toAttrs(),
                ) { Text("Error: $it") }
            }
        }
    }
}

@Composable
private fun IconActionButton(
    icon: Icon,
    isPrimary: Boolean = false,
    onClick: () -> Unit,
) {
    var hovered by remember { mutableStateOf(false) }

    Button(
        GalaxyTheme
            .actionButtonModifier(isPrimary)
            .transform { if (hovered) scale(1.06) else scale(0.92) }
            .toAttrs {
                onMouseEnter { hovered = true }
                onMouseLeave { hovered = false }
                onClick { onClick() }
                title(icon.title)
            },
    ) {
        icon(Modifier.scale(0.75f))
    }
}

private fun inputFieldModifier(): Modifier =
    Modifier
        .width(100.percent)
        .background(Color("#0f1528"))
        .border(1.px, LineStyle.Solid, GalaxyTheme.panelBorder)
        .borderRadius(6.px)
        .padding(GalaxyTheme.s(2))
        .color(GalaxyTheme.textPrimary)
        .cursor(Cursor.Text)
        .fontFamily("Inter", "Avenir Next", "Segoe UI", "sans-serif")
        .fontSize(14.px)
        .fontWeight(350)
        .transition {
            property("border-color", "transform")
            duration(180.ms)
            timingFunction(AnimationTimingFunction.Ease)
        }.transform { scale(0.98) }
