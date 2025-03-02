package core.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mohamedrejeb.ksoup.entities.KsoupEntities
import core.ui.theme.getHaqqTypography
import feature.other.service.AppRepository
import feature.other.service.model.AppSetting
import feature.quran.service.model.QuranConstant.MAX_VERSE
import feature.quran.service.model.Verse
import haqq.composeapp.generated.resources.IndoPak_by_QuranWBW_v4_2_2_WL
import haqq.composeapp.generated.resources.Res
import haqq.composeapp.generated.resources.Rubik_Bold
import haqq.composeapp.generated.resources.UthmanicHafs_V22
import haqq.composeapp.generated.resources.bg_frame_number_black
import haqq.composeapp.generated.resources.bg_frame_number_white
import org.jetbrains.compose.resources.Font
import org.jetbrains.compose.resources.painterResource
import org.koin.mp.KoinPlatform

@Composable
fun BaseText(
    text: String,
    textAnnotated: AnnotatedString? = null,
    inlineContent: Map<String, InlineTextContent> = mapOf(),
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    fontSize: TextUnit = TextUnit.Unspecified,
    fontStyle: FontStyle? = null,
    fontWeight: FontWeight? = null,
    fontFamily: FontFamily? = null,
    letterSpacing: TextUnit = TextUnit.Unspecified,
    textDecoration: TextDecoration? = null,
    textAlign: TextAlign? = null,
    lineHeight: TextUnit = TextUnit.Unspecified,
    overflow: TextOverflow = TextOverflow.Ellipsis,
    softWrap: Boolean = true,
    maxLines: Int = Int.MAX_VALUE,
    minLines: Int = 1,
    onTextLayout: (TextLayoutResult) -> Unit = {},
    style: TextStyle = LocalTextStyle.current,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
    drawableStart: Painter? = null,
    drawableEnd: Painter? = null,
    drawableStartSize: Int = 24,
    drawableEndSize: Int = 24,
    drawableStartPadding: Int = 8,
    drawableEndPadding: Int = 8,
    drawableStartColor: Color? = null,
    drawableEndColor: Color? = null,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = horizontalArrangement,
    ) {
        drawableStart?.let {
            Icon(
                modifier =
                    Modifier
                        .size(drawableStartSize.dp)
                        .padding(
                            top = 0.dp,
                            bottom = 0.dp,
                            start = 0.dp,
                            end = drawableStartPadding.dp,
                        ),
                painter = drawableStart,
                tint = drawableStartColor ?: color,
                contentDescription = "",
            )
        }

        if (textAnnotated == null) {
            Text(
                text = KsoupEntities.decodeHtml(text),
                color = color,
                fontSize = fontSize,
                fontStyle = fontStyle,
                fontWeight = fontWeight,
                fontFamily = fontFamily,
                letterSpacing = letterSpacing,
                textDecoration = textDecoration,
                textAlign = textAlign,
                lineHeight = lineHeight,
                overflow = overflow,
                softWrap = softWrap,
                maxLines = maxLines,
                minLines = minLines,
                onTextLayout = onTextLayout,
                style = style,
            )
        } else {
            Text(
                text = textAnnotated,
                inlineContent = inlineContent,
                color = color,
                fontSize = fontSize,
                fontStyle = fontStyle,
                fontWeight = fontWeight,
                fontFamily = fontFamily,
                letterSpacing = letterSpacing,
                textDecoration = textDecoration,
                textAlign = textAlign,
                lineHeight = lineHeight,
                overflow = overflow,
                softWrap = softWrap,
                maxLines = maxLines,
                minLines = minLines,
                onTextLayout = onTextLayout,
                style = style,
            )
        }

        drawableEnd?.let {
            Icon(
                modifier =
                    Modifier
                        .size(drawableEndSize.dp)
                        .padding(
                            top = 0.dp,
                            bottom = 0.dp,
                            start = drawableEndPadding.dp,
                            end = 0.dp,
                        ),
                painter = drawableEnd,
                tint = drawableEndColor ?: color,
                contentDescription = "",
            )
        }
    }
}

@Composable
fun BaseTitle(
    text: String,
    color: Color = Color.Unspecified,
    modifier: Modifier = Modifier,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Center,
) {
    BaseText(
        modifier = modifier,
        text = text,
        style = getHaqqTypography().titleMedium,
        color = color,
        horizontalArrangement = horizontalArrangement,
    )
}

@Composable
fun BasePageQuran(
    verses: List<Verse>,
    modifier: Modifier = Modifier,
) {
    val setting: AppSetting = KoinPlatform.getKoin().get<AppRepository>().getSetting()

    val fontFamily =
        when (setting.arabicStyle) {
            AppSetting.ArabicStyle.INDOPAK -> FontFamily(Font(Res.font.IndoPak_by_QuranWBW_v4_2_2_WL))
            AppSetting.ArabicStyle.UTHMANI,
            AppSetting.ArabicStyle.UTHMANI_TAJWEED,
            -> FontFamily(Font(Res.font.UthmanicHafs_V22))
        }

    val fontSize = setting.arabicFontSize

    BaseText(
        modifier = modifier,
        text = "",
        textAnnotated = getPageAnnotated(verses),
        inlineContent = getPageInlineContent(),
        fontFamily = fontFamily,
        fontSize = fontSize.sp,
        style =
            getHaqqTypography().bodyLarge.copy(
                lineHeight = (fontSize * 2).sp,
                textDirection = TextDirection.Rtl,
            ),
    )
}

