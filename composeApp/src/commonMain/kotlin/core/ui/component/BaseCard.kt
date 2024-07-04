package core.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import core.ui.theme.getHaqqTypography
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

@Composable
fun BaseMenuLandscapeCard(
    title: String,
    onItemClick: () -> Unit,
) {
    Card(
        modifier = Modifier.fillMaxWidth().height(110.dp),
        onClick = { onItemClick() },
        colors =
            CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
            ),
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
        ) {
            BaseTitle(
                modifier = Modifier.padding(16.dp).align(Alignment.BottomStart),
                text = title,
            )
        }
    }
}

@Composable
fun BaseMenuLargeCard(
    title: String,
    onItemClick: () -> Unit,
) {
    Card(
        modifier = Modifier.size(94.dp),
        onClick = { onItemClick() },
        colors =
            CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
            ),
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
        ) {
            BaseText(
                modifier = Modifier.padding(16.dp).align(Alignment.TopStart),
                text = title,
                style = getHaqqTypography().titleSmall,
            )
        }
    }
}

@Composable
fun BaseLabelCard(title: String) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(start = 16.dp, end = 16.dp, top = 16.dp),
    ) {
        BaseTitle(text = title)
    }
}

@Composable
fun BaseItemCard(
    title: String,
    titleColor: Color = Color.Unspecified,
    iconResource: DrawableResource? = null,
    iconTint: Color = LocalContentColor.current,
    imageUrl: String? = null,
    showDivider: Boolean = true,
    onClick: () -> Unit = {},
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
    ) {
        Row(
            modifier =
                Modifier
                    .clickable { onClick() }
                    .fillMaxWidth()
                    .padding(itemPadding),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            iconResource?.let {
                Icon(
                    painter = painterResource(iconResource),
                    tint = iconTint,
                    contentDescription = "",
                )
                BaseSpacerHorizontal()
            }
            imageUrl?.let {
                BaseImage(
                    imageUrl = imageUrl,
                    modifier = Modifier.size(24.dp).clip(CircleShape),
                )
                BaseSpacerHorizontal()
            }
            BaseText(text = title, color = titleColor)
        }

        if (showDivider) {
            BaseDivider()
        }
    }
}

@Composable
fun BaseLabelValueCard(
    label: String,
    value: String,
    showHighlight: Boolean = false,
    iconResource: DrawableResource? = null,
    showDivider: Boolean = true,
    onClick: () -> Unit = {},
) {
    val highlightColor = if (showHighlight) MaterialTheme.colorScheme.primary else Color.Unspecified
    val highlightStyle =
        if (showHighlight) getHaqqTypography().titleMedium else LocalTextStyle.current

    Column(
        modifier = Modifier.fillMaxWidth(),
    ) {
        Row(
            modifier =
                Modifier
                    .clickableWithoutRipple { onClick() }
                    .fillMaxWidth()
                    .padding(itemPadding),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            iconResource?.let {
                Icon(
                    painter = painterResource(iconResource),
                    contentDescription = "",
                )
                BaseSpacerHorizontal()
            }
            BaseText(
                modifier = Modifier.weight(1f),
                text = label,
                color = highlightColor,
                style = highlightStyle,
            )
            BaseText(
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.End,
                text = value,
                color = highlightColor,
                style = highlightStyle,
            )
        }

        if (showDivider) {
            BaseDivider()
        }
    }
}
