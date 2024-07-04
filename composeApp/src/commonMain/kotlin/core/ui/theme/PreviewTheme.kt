import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import core.ui.component.BaseSpacerHorizontal
import core.ui.component.BaseSpacerVertical
import core.ui.component.BaseText
import core.ui.theme.getHaqqTypography

@Composable
fun PreviewTheme() {
    Column(modifier = Modifier.verticalScroll(rememberScrollState()).padding(16.dp)) {
        PreviewTypography()

        BaseSpacerVertical()

        PreviewColor()
    }
}

@Composable
private fun PreviewTypography() {
    Column(modifier = Modifier.fillMaxWidth()) {
        BaseText("displayLarge", style = getHaqqTypography().displayLarge)
        BaseText("displayMedium", style = getHaqqTypography().displayMedium)
        BaseText("displaySmall", style = getHaqqTypography().displaySmall)
        BaseText("headlineLarge", style = getHaqqTypography().headlineLarge)
        BaseText("headlineMedium", style = getHaqqTypography().headlineMedium)
        BaseText("headlineSmall", style = getHaqqTypography().headlineSmall)
        BaseText("titleLarge", style = getHaqqTypography().titleLarge)
        BaseText("titleMedium", style = getHaqqTypography().titleMedium)
        BaseText("titleSmall", style = getHaqqTypography().titleSmall)
        BaseText("bodyLarge", style = getHaqqTypography().bodyLarge)
        BaseText("bodyMedium", style = getHaqqTypography().bodyMedium)
        BaseText("bodySmall", style = getHaqqTypography().bodySmall)
        BaseText("labelLarge", style = getHaqqTypography().labelLarge)
        BaseText("labelMedium", style = getHaqqTypography().labelMedium)
        BaseText("labelSmall", style = getHaqqTypography().labelSmall)
        BaseText("default")
    }
}

@Composable
private fun PreviewColor() {
    Column(modifier = Modifier.fillMaxWidth()) {
        PreviewItemColor(
            color = MaterialTheme.colorScheme.primary,
            colorName = "primary",
        )
        PreviewItemColor(
            color = MaterialTheme.colorScheme.onPrimary,
            colorName = "onPrimary",
        )
        PreviewItemColor(
            color = MaterialTheme.colorScheme.primaryContainer,
            colorName = "primaryContainer",
        )
        PreviewItemColor(
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            colorName = "onPrimaryContainer",
        )
        PreviewItemColor(
            color = MaterialTheme.colorScheme.secondary,
            colorName = "secondary",
        )
        PreviewItemColor(
            color = MaterialTheme.colorScheme.onSecondary,
            colorName = "onSecondary",
        )
        PreviewItemColor(
            color = MaterialTheme.colorScheme.secondaryContainer,
            colorName = "secondaryContainer",
        )
        PreviewItemColor(
            color = MaterialTheme.colorScheme.onSecondaryContainer,
            colorName = "onSecondaryContainer",
        )
        PreviewItemColor(
            color = MaterialTheme.colorScheme.tertiary,
            colorName = "tertiary",
        )
        PreviewItemColor(
            color = MaterialTheme.colorScheme.onTertiary,
            colorName = "onTertiary",
        )
        PreviewItemColor(
            color = MaterialTheme.colorScheme.tertiaryContainer,
            colorName = "tertiaryContainer",
        )
        PreviewItemColor(
            color = MaterialTheme.colorScheme.onTertiaryContainer,
            colorName = "onTertiaryContainer",
        )
        PreviewItemColor(
            color = MaterialTheme.colorScheme.error,
            colorName = "error",
        )
        PreviewItemColor(
            color = MaterialTheme.colorScheme.errorContainer,
            colorName = "errorContainer",
        )
        PreviewItemColor(
            color = MaterialTheme.colorScheme.onError,
            colorName = "onError",
        )
        PreviewItemColor(
            color = MaterialTheme.colorScheme.onErrorContainer,
            colorName = "onErrorContainer",
        )
        PreviewItemColor(
            color = MaterialTheme.colorScheme.background,
            colorName = "background",
        )
        PreviewItemColor(
            color = MaterialTheme.colorScheme.onBackground,
            colorName = "onBackground",
        )
        PreviewItemColor(
            color = MaterialTheme.colorScheme.surface,
            colorName = "surface",
        )
        PreviewItemColor(
            color = MaterialTheme.colorScheme.onSurface,
            colorName = "onSurface",
        )
        PreviewItemColor(
            color = MaterialTheme.colorScheme.surfaceVariant,
            colorName = "surfaceVariant",
        )
        PreviewItemColor(
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            colorName = "onSurfaceVariant",
        )
        PreviewItemColor(
            color = MaterialTheme.colorScheme.outline,
            colorName = "outline",
        )
        PreviewItemColor(
            color = MaterialTheme.colorScheme.inverseOnSurface,
            colorName = "inverseOnSurface",
        )
        PreviewItemColor(
            color = MaterialTheme.colorScheme.inverseSurface,
            colorName = "inverseSurface",
        )
        PreviewItemColor(
            color = MaterialTheme.colorScheme.inversePrimary,
            colorName = "inversePrimary",
        )
        PreviewItemColor(
            color = MaterialTheme.colorScheme.surfaceTint,
            colorName = "surfaceTint",
        )
        PreviewItemColor(
            color = MaterialTheme.colorScheme.outlineVariant,
            colorName = "outlineVariant",
        )
        PreviewItemColor(
            color = MaterialTheme.colorScheme.scrim,
            colorName = "scrim",
        )
    }
}

@Composable
private fun PreviewItemColor(
    color: Color,
    colorName: String,
) {
    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Box(modifier = Modifier.size(50.dp).background(color))
        BaseSpacerHorizontal()
        BaseText(colorName)
    }
}
