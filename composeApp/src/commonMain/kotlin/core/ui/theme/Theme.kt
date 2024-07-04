package core.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import core.ui.theme.HaqqColor.DarkColors
import core.ui.theme.HaqqColor.LightColors
import feature.other.service.AppRepository
import feature.other.service.model.AppSetting
import org.koin.mp.KoinPlatform

@Composable
fun HaqqTheme(
    useDarkTheme: Boolean = isSystemInDarkTheme(),
    content:
        @Composable()
        () -> Unit,
) {
    val appRepository = KoinPlatform.getKoin().get<AppRepository>()
    val setting = appRepository.getSetting()

    val colors =
        when (setting.theme) {
            AppSetting.Theme.AUTO -> if (useDarkTheme) DarkColors else LightColors
            AppSetting.Theme.LIGHT -> LightColors
            AppSetting.Theme.DARK -> DarkColors
        }

    MaterialTheme(
        colorScheme = colors,
        content = content,
        typography = getHaqqTypography(),
    )
}
