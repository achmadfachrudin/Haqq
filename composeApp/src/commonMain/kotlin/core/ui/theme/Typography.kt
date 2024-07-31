package core.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import haqq.composeapp.generated.resources.Res
import haqq.composeapp.generated.resources.Rubik_Bold
import haqq.composeapp.generated.resources.Rubik_Light
import haqq.composeapp.generated.resources.Rubik_Medium
import haqq.composeapp.generated.resources.Rubik_Regular
import haqq.composeapp.generated.resources.Rubik_SemiBold
import org.jetbrains.compose.resources.Font

@Composable
fun getHaqqTypography(): Typography {
    val displayFontFamily =
        FontFamily(
            Font(Res.font.Rubik_Light, FontWeight.Light),
            Font(Res.font.Rubik_Regular, FontWeight.Normal),
            Font(Res.font.Rubik_Medium, FontWeight.Medium),
            Font(Res.font.Rubik_SemiBold, FontWeight.SemiBold),
            Font(Res.font.Rubik_Bold, FontWeight.Bold),
        )

    val defaultTypography = Typography()

    return Typography(
        displayLarge = defaultTypography.displayLarge.copy(fontFamily = displayFontFamily),
        displayMedium = defaultTypography.displayMedium.copy(fontFamily = displayFontFamily),
        displaySmall = defaultTypography.displaySmall.copy(fontFamily = displayFontFamily),
        headlineLarge = defaultTypography.headlineLarge.copy(fontFamily = displayFontFamily),
        headlineMedium = defaultTypography.headlineMedium.copy(fontFamily = displayFontFamily),
        headlineSmall = defaultTypography.headlineSmall.copy(fontFamily = displayFontFamily),
        titleLarge = defaultTypography.titleLarge.copy(fontFamily = displayFontFamily),
        titleMedium = defaultTypography.titleMedium.copy(fontFamily = displayFontFamily),
        titleSmall = defaultTypography.titleSmall.copy(fontFamily = displayFontFamily),
        bodyLarge = defaultTypography.bodyLarge.copy(fontFamily = displayFontFamily),
        bodyMedium = defaultTypography.bodyMedium.copy(fontFamily = displayFontFamily),
        bodySmall = defaultTypography.bodySmall.copy(fontFamily = displayFontFamily),
        labelLarge = defaultTypography.labelLarge.copy(fontFamily = displayFontFamily),
        labelMedium = defaultTypography.labelMedium.copy(fontFamily = displayFontFamily),
        labelSmall = defaultTypography.labelSmall.copy(fontFamily = displayFontFamily),
    )
}
