package core.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import feature.other.service.AppRepository
import feature.other.service.model.AppSetting
import org.koin.mp.KoinPlatform

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
