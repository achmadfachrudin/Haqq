package core.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import feature.other.service.AppRepository
import feature.other.service.model.AppSetting
import org.koin.mp.KoinPlatform

object HaqqColor {
    private val md_theme_light_primary = Color(0xFF00696E)
    private val md_theme_light_onPrimary = Color(0xFFFFFFFF)
    private val md_theme_light_primaryContainer = Color(0xFF6FF6FD)
    private val md_theme_light_onPrimaryContainer = Color(0xFF002021)
    private val md_theme_light_secondary = Color(0xFF4A6364)
    private val md_theme_light_onSecondary = Color(0xFFFFFFFF)
    private val md_theme_light_secondaryContainer = Color(0xFFCCE8E9)
    private val md_theme_light_onSecondaryContainer = Color(0xFF041F21)
    private val md_theme_light_tertiary = Color(0xFF4E5F7D)
    private val md_theme_light_onTertiary = Color(0xFFFFFFFF)
    private val md_theme_light_tertiaryContainer = Color(0xFFD6E3FF)
    private val md_theme_light_onTertiaryContainer = Color(0xFF081C36)
    private val md_theme_light_error = Color(0xFFBA1A1A)
    private val md_theme_light_errorContainer = Color(0xFFFFDAD6)
    private val md_theme_light_onError = Color(0xFFFFFFFF)
    private val md_theme_light_onErrorContainer = Color(0xFF410002)
    private val md_theme_light_background = Color(0xFFFAFDFC)
    private val md_theme_light_onBackground = Color(0xFF191C1C)
    private val md_theme_light_surface = Color(0xFFFAFDFC)
    private val md_theme_light_onSurface = Color(0xFF191C1C)
    private val md_theme_light_surfaceVariant = Color(0xFFDAE4E5)
    private val md_theme_light_onSurfaceVariant = Color(0xFF3F4949)
    private val md_theme_light_outline = Color(0xFF6F7979)
    private val md_theme_light_inverseOnSurface = Color(0xFFEFF1F1)
    private val md_theme_light_inverseSurface = Color(0xFF2D3131)
    private val md_theme_light_inversePrimary = Color(0xFF4CD9E0)
    private val md_theme_light_shadow = Color(0xFF000000)
    private val md_theme_light_surfaceTint = Color(0xFF00696E)
    private val md_theme_light_outlineVariant = Color(0xFFBEC8C9)
    private val md_theme_light_scrim = Color(0xFF000000)
    private val md_theme_dark_primary = Color(0xFF4CD9E0)
    private val md_theme_dark_onPrimary = Color(0xFF003739)
    private val md_theme_dark_primaryContainer = Color(0xFF004F53)
    private val md_theme_dark_onPrimaryContainer = Color(0xFF6FF6FD)
    private val md_theme_dark_secondary = Color(0xFFB1CCCD)
    private val md_theme_dark_onSecondary = Color(0xFF1B3436)
    private val md_theme_dark_secondaryContainer = Color(0xFF324B4C)
    private val md_theme_dark_onSecondaryContainer = Color(0xFFCCE8E9)
    private val md_theme_dark_tertiary = Color(0xFFB5C7E9)
    private val md_theme_dark_onTertiary = Color(0xFF1F314C)
    private val md_theme_dark_tertiaryContainer = Color(0xFF364764)
    private val md_theme_dark_onTertiaryContainer = Color(0xFFD6E3FF)
    private val md_theme_dark_error = Color(0xFFFFB4AB)
    private val md_theme_dark_errorContainer = Color(0xFF93000A)
    private val md_theme_dark_onError = Color(0xFF690005)
    private val md_theme_dark_onErrorContainer = Color(0xFFFFDAD6)
    private val md_theme_dark_background = Color(0xFF191C1C)
    private val md_theme_dark_onBackground = Color(0xFFE0E3E3)
    private val md_theme_dark_surface = Color(0xFF191C1C)
    private val md_theme_dark_onSurface = Color(0xFFE0E3E3)
    private val md_theme_dark_surfaceVariant = Color(0xFF3F4949)
    private val md_theme_dark_onSurfaceVariant = Color(0xFFBEC8C9)
    private val md_theme_dark_outline = Color(0xFF899393)
    private val md_theme_dark_inverseOnSurface = Color(0xFF191C1C)
    private val md_theme_dark_inverseSurface = Color(0xFFE0E3E3)
    private val md_theme_dark_inversePrimary = Color(0xFF00696E)
    private val md_theme_dark_shadow = Color(0xFF000000)
    private val md_theme_dark_surfaceTint = Color(0xFF4CD9E0)
    private val md_theme_dark_outlineVariant = Color(0xFF3F4949)
    private val md_theme_dark_scrim = Color(0xFF000000)

