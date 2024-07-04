package core.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import haqq.composeapp.generated.resources.Res
import haqq.composeapp.generated.resources.Ubuntu_Bold
import haqq.composeapp.generated.resources.Ubuntu_Medium
import haqq.composeapp.generated.resources.Ubuntu_Regular
import org.jetbrains.compose.resources.Font

@Composable
fun getHaqqTypography(): Typography {
    val defaultTypography = Typography()

    return defaultTypography.copy(
        displayLarge = replaceFontFamily(defaultTypography.displayLarge),
        displayMedium = replaceFontFamily(defaultTypography.displayMedium),
        displaySmall = replaceFontFamily(defaultTypography.displaySmall),
        headlineLarge = replaceFontFamily(defaultTypography.headlineLarge),
        headlineMedium = replaceFontFamily(defaultTypography.headlineMedium),
        headlineSmall = replaceFontFamily(defaultTypography.headlineSmall),
        titleLarge = replaceFontFamily(defaultTypography.titleLarge),
        titleMedium = replaceFontFamily(defaultTypography.titleMedium),
        titleSmall = replaceFontFamily(defaultTypography.titleSmall),
        bodyLarge = replaceFontFamily(defaultTypography.bodyLarge),
        bodyMedium = replaceFontFamily(defaultTypography.bodyMedium),
        bodySmall = replaceFontFamily(defaultTypography.bodySmall),
        labelLarge = replaceFontFamily(defaultTypography.labelLarge),
        labelMedium = replaceFontFamily(defaultTypography.labelMedium),
        labelSmall = replaceFontFamily(defaultTypography.labelSmall),
    )
}

@Composable
private fun replaceFontFamily(textStyle: TextStyle): TextStyle {
    val fontFamily =
        FontFamily(
            when (textStyle.fontWeight) {
                FontWeight.Bold -> Font(Res.font.Ubuntu_Bold)
                FontWeight.Medium -> Font(Res.font.Ubuntu_Medium)
                else -> Font(Res.font.Ubuntu_Regular)
            },
        )

    return textStyle.copy(fontFamily = fontFamily)
}