/**
 * https://github.com/marwan/indopak-quran-text/tree/master/Fonts
 * fontFamily: FontFamily = FontFamily(Font(Res.font.IndoPak_by_QuranWBW_v4_2_2_WL)
 * https://fonts.qurancomplex.gov.sa/wp02/en/%D8%AD%D9%81%D8%B5/
 * fontFamily: FontFamily = FontFamily(Font(Res.font.UthmanicHafs_V22)
 */
@Composable
fun BaseArabic(
    modifier: Modifier = Modifier,
    text: String,
    verseNumber: Int = 0,
    sajdahNumber: Int = 0,
) {
    val setting: AppSetting = KoinPlatform.getKoin().get<AppRepository>().getSetting()

    val fontFamily =
        when (setting.arabicStyle) {
            AppSetting.ArabicStyle.INDOPAK -> FontFamily(Font(Res.font.IndoPak_by_QuranWBW_v4_2_2_WL))
            AppSetting.ArabicStyle.UTHMANI,
            AppSetting.ArabicStyle.UTHMANI_TAJWEED,
            -> FontFamily(Font(Res.font.UthmanicHafs_V22))
        }

    val fontSize = setting.arabicFontSize

    BaseText(
        modifier = modifier,
        text = "",
        textAnnotated = getVerseAnnotated(text, verseNumber, sajdahNumber),
        inlineContent = getVerseInlineContent(verseNumber),
        fontFamily = fontFamily,
        fontSize = fontSize.sp,
        style =
            getHaqqTypography().bodyLarge.copy(
                lineHeight = (fontSize * 2).sp,
                textDirection = TextDirection.Rtl,
            ),
    )
}

@Composable
fun getPageAnnotated(verses: List<Verse>): AnnotatedString {
    val annotatedString =
        buildAnnotatedString {
            verses.forEachIndexed { index, verse ->
                pushStringAnnotation(
                    tag = "tag-${verse.id}",
                    annotation = verse.textArabic,
                )

                append(verse.textArabic)

                append(" ")
                appendInlineContent(id = "frameNumber-${verse.verseNumber}")

                append(" ")

                pop()
            }
        }

    return annotatedString
}

@Composable
fun getVerseAnnotated(
    textArabic: String,
    verseNumber: Int,
    sajdahNumber: Int,
): AnnotatedString {
    val annotatedString =
        buildAnnotatedString {
            append(
                textArabic
                    .replace("\u06E9", "\u06E9 ")
                    .replace("\uE022\u200F", " \uE022\u200F"),
            )

            if (verseNumber > 0) {
                append("  ")
                appendInlineContent(id = "frameNumber-$verseNumber")
            }

            append(" ")
        }

    return annotatedString
}

@Composable
fun getPageInlineContent(): Map<String, InlineTextContent> {
    val inlineContentMap = mutableMapOf<String, InlineTextContent>()

    for (i in 1..MAX_VERSE) {
        inlineContentMap["frameNumber-$i"] = getInlineText(i)
    }

    return inlineContentMap
}

@Composable
fun getVerseInlineContent(verseNumber: Int): Map<String, InlineTextContent> {
    val inlineContentMap = mutableMapOf<String, InlineTextContent>()

    inlineContentMap["frameNumber-$verseNumber"] = getInlineText(verseNumber)

    return inlineContentMap
}

@Composable
private fun getInlineText(verseNumber: Int): InlineTextContent {
    val appRepository = KoinPlatform.getKoin().get<AppRepository>()
    val setting = appRepository.getSetting()

    val arabicVerseNumber =
        verseNumber
            .toString()
            .replace("1", "١")
            .replace("2", "٢")
            .replace("3", "٣")
            .replace("4", "٤")
            .replace("5", "٥")
            .replace("6", "٦")
            .replace("7", "٧")
            .replace("8", "٨")
            .replace("9", "٩")
            .replace("0", "٠")

    val iconRes =
        when (setting.theme) {
            AppSetting.Theme.AUTO -> if (isSystemInDarkTheme()) Res.drawable.bg_frame_number_white else Res.drawable.bg_frame_number_black
            AppSetting.Theme.LIGHT -> Res.drawable.bg_frame_number_black
            AppSetting.Theme.DARK -> Res.drawable.bg_frame_number_white
        }

    val inlineText =
        InlineTextContent(
            Placeholder(26.sp, 26.sp, PlaceholderVerticalAlign.TextCenter),
        ) {
            Box {
                Image(
                    painter = painterResource(iconRes),
                    modifier = Modifier.size(26.dp),
                    contentDescription = "",
                )
                BaseText(
                    modifier = Modifier.align(Alignment.Center),
                    text = arabicVerseNumber,
                    fontSize = 10.sp,
                )
            }
        }

    return inlineText
}

@Composable
fun BaseTransliteration(
    text: String,
    modifier: Modifier = Modifier,
) {
    val setting: AppSetting = KoinPlatform.getKoin().get<AppRepository>().getSetting()

    BaseText(
        text = text,
        style = getHaqqTypography().bodyLarge,
        fontSize = setting.transliterationFontSize.sp,
        modifier = modifier,
    )
}

@Composable
fun BaseTranslation(
    text: String,
    modifier: Modifier = Modifier,
) {
    val setting: AppSetting = KoinPlatform.getKoin().get<AppRepository>().getSetting()

    BaseText(
        text = text,
        style = getHaqqTypography().bodyLarge,
        fontFamily = FontFamily(Font(Res.font.Rubik_Bold)),
        fontSize = setting.translationFontSize.sp,
        modifier = modifier,
    )
}

@Composable
fun BaseHadith(
    text: String,
    modifier: Modifier = Modifier,
) {
    BaseText(
        text = text,
        style = getHaqqTypography().bodySmall,
        modifier = modifier,
    )
}
