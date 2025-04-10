package feature.home.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import core.ui.component.BaseImage
import core.ui.component.BaseIndicator
import core.ui.component.BaseMessageCard
import core.ui.component.BaseSpacerVertical
import core.ui.component.BaseText
import core.ui.component.BaseTitle
import core.ui.component.ErrorState
import core.ui.component.LoadingState
import core.ui.theme.getHaqqTypography
import feature.dhikr.service.model.DhikrType
import feature.home.service.model.HomeTemplate
import feature.quran.service.QuranRepository
import haqq.composeapp.generated.resources.Res
import haqq.composeapp.generated.resources.dhikr_afternoon_title
import haqq.composeapp.generated.resources.dhikr_morning_title
import haqq.composeapp.generated.resources.dhikr_pray_title
import haqq.composeapp.generated.resources.dhikr_ruqyah_title
import haqq.composeapp.generated.resources.dhikr_sleep_title
import haqq.composeapp.generated.resources.lastread_continue
import haqq.composeapp.generated.resources.next_prayer_time
import haqq.composeapp.generated.resources.prayer_note
import haqq.composeapp.generated.resources.read_dhikr
import haqq.composeapp.generated.resources.see_all
import haqq.composeapp.generated.resources.surah_ayah
import org.jetbrains.compose.resources.stringResource
import org.koin.mp.KoinPlatform

@Composable
internal fun MainPageHome(
    state: MainScreenModel.HomeState,
    onPrayerTimeClick: () -> Unit,
    onDhikrClick: (dhikrType: DhikrType) -> Unit,
    onWebClick: (url: String) -> Unit,
    onVerseClick: (chapterNumber: Int, verseNumber: Int) -> Unit,
    onVideoClick: (videoId: String) -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxSize(),
    ) {
        when (state) {
            MainScreenModel.HomeState.Loading -> {
                LoadingState()
            }

            is MainScreenModel.HomeState.Content -> {
                LazyColumn {
                    items(state.templates) { template ->
                        when (template) {
                            is HomeTemplate.PrayerTime -> {
                                PrayerTime(
                                    template = template,
                                    onItemClick = { onPrayerTimeClick() },
                                )
                            }

                            is HomeTemplate.Dhikr -> {
                                DhikrCard(
                                    template = template,
                                    onItemClick = { onDhikrClick(template.dhikrType) },
                                )
                            }

                            is HomeTemplate.Menu -> {
                            }

                            is HomeTemplate.LastRead -> {
                                LastReadCard(
                                    template = template,
                                    onItemClick = {
                                        onVerseClick(
                                            template.chapterNumber,
                                            template.verseNumber,
                                        )
                                    },
                                )
                            }

                            is HomeTemplate.QuranVerse -> {
                                QuranVerseCard(
                                    template = template,
                                    onItemClick = {
                                        onVerseClick(
                                            template.chapterNumber,
                                            template.verseNumber,
                                        )
                                    },
                                )
                            }

                            is HomeTemplate.Message -> {
                                MessageCard(
                                    template = template,
                                    onItemClick = { },
                                )
                            }

                            is HomeTemplate.SingleImage -> {
                                SingleImageCard(
                                    template = template,
                                    onItemClick = { link ->
                                        onWebClick(link)
                                    },
                                )
                            }

                            is HomeTemplate.MultipleImage -> {
                                MultipleImageCard(
                                    template = template,
                                    onItemClick = { link ->
                                        onWebClick(link)
                                    },
                                )
                            }

                            is HomeTemplate.Video -> {
                                VideoCard(
                                    template = template,
                                    onItemClick = { link ->
                                        onVideoClick(link)
                                    },
                                )
                            }
                        }
                    }
                }
            }

            is MainScreenModel.HomeState.Error -> {
                ErrorState(state.message)
            }
        }
    }
}

@Composable
private fun PrayerTime(
    template: HomeTemplate.PrayerTime,
    onItemClick: () -> Unit,
) {
    val header =
        """
        ${template.date}
        ${template.locationName}
        """.trimIndent()

    Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        if (template.label.isNotEmpty()) {
            BaseTitle(template.label)
        }
        BaseText(text = header)

        if (template.nextPrayerTime.isNotEmpty()) {
            BaseSpacerVertical()

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                BaseText(
                    text = stringResource(Res.string.next_prayer_time),
                )
                BaseText(
                    text = stringResource(template.nextPrayerName),
                    style = getHaqqTypography().bodyMedium,
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                TextButton(
                    onClick = { onItemClick() },
                ) {
                    Text(stringResource(Res.string.see_all))
                }
                BaseText(
                    text = template.nextPrayerTime,
                    style = getHaqqTypography().titleLarge,
                )
            }
        }

        BaseText(
            text = stringResource(Res.string.prayer_note),
            style = getHaqqTypography().labelMedium,
        )
    }
}

