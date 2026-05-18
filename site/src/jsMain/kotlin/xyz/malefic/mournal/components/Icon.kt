package xyz.malefic.mournal.components

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.silk.components.icons.mdi.IconStyle
import com.varabyte.kobweb.silk.components.icons.mdi.MdiAdd
import com.varabyte.kobweb.silk.components.icons.mdi.MdiBuild
import com.varabyte.kobweb.silk.components.icons.mdi.MdiDelete
import com.varabyte.kobweb.silk.components.icons.mdi.MdiEdit
import com.varabyte.kobweb.silk.components.icons.mdi.MdiHome
import com.varabyte.kobweb.silk.components.icons.mdi.MdiRefresh
import com.varabyte.kobweb.silk.components.icons.mdi.MdiSearch
import com.varabyte.kobweb.silk.components.icons.mdi.MdiUpdate

enum class Icon(
    val icon: @Composable (modifier: Modifier, style: IconStyle) -> Unit,
) {
    RESET({ modifier, style -> MdiRefresh(modifier, style) }),
    EDIT({ modifier, style -> MdiEdit(modifier, style) }),
    DELETE({ modifier, style -> MdiDelete(modifier, style) }),
    HOME({ modifier, style -> MdiHome(modifier, style) }),
    SEARCH({ modifier, style -> MdiSearch(modifier, style) }),
    BUILD({ modifier, style -> MdiBuild(modifier, style) }),
    PLUS({ modifier, style -> MdiAdd(modifier, style) }),
    UPDATE({ modifier, style -> MdiUpdate(modifier, style) }),
}

@Composable
operator fun Icon.invoke(
    modifier: Modifier = Modifier,
    style: IconStyle = IconStyle.FILLED,
) = icon(modifier, style)