    val LightColors =
        lightColorScheme(
            primary = md_theme_light_primary,
            onPrimary = md_theme_light_onPrimary,
            primaryContainer = md_theme_light_primaryContainer,
            onPrimaryContainer = md_theme_light_onPrimaryContainer,
            secondary = md_theme_light_secondary,
            onSecondary = md_theme_light_onSecondary,
            secondaryContainer = md_theme_light_secondaryContainer,
            onSecondaryContainer = md_theme_light_onSecondaryContainer,
            tertiary = md_theme_light_tertiary,
            onTertiary = md_theme_light_onTertiary,
            tertiaryContainer = md_theme_light_tertiaryContainer,
            onTertiaryContainer = md_theme_light_onTertiaryContainer,
            error = md_theme_light_error,
            errorContainer = md_theme_light_errorContainer,
            onError = md_theme_light_onError,
            onErrorContainer = md_theme_light_onErrorContainer,
            background = md_theme_light_background,
            onBackground = md_theme_light_onBackground,
            surface = md_theme_light_surface,
            onSurface = md_theme_light_onSurface,
            surfaceVariant = md_theme_light_surfaceVariant,
            onSurfaceVariant = md_theme_light_onSurfaceVariant,
            outline = md_theme_light_outline,
            inverseOnSurface = md_theme_light_inverseOnSurface,
            inverseSurface = md_theme_light_inverseSurface,
            inversePrimary = md_theme_light_inversePrimary,
            surfaceTint = md_theme_light_surfaceTint,
            outlineVariant = md_theme_light_outlineVariant,
            scrim = md_theme_light_scrim,
        )

    val DarkColors =
        darkColorScheme(
            primary = md_theme_dark_primary,
            onPrimary = md_theme_dark_onPrimary,
            primaryContainer = md_theme_dark_primaryContainer,
            onPrimaryContainer = md_theme_dark_onPrimaryContainer,
            secondary = md_theme_dark_secondary,
            onSecondary = md_theme_dark_onSecondary,
            secondaryContainer = md_theme_dark_secondaryContainer,
            onSecondaryContainer = md_theme_dark_onSecondaryContainer,
            tertiary = md_theme_dark_tertiary,
            onTertiary = md_theme_dark_onTertiary,
            tertiaryContainer = md_theme_dark_tertiaryContainer,
            onTertiaryContainer = md_theme_dark_onTertiaryContainer,
            error = md_theme_dark_error,
            errorContainer = md_theme_dark_errorContainer,
            onError = md_theme_dark_onError,
            onErrorContainer = md_theme_dark_onErrorContainer,
            background = md_theme_dark_background,
            onBackground = md_theme_dark_onBackground,
            surface = md_theme_dark_surface,
            onSurface = md_theme_dark_onSurface,
            surfaceVariant = md_theme_dark_surfaceVariant,
            onSurfaceVariant = md_theme_dark_onSurfaceVariant,
            outline = md_theme_dark_outline,
            inverseOnSurface = md_theme_dark_inverseOnSurface,
            inverseSurface = md_theme_dark_inverseSurface,
            inversePrimary = md_theme_dark_inversePrimary,
            surfaceTint = md_theme_dark_surfaceTint,
            outlineVariant = md_theme_dark_outlineVariant,
            scrim = md_theme_dark_scrim,
        )
}

