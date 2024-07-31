package core.ui.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

object PinkColor {
    private val primaryLight = Color(0xFF88004E)
    private val onPrimaryLight = Color(0xFFFFFFFF)
    private val primaryContainerLight = Color(0xFFBF2674)
    private val onPrimaryContainerLight = Color(0xFFFFFFFF)
    private val secondaryLight = Color(0xFF954365)
    private val onSecondaryLight = Color(0xFFFFFFFF)
    private val secondaryContainerLight = Color(0xFFFFA7C7)
    private val onSecondaryContainerLight = Color(0xFF5D1638)
    private val tertiaryLight = Color(0xFF871700)
    private val onTertiaryLight = Color(0xFFFFFFFF)
    private val tertiaryContainerLight = Color(0xFFC13315)
    private val onTertiaryContainerLight = Color(0xFFFFFFFF)
    private val errorLight = Color(0xFFBA1A1A)
    private val onErrorLight = Color(0xFFFFFFFF)
    private val errorContainerLight = Color(0xFFFFDAD6)
    private val onErrorContainerLight = Color(0xFF410002)
    private val backgroundLight = Color(0xFFFFF8F8)
    private val onBackgroundLight = Color(0xFF25181C)
    private val surfaceLight = Color(0xFFFFF8F8)
    private val onSurfaceLight = Color(0xFF25181C)
    private val surfaceVariantLight = Color(0xFFFBDAE3)
    private val onSurfaceVariantLight = Color(0xFF574148)
    private val outlineLight = Color(0xFF8B7078)
    private val outlineVariantLight = Color(0xFFDEBEC7)
    private val scrimLight = Color(0xFF000000)
    private val inverseSurfaceLight = Color(0xFF3B2C31)
    private val inverseOnSurfaceLight = Color(0xFFFFECF0)
    private val inversePrimaryLight = Color(0xFFFFB0CC)
    private val surfaceDimLight = Color(0xFFECD4DA)
    private val surfaceBrightLight = Color(0xFFFFF8F8)
    private val surfaceContainerLowestLight = Color(0xFFFFFFFF)
    private val surfaceContainerLowLight = Color(0xFFFFF0F3)
    private val surfaceContainerLight = Color(0xFFFFE8EE)
    private val surfaceContainerHighLight = Color(0xFFFAE2E8)
    private val surfaceContainerHighestLight = Color(0xFFF5DDE3)

    private val primaryDark = Color(0xFFFFB0CC)
    private val onPrimaryDark = Color(0xFF640038)
    private val primaryContainerDark = Color(0xFF9F005C)
    private val onPrimaryContainerDark = Color(0xFFFFEDF1)
    private val secondaryDark = Color(0xFFFFB0CC)
    private val onSecondaryDark = Color(0xFF5B1436)
    private val secondaryContainerDark = Color(0xFF6F2446)
    private val onSecondaryContainerDark = Color(0xFFFFC6D8)
    private val tertiaryDark = Color(0xFFFFB4A4)
    private val onTertiaryDark = Color(0xFF630E00)
    private val tertiaryContainerDark = Color(0xFF9D1C00)
    private val onTertiaryContainerDark = Color(0xFFFFECE8)
    private val errorDark = Color(0xFFFFB4AB)
    private val onErrorDark = Color(0xFF690005)
    private val errorContainerDark = Color(0xFF93000A)
    private val onErrorContainerDark = Color(0xFFFFDAD6)
    private val backgroundDark = Color(0xFF1C1014)
    private val onBackgroundDark = Color(0xFFF5DDE3)
    private val surfaceDark = Color(0xFF1C1014)
    private val onSurfaceDark = Color(0xFFF5DDE3)
    private val surfaceVariantDark = Color(0xFF574148)
    private val onSurfaceVariantDark = Color(0xFFDEBEC7)
    private val outlineDark = Color(0xFFA68992)
    private val outlineVariantDark = Color(0xFF574148)
    private val scrimDark = Color(0xFF000000)
    private val inverseSurfaceDark = Color(0xFFF5DDE3)
    private val inverseOnSurfaceDark = Color(0xFF3B2C31)
    private val inversePrimaryDark = Color(0xFFB3186A)
    private val surfaceDimDark = Color(0xFF1C1014)
    private val surfaceBrightDark = Color(0xFF44353A)
    private val surfaceContainerLowestDark = Color(0xFF160B0F)
    private val surfaceContainerLowDark = Color(0xFF25181C)
    private val surfaceContainerDark = Color(0xFF291C20)
    private val surfaceContainerHighDark = Color(0xFF34262B)
    private val surfaceContainerHighestDark = Color(0xFF403135)

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
