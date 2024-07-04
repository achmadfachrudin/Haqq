package core.ui.component

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Stable
val itemPadding = PaddingValues(16.dp)

@Composable
fun BaseDivider(horizontalPadding: Int = 16) {
    HorizontalDivider(
        modifier = Modifier.padding(horizontal = horizontalPadding.dp),
        thickness = 0.5.dp,
    )
}