@Composable
private fun DhikrCard(
    template: HomeTemplate.Dhikr,
    onItemClick: () -> Unit,
) {
    val dhikrName =
        when (template.dhikrType) {
            DhikrType.MORNING -> stringResource(Res.string.dhikr_morning_title)
            DhikrType.AFTERNOON -> stringResource(Res.string.dhikr_afternoon_title)
            DhikrType.PRAY -> stringResource(Res.string.dhikr_pray_title)
            DhikrType.SLEEP -> stringResource(Res.string.dhikr_sleep_title)
            DhikrType.RUQYAH -> stringResource(Res.string.dhikr_ruqyah_title)
        }

    Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        if (template.label.isNotEmpty()) {
            BaseTitle(template.label)
        }
        BaseMessageCard(
            textMessage = stringResource(Res.string.read_dhikr).replace("%1", dhikrName),
            onItemClick = onItemClick,
        )
    }
}

@Composable
private fun LastReadCard(
    template: HomeTemplate.LastRead,
    onItemClick: () -> Unit,
) {
    val quranRepository = KoinPlatform.getKoin().get<QuranRepository>()
    val chapterNameSimple = quranRepository.getChapterNameSimple(template.chapterNumber)
    Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        if (template.label.isNotEmpty()) {
            BaseTitle(template.label)
        }
        BaseMessageCard(
            textArabic = template.arabic,
            textCaption =
                stringResource(Res.string.surah_ayah)
                    .replace("%1", chapterNameSimple)
                    .replace("%2", "${template.verseNumber}"),
            verseNumber = template.verseNumber,
            buttonText = stringResource(Res.string.lastread_continue),
            onItemClick = onItemClick,
        )
    }
}

@Composable
private fun QuranVerseCard(
    template: HomeTemplate.QuranVerse,
    onItemClick: () -> Unit,
) {
    val quranRepository = KoinPlatform.getKoin().get<QuranRepository>()
    val chapterNameSimple = quranRepository.getChapterNameSimple(template.chapterNumber)
    val desc =
        stringResource(Res.string.surah_ayah)
            .replace("%1", chapterNameSimple)
            .replace("%2", "${template.verseNumber}")
    Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        if (template.label.isNotEmpty()) {
            BaseTitle(template.label)
        }
        BaseMessageCard(
            textTranslation = template.translation,
            textCaption = desc,
            buttonText = stringResource(Res.string.see_all),
            onItemClick = onItemClick,
        )
    }
}

@Composable
private fun MessageCard(
    template: HomeTemplate.Message,
    onItemClick: () -> Unit,
) {
    Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        if (template.label.isNotEmpty()) {
            BaseTitle(template.label)
        }
        BaseMessageCard(
            textMessage =
                if (template.textResource != null) {
                    stringResource(template.textResource)
                } else {
                    template.textString
                },
            onItemClick = onItemClick,
        )
    }
}

@Composable
private fun SingleImageCard(
    template: HomeTemplate.SingleImage,
    onItemClick: (link: String) -> Unit,
) {
    BaseImage(
        modifier =
            Modifier
                .fillMaxWidth()
                .clickable { if (template.link.isNotEmpty()) onItemClick(template.link) },
        imageUrl = template.image,
    )
}

@Composable
private fun MultipleImageCard(
    template: HomeTemplate.MultipleImage,
    onItemClick: (link: String) -> Unit,
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        val pagerState = rememberPagerState(pageCount = { template.images.size })

        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxWidth(),
        ) { index ->
            val link = template.links[index]
            val image = template.images[index]
            BaseImage(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .clip(MaterialTheme.shapes.small)
                        .clickable { if (link.isNotEmpty()) onItemClick(link) },
                imageUrl = image,
            )
        }

        BaseIndicator(
            pagerState,
            modifier =
                Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 8.dp, bottom = 16.dp),
        )
    }
}

@Composable
private fun VideoCard(
    template: HomeTemplate.Video,
    onItemClick: (link: String) -> Unit,
) {
    Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        if (template.label.isNotEmpty()) {
            BaseTitle(template.label)
        }
        BaseImage(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .clip(MaterialTheme.shapes.small)
                    .clickable { if (template.link.isNotEmpty()) onItemClick(template.link) },
            imageUrl = template.image,
            contentScale = ContentScale.FillWidth,
        )
    }
}
