package core.ui.component

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun BaseSpacerVertical(height: Int = 16) {
    Spacer(modifier = Modifier.width(2.dp).height(height.dp))
}

@Composable
fun BaseSpacerHorizontal(width: Int = 16) {
    Spacer(modifier = Modifier.height(2.dp).width(width.dp))
}
