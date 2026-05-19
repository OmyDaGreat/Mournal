package xyz.malefic.mournal.components

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.silk.components.icons.mdi.IconStyle
import com.varabyte.kobweb.silk.components.icons.mdi.MdiAdd
import com.varabyte.kobweb.silk.components.icons.mdi.MdiBackspace
import com.varabyte.kobweb.silk.components.icons.mdi.MdiBuild
import com.varabyte.kobweb.silk.components.icons.mdi.MdiDelete
import com.varabyte.kobweb.silk.components.icons.mdi.MdiEdit
import com.varabyte.kobweb.silk.components.icons.mdi.MdiHome
import com.varabyte.kobweb.silk.components.icons.mdi.MdiRefresh
import com.varabyte.kobweb.silk.components.icons.mdi.MdiSave
import com.varabyte.kobweb.silk.components.icons.mdi.MdiSearch
import com.varabyte.kobweb.silk.components.icons.mdi.MdiUpdate

enum class Icon(
    val title: String,
    val icon: @Composable (modifier: Modifier, style: IconStyle) -> Unit,
) {
    RESET("Reset", { modifier, style -> MdiRefresh(modifier, style) }),
    EDIT("Edit", { modifier, style -> MdiEdit(modifier, style) }),
    DELETE("Delete", { modifier, style -> MdiDelete(modifier, style) }),
    HOME("Home", { modifier, style -> MdiHome(modifier, style) }),
    SEARCH("Search", { modifier, style -> MdiSearch(modifier, style) }),
    BUILD("Manage", { modifier, style -> MdiBuild(modifier, style) }),
    PLUS("Add", { modifier, style -> MdiAdd(modifier, style) }),
    UPDATE("Update", { modifier, style -> MdiUpdate(modifier, style) }),
    SAVE("Save", { modifier, style -> MdiSave(modifier, style) }),
    BACKSPACE("Clear", { modifier, style -> MdiBackspace(modifier, style) }),
}

@Composable
operator fun Icon.invoke(
    modifier: Modifier = Modifier,
    style: IconStyle = IconStyle.FILLED,
) = icon(modifier, style)
