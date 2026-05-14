package xyz.malefic.mournal.pages

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
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
import com.varabyte.kobweb.compose.ui.modifiers.display
import com.varabyte.kobweb.compose.ui.modifiers.fontFamily
import com.varabyte.kobweb.compose.ui.modifiers.fontSize
import com.varabyte.kobweb.compose.ui.modifiers.fontWeight
import com.varabyte.kobweb.compose.ui.modifiers.gap
import com.varabyte.kobweb.compose.ui.modifiers.letterSpacing
import com.varabyte.kobweb.compose.ui.modifiers.lineHeight
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.minHeight
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.transform
import com.varabyte.kobweb.compose.ui.modifiers.transition
import com.varabyte.kobweb.compose.ui.modifiers.width
import com.varabyte.kobweb.compose.ui.styleModifier
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.core.Page
import kotlinx.coroutines.launch
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
import org.jetbrains.compose.web.dom.Button
import org.jetbrains.compose.web.dom.Div
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
import xyz.malefic.mournal.styles.GalaxyTheme

@Page("/manage")
@Composable
fun ManagePage() {
    val scope = rememberCoroutineScope()

    var apiKeyDraft by mutableStateOf(readSavedApiKey())
    var activeApiKey by mutableStateOf(readSavedApiKey())
    var entries by mutableStateOf<List<Entry>>(emptyList())
    var status by mutableStateOf<String?>(null)
    var error by mutableStateOf<String?>(null)
    var focusedEntryId by mutableStateOf<Long?>(null)

    var editingEntryId by mutableStateOf<Long?>(null)
    var formAuthor by mutableStateOf("")
    var formText by mutableStateOf("")
    var formDate by mutableStateOf(todayDate())
    var formSongQuery by mutableStateOf("")

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
            if (entries.isNotEmpty() && entries.none { it.id == focusedEntryId }) {
                focusedEntryId = entries.first().id
            }
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

    Box(GalaxyTheme.pageFrame, contentAlignment = Alignment.TopCenter) {
        Column(
            GalaxyTheme.centeredColumn.padding(top = GalaxyTheme.s(2)),
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
                Text("MANAGE")
            }

            Div(attrs = GalaxyTheme.panelModifier.toAttrs()) {
                Column(Modifier.gap(GalaxyTheme.s(1))) {
                    Input(
                        type = InputType.Text,
                        attrs =
                            inputFieldModifier()
                                .toAttrs {
                                    placeholder("X-API-Key")
                                    value(apiKeyDraft)
                                    onInput { event -> apiKeyDraft = event.value }
                                },
                    )
                    Row(Modifier.gap(GalaxyTheme.s(1))) {
                        ActionButton("Save Key", isPrimary = true) {
                            saveApiKey(apiKeyDraft)
                            activeApiKey = apiKeyDraft.trim()
                            status = "API key saved."
                            error = null
                        }
                        ActionButton("Clear Key") {
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
                    attrs =
                        Modifier
                            .color(GalaxyTheme.textSecondary)
                            .fontWeight(350)
                            .toAttrs(),
                ) {
                    Text("Provide an API key to unlock create, edit, and delete.")
                }
            } else {
                Div(attrs = GalaxyTheme.panelModifier.toAttrs()) {
                    Column(Modifier.gap(GalaxyTheme.s(1))) {
                        H3(
                            attrs =
                                Modifier
                                    .fontWeight(FontWeight.Bold)
                                    .margin(0.px, 0.px, 8.px, 0.px)
                                    .fontSize(18.px)
                                    .toAttrs(),
                        ) {
                            Text(if (editingEntryId == null) "Create Entry" else "Edit #$editingEntryId")
                        }

                        Input(
                            type = InputType.Text,
                            attrs =
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
                            type = InputType.Date,
                            attrs =
                                inputFieldModifier()
                                    .toAttrs {
                                        value(formDate)
                                        onInput { event -> formDate = event.value }
                                    },
                        )
                        Input(
                            type = InputType.Text,
                            attrs =
                                inputFieldModifier()
                                    .toAttrs {
                                        placeholder("Song query (optional)")
                                        value(formSongQuery)
                                        onInput { event -> formSongQuery = event.value }
                                    },
                        )

                        Row(Modifier.gap(GalaxyTheme.s(1))) {
                            ActionButton(if (editingEntryId == null) "Add Entry" else "Update Entry", isPrimary = true) {
                                scope.launch {
                                    error = null
                                    status = null
                                    val payload =
                                        EntryPayload(
                                            id = editingEntryId,
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
                            ActionButton("Reset") {
                                resetForm()
                                status = "Form reset."
                            }
                        }
                    }
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
                    entries.asReversed().forEach { entry ->
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

                                Row(Modifier.gap(GalaxyTheme.s(1))) {
                                    ActionButton("Edit") {
                                        editingEntryId = entry.id
                                        formAuthor = entry.author
                                        formText = entry.text
                                        formDate = entry.date
                                        formSongQuery = entry.song?.name ?: ""
                                        status = "Loaded #${entry.id}."
                                        error = null
                                    }
                                    ActionButton("Delete") {
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
            }

            status?.let {
                P(
                    attrs =
                        Modifier
                            .color(GalaxyTheme.lavender)
                            .fontSize(13.px)
                            .toAttrs(),
                ) { Text(it) }
            }
            error?.let {
                P(
                    attrs =
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
private fun ActionButton(
    label: String,
    isPrimary: Boolean = false,
    onClick: () -> Unit,
) {
    var hovered by mutableStateOf(false)

    Button(
        attrs =
            GalaxyTheme
                .actionButtonModifier(isPrimary)
                .transform { if (hovered) scale(1.06) else scale(0.92) }
                .toAttrs {
                    onMouseEnter { hovered = true }
                    onMouseLeave { hovered = false }
                    onClick { onClick() }
                },
    ) {
        Text(label)
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
        .cursor(com.varabyte.kobweb.compose.css.Cursor.Text)
        .fontFamily("Inter", "Avenir Next", "Segoe UI", "sans-serif")
        .fontSize(14.px)
        .fontWeight(350)
        .transition {
            property("border-color", "transform")
            duration(180.ms)
            timingFunction(AnimationTimingFunction.Ease)
        }.transform { scale(0.98) }
