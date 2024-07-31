package core.ui.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

object BlueColor {
    private val primaryLight = Color(0xFF0243C4)
    private val onPrimaryLight = Color(0xFFFFFFFF)
    private val primaryContainerLight = Color(0xFF416BEB)
    private val onPrimaryContainerLight = Color(0xFFFFFFFF)
    private val secondaryLight = Color(0xFF4E5C8F)
    private val onSecondaryLight = Color(0xFFFFFFFF)
    private val secondaryContainerLight = Color(0xFFC0CCFF)
    private val onSecondaryContainerLight = Color(0xFF2A3869)
    private val tertiaryLight = Color(0xFF822294)
    private val onTertiaryLight = Color(0xFFFFFFFF)
    private val tertiaryContainerLight = Color(0xFFAC4CBC)
    private val onTertiaryContainerLight = Color(0xFFFFFFFF)
    private val errorLight = Color(0xFFBA1A1A)
    private val onErrorLight = Color(0xFFFFFFFF)
    private val errorContainerLight = Color(0xFFFFDAD6)
    private val onErrorContainerLight = Color(0xFF410002)
    private val backgroundLight = Color(0xFFFAF8FF)
    private val onBackgroundLight = Color(0xFF1A1B23)
    private val surfaceLight = Color(0xFFFAF8FF)
    private val onSurfaceLight = Color(0xFF1A1B23)
    private val surfaceVariantLight = Color(0xFFE0E1F3)
    private val onSurfaceVariantLight = Color(0xFF434654)
    private val outlineLight = Color(0xFF747686)
    private val outlineVariantLight = Color(0xFFC4C5D7)
    private val scrimLight = Color(0xFF000000)
    private val inverseSurfaceLight = Color(0xFF2E3038)
    private val inverseOnSurfaceLight = Color(0xFFF0F0FB)
    private val inversePrimaryLight = Color(0xFFB6C4FF)
    private val surfaceDimLight = Color(0xFFD9D9E4)
    private val surfaceBrightLight = Color(0xFFFAF8FF)
    private val surfaceContainerLowestLight = Color(0xFFFFFFFF)
    private val surfaceContainerLowLight = Color(0xFFF3F2FE)
    private val surfaceContainerLight = Color(0xFFEDEDF8)
    private val surfaceContainerHighLight = Color(0xFFE8E7F2)
    private val surfaceContainerHighestLight = Color(0xFFE2E1EC)

    private val primaryDark = Color(0xFFB6C4FF)
    private val onPrimaryDark = Color(0xFF00287E)
    private val primaryContainerDark = Color(0xFF406BEA)
    private val onPrimaryContainerDark = Color(0xFFFFFFFF)
    private val secondaryDark = Color(0xFFB6C4FE)
    private val onSecondaryDark = Color(0xFF1F2D5D)
    private val secondaryContainerDark = Color(0xFF2F3D6E)
    private val onSecondaryContainerDark = Color(0xFFCAD3FF)
    private val tertiaryDark = Color(0xFFF8ACFF)
    private val onTertiaryDark = Color(0xFF570067)
    private val tertiaryContainerDark = Color(0xFFAD4DBD)
    private val onTertiaryContainerDark = Color(0xFFFFFFFF)
    private val errorDark = Color(0xFFFFB4AB)
    private val onErrorDark = Color(0xFF690005)
    private val errorContainerDark = Color(0xFF93000A)
    private val onErrorContainerDark = Color(0xFFFFDAD6)
    private val backgroundDark = Color(0xFF11131A)
    private val onBackgroundDark = Color(0xFFE2E1EC)
    private val surfaceDark = Color(0xFF11131A)
    private val onSurfaceDark = Color(0xFFE2E1EC)
    private val surfaceVariantDark = Color(0xFF434654)
    private val onSurfaceVariantDark = Color(0xFFC4C5D7)
    private val outlineDark = Color(0xFF8E90A0)
    private val outlineVariantDark = Color(0xFF434654)
    private val scrimDark = Color(0xFF000000)
    private val inverseSurfaceDark = Color(0xFFE2E1EC)
    private val inverseOnSurfaceDark = Color(0xFF2E3038)
    private val inversePrimaryDark = Color(0xFF2353D3)
    private val surfaceDimDark = Color(0xFF11131A)
    private val surfaceBrightDark = Color(0xFF373941)
    private val surfaceContainerLowestDark = Color(0xFF0C0E15)
    private val surfaceContainerLowDark = Color(0xFF1A1B23)
    private val surfaceContainerDark = Color(0xFF1E1F27)
    private val surfaceContainerHighDark = Color(0xFF282A32)
    private val surfaceContainerHighestDark = Color(0xFF33343D)

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
