package core.ui.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

object RedColor {
    private val primaryLight = Color(0xFFA50010)
    private val onPrimaryLight = Color(0xFFFFFFFF)
    private val primaryContainerLight = Color(0xFFE61821)
    private val onPrimaryContainerLight = Color(0xFFFFFFFF)
    private val secondaryLight = Color(0xFFAE302B)
    private val onSecondaryLight = Color(0xFFFFFFFF)
    private val secondaryContainerLight = Color(0xFFFF8176)
    private val onSecondaryContainerLight = Color(0xFF410002)
    private val tertiaryLight = Color(0xFF764400)
    private val onTertiaryLight = Color(0xFFFFFFFF)
    private val tertiaryContainerLight = Color(0xFFA96400)
    private val onTertiaryContainerLight = Color(0xFFFFFFFF)
    private val errorLight = Color(0xFFBA1A1A)
    private val onErrorLight = Color(0xFFFFFFFF)
    private val errorContainerLight = Color(0xFFFFDAD6)
    private val onErrorContainerLight = Color(0xFF410002)
    private val backgroundLight = Color(0xFFFFF8F7)
    private val onBackgroundLight = Color(0xFF291715)
    private val surfaceLight = Color(0xFFFFF8F7)
    private val onSurfaceLight = Color(0xFF291715)
    private val surfaceVariantLight = Color(0xFFFFDAD6)
    private val onSurfaceVariantLight = Color(0xFF5E3F3C)
    private val outlineLight = Color(0xFF936E6A)
    private val outlineVariantLight = Color(0xFFE8BCB7)
    private val scrimLight = Color(0xFF000000)
    private val inverseSurfaceLight = Color(0xFF402B29)
    private val inverseOnSurfaceLight = Color(0xFFFFEDEA)
    private val inversePrimaryLight = Color(0xFFFFB4AB)
    private val surfaceDimLight = Color(0xFFF5D2CE)
    private val surfaceBrightLight = Color(0xFFFFF8F7)
    private val surfaceContainerLowestLight = Color(0xFFFFFFFF)
    private val surfaceContainerLowLight = Color(0xFFFFF0EE)
    private val surfaceContainerLight = Color(0xFFFFE9E6)
    private val surfaceContainerHighLight = Color(0xFFFFE2DE)
    private val surfaceContainerHighestLight = Color(0xFFFEDBD7)

    private val primaryDark = Color(0xFFFFB4AB)
    private val onPrimaryDark = Color(0xFF690006)
    private val primaryContainerDark = Color(0xFFE2131F)
    private val onPrimaryContainerDark = Color(0xFFFFFFFF)
    private val secondaryDark = Color(0xFFFFB4AB)
    private val onSecondaryDark = Color(0xFF690006)
    private val secondaryContainerDark = Color(0xFF820E10)
    private val onSecondaryContainerDark = Color(0xFFFFC8C2)
    private val tertiaryDark = Color(0xFFFFB86E)
    private val onTertiaryDark = Color(0xFF492900)
    private val tertiaryContainerDark = Color(0xFFA46100)
    private val onTertiaryContainerDark = Color(0xFFFFFFFF)
    private val errorDark = Color(0xFFFFB4AB)
    private val onErrorDark = Color(0xFF690005)
    private val errorContainerDark = Color(0xFF93000A)
    private val onErrorContainerDark = Color(0xFFFFDAD6)
    private val backgroundDark = Color(0xFF200F0D)
    private val onBackgroundDark = Color(0xFFFEDBD7)
    private val surfaceDark = Color(0xFF200F0D)
    private val onSurfaceDark = Color(0xFFFEDBD7)
    private val surfaceVariantDark = Color(0xFF5E3F3C)
    private val onSurfaceVariantDark = Color(0xFFE8BCB7)
    private val outlineDark = Color(0xFFAE8783)
    private val outlineVariantDark = Color(0xFF5E3F3C)
    private val scrimDark = Color(0xFF000000)
    private val inverseSurfaceDark = Color(0xFFFEDBD7)
    private val inverseOnSurfaceDark = Color(0xFF402B29)
    private val inversePrimaryDark = Color(0xFFC00014)
    private val surfaceDimDark = Color(0xFF200F0D)
    private val surfaceBrightDark = Color(0xFF4A3431)
    private val surfaceContainerLowestDark = Color(0xFF1A0A08)
    private val surfaceContainerLowDark = Color(0xFF291715)
    private val surfaceContainerDark = Color(0xFF2E1B19)
    private val surfaceContainerHighDark = Color(0xFF392523)
    private val surfaceContainerHighestDark = Color(0xFF452F2D)

