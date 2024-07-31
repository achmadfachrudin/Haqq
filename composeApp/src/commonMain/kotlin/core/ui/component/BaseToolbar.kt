package core.ui.component

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.style.TextOverflow
import haqq.composeapp.generated.resources.Res
import haqq.composeapp.generated.resources.arrow_left
import haqq.composeapp.generated.resources.arrow_right
import haqq.composeapp.generated.resources.info
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BaseTopAppBar(
    title: String = "",
    showLeftButton: Boolean = true,
    showOptionalButton: Boolean = false,
    showRightButton: Boolean = false,
    leftButtonImage: Painter = painterResource(Res.drawable.arrow_left),
    optionalButtonImage: Painter = painterResource(Res.drawable.info),
    rightButtonImage: Painter = painterResource(Res.drawable.arrow_right),
    showOptionalLottie: Boolean = false,
    optionalLottie: @Composable (() -> Unit)? = null,
    optionalButtonTint: Color = LocalContentColor.current,
    onLeftButtonClick: () -> Unit = {},
    onOptionalButtonClick: () -> Unit = {},
    onRightButtonClick: () -> Unit = {},
) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = title,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        },
        navigationIcon = {
            if (showLeftButton) {
                IconButton(onClick = onLeftButtonClick) {
                    Icon(
                        painter = leftButtonImage,
                        contentDescription = "",
                    )
                }
            }
        },
        actions = {
            Row {
                if (showOptionalLottie) {
                    IconButton(onClick = onOptionalButtonClick) {
                        optionalLottie?.invoke()
                    }
                }
                if (showOptionalButton) {
                    IconButton(onClick = onOptionalButtonClick) {
                        Icon(
                            painter = optionalButtonImage,
                            tint = optionalButtonTint,
                            contentDescription = "",
                        )
                    }
                }
                if (showRightButton) {
                    IconButton(onClick = onRightButtonClick) {
                        Icon(
                            painter = rightButtonImage,
                            contentDescription = "",
                        )
                    }
                }
            }
        },
    )
}