enum class ExtraColor {
    PINK,
    GREEN,
    ORANGE,
    BLUE,
    VIOLET,
    ;

    companion object {
        private val md_theme_light_primary_pink = Color(0xFFB61B51)
        private val md_theme_light_onPrimary_pink = Color(0xFFFFFFFF)
        private val md_theme_dark_primary_pink = Color(0xFFFFB2BF)
        private val md_theme_dark_onPrimary_pink = Color(0xFF660028)

        private val md_theme_light_primary_green = Color(0xFF026e00)
        private val md_theme_light_onPrimary_green = Color(0xFFffffff)
        private val md_theme_dark_primary_green = Color(0xFF02e600)
        private val md_theme_dark_onPrimary_green = Color(0xFF013a00)

        private val md_theme_light_primary_orange = Color(0xFF855400)
        private val md_theme_light_onPrimary_orange = Color(0xFFffffff)
        private val md_theme_dark_primary_orange = Color(0xFFffb95c)
        private val md_theme_dark_onPrimary_orange = Color(0xFF462a00)

        private val md_theme_light_primary_blue = Color(0xFF006686)
        private val md_theme_light_onPrimary_blue = Color(0xFFffffff)
        private val md_theme_dark_primary_blue = Color(0xFF70d2ff)
        private val md_theme_dark_onPrimary_blue = Color(0xFF003547)

        private val md_theme_light_primary_violet = Color(0xFF6750A4)
        private val md_theme_light_onPrimary_violet = Color(0xFFFFFFFF)
        private val md_theme_dark_primary_violet = Color(0xFFD0BCFF)
        private val md_theme_dark_onPrimary_violet = Color(0xFF381E72)

        @Composable
        fun getPairColor(color: ExtraColor): Pair<Color, Color> {
            val appRepository = KoinPlatform.getKoin().get<AppRepository>()
            val setting = appRepository.getSetting()

            val lightColors =
                when (color) {
                    PINK ->
                        Pair(
                            md_theme_light_primary_pink,
                            md_theme_light_onPrimary_pink,
                        )

                    GREEN ->
                        Pair(
                            md_theme_light_primary_green,
                            md_theme_light_onPrimary_green,
                        )

                    ORANGE ->
                        Pair(
                            md_theme_light_primary_orange,
                            md_theme_light_onPrimary_orange,
                        )

                    BLUE ->
                        Pair(
                            md_theme_light_primary_blue,
                            md_theme_light_onPrimary_blue,
                        )

                    VIOLET ->
                        Pair(
                            md_theme_light_primary_violet,
                            md_theme_light_onPrimary_violet,
                        )
                }

            val darkColors =
                when (color) {
                    PINK ->
                        Pair(
                            md_theme_dark_primary_pink,
                            md_theme_dark_onPrimary_pink,
                        )

                    GREEN ->
                        Pair(
                            md_theme_dark_primary_green,
                            md_theme_dark_onPrimary_green,
                        )

                    ORANGE ->
                        Pair(
                            md_theme_dark_primary_orange,
                            md_theme_dark_onPrimary_orange,
                        )

                    BLUE ->
                        Pair(
                            md_theme_dark_primary_blue,
                            md_theme_dark_onPrimary_blue,
                        )

                    VIOLET ->
                        Pair(
                            md_theme_dark_primary_violet,
                            md_theme_dark_onPrimary_violet,
                        )
                }

            return when (setting.theme) {
                AppSetting.Theme.AUTO -> if (isSystemInDarkTheme()) darkColors else lightColors
                AppSetting.Theme.LIGHT -> lightColors
                AppSetting.Theme.DARK -> darkColors
            }
        }
    }
}
