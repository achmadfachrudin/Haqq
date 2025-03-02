package core.ui.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import core.ui.theme.getHaqqTypography
import feature.other.service.AppRepository
import feature.other.service.mapper.getString
import feature.other.service.model.AppSetting
import feature.other.service.model.AppString
import org.koin.mp.KoinPlatform

@Composable
fun BismillahCard() {
    Column(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(16.dp),
    ) {
        BaseArabic(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            text = "بِسْمِ اللّٰهِ الرَّحْمٰنِ الرَّحِيْمِ",
        )
    }
}

@Composable
fun ArabicCard(
    title: String = "",
    desc: String = "",
    textArabic: String,
    textTransliteration: String = "",
    textTranslation: String = "",
    hadith: String = "",
    verseNumber: Int = 0,
    sajdahNumber: Int = 0,
    maxCount: Int = 1,
    showRipple: Boolean = true,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val appRepository = KoinPlatform.getKoin().get<AppRepository>()
    val setting: AppSetting = appRepository.getSetting()

    val columnModifier =
        if (showRipple) {
            modifier
                .clickable { onClick() }
                .fillMaxWidth()
                .padding(16.dp)
        } else {
            modifier
                .clickableWithoutRipple { onClick() }
                .fillMaxWidth()
                .padding(16.dp)
        }

    Column(
        modifier = columnModifier,
    ) {
        if (title.isNotEmpty()) {
            BaseTitle(text = title)
        }

        if (desc.isNotEmpty()) {
            BaseText(desc)
        }

        BaseSpacerVertical(4)

        BaseArabic(
            modifier = Modifier.align(Alignment.End),
            text = textArabic,
            verseNumber = verseNumber,
            sajdahNumber = sajdahNumber,
        )

        if (maxCount > 1) {
            BaseSpacerVertical(4)
            Column(
                modifier =
                    Modifier
                        .defaultMinSize(minHeight = AssistChipDefaults.Height)
                        .border(
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
                            shape = MaterialTheme.shapes.medium,
                        ).clip(MaterialTheme.shapes.medium)
                        .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                BaseText(
                    text = "${AppString.REPEAT.getString()} ${maxCount}x",
                    style = MaterialTheme.typography.labelLarge,
                )
            }
        }

        if (setting.transliterationVisibility && textTransliteration.isNotEmpty()) {
            BaseSpacerVertical(4)
            BaseTransliteration(
                text = textTransliteration,
            )
        }

        if (setting.translationVisibility && textTranslation.isNotEmpty()) {
            BaseSpacerVertical(4)
            BaseTranslation(
                text = textTranslation,
            )
        }

        if (hadith.isNotEmpty()) {
            BaseSpacerVertical()
            BaseHadith(text = hadith)
        }
    }
}

@Composable
fun BaseMessageCard(
    textArabic: String = "",
    textTranslation: String = "",
    textMessage: String = "",
    textCaption: String = "",
    verseNumber: Int = 0,
    buttonText: String = "",
    onItemClick: () -> Unit,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = { onItemClick() },
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
        ) {
            if (textArabic.isNotEmpty()) {
                BaseArabic(
                    modifier = Modifier.align(Alignment.End),
                    text = textArabic,
                    verseNumber = verseNumber,
                )
            }

            if (textTranslation.isNotEmpty()) {
                BaseTranslation(text = textTranslation)
            }

            if (textMessage.isNotEmpty()) {
                BaseText(text = textMessage)
            }

            if (textCaption.isNotEmpty()) {
                BaseText(
                    text = textCaption,
                    style = getHaqqTypography().bodySmall,
                )
            }

            if (buttonText.isNotEmpty()) {
                BaseButton(
                    modifier = Modifier.align(Alignment.End),
                    text = buttonText,
                    onClick = { onItemClick() },
                )
            }
        }
    }
}
