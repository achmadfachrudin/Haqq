package core.ui.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

object PurpleColor {
    private val primaryLight = Color(0xFF6A009C)
    private val onPrimaryLight = Color(0xFFFFFFFF)
    private val primaryContainerLight = Color(0xFF9337C8)
    private val onPrimaryContainerLight = Color(0xFFFFFFFF)
    private val secondaryLight = Color(0xFF774E8D)
    private val onSecondaryLight = Color(0xFFFFFFFF)
    private val secondaryContainerLight = Color(0xFFEABEFF)
    private val onSecondaryContainerLight = Color(0xFF512A67)
    private val tertiaryLight = Color(0xFF840052)
    private val onTertiaryLight = Color(0xFFFFFFFF)
    private val tertiaryContainerLight = Color(0xFFB82B78)
    private val onTertiaryContainerLight = Color(0xFFFFFFFF)
    private val errorLight = Color(0xFFBA1A1A)
    private val onErrorLight = Color(0xFFFFFFFF)
    private val errorContainerLight = Color(0xFFFFDAD6)
    private val onErrorContainerLight = Color(0xFF410002)
    private val backgroundLight = Color(0xFFFFF7FC)
    private val onBackgroundLight = Color(0xFF201922)
    private val surfaceLight = Color(0xFFFFF7FC)
    private val onSurfaceLight = Color(0xFF201922)
    private val surfaceVariantLight = Color(0xFFEEDDF1)
    private val onSurfaceVariantLight = Color(0xFF4E4352)
    private val outlineLight = Color(0xFF807384)
    private val outlineVariantLight = Color(0xFFD1C1D4)
    private val scrimLight = Color(0xFF000000)
    private val inverseSurfaceLight = Color(0xFF352E37)
    private val inverseOnSurfaceLight = Color(0xFFFAEDFA)
    private val inversePrimaryLight = Color(0xFFE6B4FF)
    private val surfaceDimLight = Color(0xFFE2D6E3)
    private val surfaceBrightLight = Color(0xFFFFF7FC)
    private val surfaceContainerLowestLight = Color(0xFFFFFFFF)
    private val surfaceContainerLowLight = Color(0xFFFCF0FD)
    private val surfaceContainerLight = Color(0xFFF7EAF7)
    private val surfaceContainerHighLight = Color(0xFFF1E4F1)
    private val surfaceContainerHighestLight = Color(0xFFEBDFEB)

    private val primaryDark = Color(0xFFE6B4FF)
    private val onPrimaryDark = Color(0xFF4F0076)
    private val primaryContainerDark = Color(0xFF7913AE)
    private val onPrimaryContainerDark = Color(0xFFFAE6FF)
    private val secondaryDark = Color(0xFFE5B5FD)
    private val onSecondaryDark = Color(0xFF451F5B)
    private val secondaryContainerDark = Color(0xFF532C6A)
    private val onSecondaryContainerDark = Color(0xFFECC2FF)
    private val tertiaryDark = Color(0xFFFFB0D0)
    private val onTertiaryDark = Color(0xFF63003C)
    private val tertiaryContainerDark = Color(0xFF980660)
    private val onTertiaryContainerDark = Color(0xFFFFE6EE)
    private val errorDark = Color(0xFFFFB4AB)
    private val onErrorDark = Color(0xFF690005)
    private val errorContainerDark = Color(0xFF93000A)
    private val onErrorContainerDark = Color(0xFFFFDAD6)
    private val backgroundDark = Color(0xFF17111A)
    private val onBackgroundDark = Color(0xFFEBDFEB)
    private val surfaceDark = Color(0xFF17111A)
    private val onSurfaceDark = Color(0xFFEBDFEB)
    private val surfaceVariantDark = Color(0xFF4E4352)
    private val onSurfaceVariantDark = Color(0xFFD1C1D4)
    private val outlineDark = Color(0xFF9A8C9E)
    private val outlineVariantDark = Color(0xFF4E4352)
    private val scrimDark = Color(0xFF000000)
    private val inverseSurfaceDark = Color(0xFFEBDFEB)
    private val inverseOnSurfaceDark = Color(0xFF352E37)
    private val inversePrimaryDark = Color(0xFF8B2EC0)
    private val surfaceDimDark = Color(0xFF17111A)
    private val surfaceBrightDark = Color(0xFF3E3740)
    private val surfaceContainerLowestDark = Color(0xFF120C14)
    private val surfaceContainerLowDark = Color(0xFF201922)
    private val surfaceContainerDark = Color(0xFF241D26)
    private val surfaceContainerHighDark = Color(0xFF2E2831)
    private val surfaceContainerHighestDark = Color(0xFF39323C)

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