    val lightScheme =
        lightColorScheme(
            primary = primaryLight,
            onPrimary = onPrimaryLight,
            primaryContainer = primaryContainerLight,
            onPrimaryContainer = onPrimaryContainerLight,
            secondary = secondaryLight,
            onSecondary = onSecondaryLight,
            secondaryContainer = secondaryContainerLight,
            onSecondaryContainer = onSecondaryContainerLight,
            tertiary = tertiaryLight,
            onTertiary = onTertiaryLight,
            tertiaryContainer = tertiaryContainerLight,
            onTertiaryContainer = onTertiaryContainerLight,
            error = errorLight,
            onError = onErrorLight,
            errorContainer = errorContainerLight,
            onErrorContainer = onErrorContainerLight,
            background = backgroundLight,
            onBackground = onBackgroundLight,
            surface = surfaceLight,
            onSurface = onSurfaceLight,
            surfaceVariant = surfaceVariantLight,
            onSurfaceVariant = onSurfaceVariantLight,
            outline = outlineLight,
            outlineVariant = outlineVariantLight,
            scrim = scrimLight,
            inverseSurface = inverseSurfaceLight,
            inverseOnSurface = inverseOnSurfaceLight,
            inversePrimary = inversePrimaryLight,
            surfaceDim = surfaceDimLight,
            surfaceBright = surfaceBrightLight,
            surfaceContainerLowest = surfaceContainerLowestLight,
            surfaceContainerLow = surfaceContainerLowLight,
            surfaceContainer = surfaceContainerLight,
            surfaceContainerHigh = surfaceContainerHighLight,
            surfaceContainerHighest = surfaceContainerHighestLight,
        )

    val darkScheme =
        darkColorScheme(
            primary = primaryDark,
            onPrimary = onPrimaryDark,
            primaryContainer = primaryContainerDark,
            onPrimaryContainer = onPrimaryContainerDark,
            secondary = secondaryDark,
            onSecondary = onSecondaryDark,
            secondaryContainer = secondaryContainerDark,
            onSecondaryContainer = onSecondaryContainerDark,
            tertiary = tertiaryDark,
            onTertiary = onTertiaryDark,
            tertiaryContainer = tertiaryContainerDark,
            onTertiaryContainer = onTertiaryContainerDark,
            error = errorDark,
            onError = onErrorDark,
            errorContainer = errorContainerDark,
            onErrorContainer = onErrorContainerDark,
            background = backgroundDark,
            onBackground = onBackgroundDark,
            surface = surfaceDark,
            onSurface = onSurfaceDark,
            surfaceVariant = surfaceVariantDark,
            onSurfaceVariant = onSurfaceVariantDark,
            outline = outlineDark,
            outlineVariant = outlineVariantDark,
            scrim = scrimDark,
            inverseSurface = inverseSurfaceDark,
            inverseOnSurface = inverseOnSurfaceDark,
            inversePrimary = inversePrimaryDark,
            surfaceDim = surfaceDimDark,
            surfaceBright = surfaceBrightDark,
            surfaceContainerLowest = surfaceContainerLowestDark,
            surfaceContainerLow = surfaceContainerLowDark,
            surfaceContainer = surfaceContainerDark,
            surfaceContainerHigh = surfaceContainerHighDark,
            surfaceContainerHighest = surfaceContainerHighestDark,
        )
}
