package core.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import feature.other.service.AppRepository
import feature.other.service.model.AppSetting
import org.koin.mp.KoinPlatform

@Immutable
data class ColorFamily(
    val color: Color,
    val onColor: Color,
    val colorContainer: Color,
    val onColorContainer: Color,
)

@Composable
fun HaqqTheme(
    useDarkTheme: Boolean = isSystemInDarkTheme(),
    content:
        @Composable()
        () -> Unit,
) {
    val appRepository = KoinPlatform.getKoin().get<AppRepository>()
    val setting = appRepository.getSetting()

    val lightScheme =
        when (setting.themeColor) {
            AppSetting.ThemeColor.GREEN -> GreenColor.lightScheme
            AppSetting.ThemeColor.BLUE -> BlueColor.lightScheme
            AppSetting.ThemeColor.PURPLE -> PurpleColor.lightScheme
            AppSetting.ThemeColor.RED -> RedColor.lightScheme
            AppSetting.ThemeColor.PINK -> PinkColor.lightScheme
        }

    val darkScheme =
        when (setting.themeColor) {
            AppSetting.ThemeColor.GREEN -> GreenColor.darkScheme
            AppSetting.ThemeColor.BLUE -> BlueColor.darkScheme
            AppSetting.ThemeColor.PURPLE -> PurpleColor.darkScheme
            AppSetting.ThemeColor.RED -> RedColor.darkScheme
            AppSetting.ThemeColor.PINK -> PinkColor.darkScheme
        }

    val colorScheme =
        when (setting.theme) {
            AppSetting.Theme.AUTO -> if (useDarkTheme) darkScheme else lightScheme
            AppSetting.Theme.LIGHT -> lightScheme
            AppSetting.Theme.DARK -> darkScheme
        }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = getHaqqTypography(),
        content = content,
    )
}
