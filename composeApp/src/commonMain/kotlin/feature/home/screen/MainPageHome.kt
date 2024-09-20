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
import feature.other.service.mapper.getString
import feature.other.service.model.AppString
import feature.quran.service.QuranRepository
import org.koin.mp.KoinPlatform

@Composable
internal fun MainPageHome(
    state: MainScreenModel.MainState,
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
            MainScreenModel.MainState.Loading -> {
                LoadingState()
            }

            is MainScreenModel.MainState.Content -> {
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

            is MainScreenModel.MainState.Error -> {
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
            BaseSpacerVertical()
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
                    text = AppString.NEXT_PRAYER_TIME.getString(),
                )
                BaseText(
                    text = template.nextPrayerName,
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
                    Text(AppString.SEE_ALL.getString())
                }
                BaseText(
                    text = template.nextPrayerTime,
                    style = getHaqqTypography().titleLarge,
                )
            }
        }

        BaseText(
            text = AppString.PRAYER_NOTE.getString(),
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
            DhikrType.MORNING -> AppString.DHIKR_MORNING_TITLE
            DhikrType.AFTERNOON -> AppString.DHIKR_AFTERNOON_TITLE
            DhikrType.PRAY -> AppString.DHIKR_PRAY_TITLE
            DhikrType.SLEEP -> AppString.DHIKR_SLEEP_TITLE
            DhikrType.RUQYAH -> AppString.DHIKR_RUQYAH_TITLE
        }.getString()

    Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        if (template.label.isNotEmpty()) {
            BaseTitle(template.label)
            BaseSpacerVertical()
        }
        BaseMessageCard(
            textMessage = AppString.READ_DHIKR.getString().replace("%1", dhikrName),
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
            BaseSpacerVertical()
        }
        BaseMessageCard(
            textArabic = template.arabic,
            textMessage =
                AppString.SURAH_AYAH
                    .getString()
                    .replace("%1", chapterNameSimple)
                    .replace("%2", "${template.verseNumber}"),
            buttonText = AppString.LASTREAD_CONTINUE.getString(),
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
        AppString.SURAH_AYAH
            .getString()
            .replace("%1", chapterNameSimple)
            .replace("%2", "${template.verseNumber}")
    Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        if (template.label.isNotEmpty()) {
            BaseTitle(template.label)
            BaseSpacerVertical()
        }
        BaseMessageCard(
            textMessage =
                """
                ${template.translation}
                $desc
                """.trimIndent(),
            buttonText = AppString.SEE_ALL.getString(),
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
            BaseSpacerVertical()
        }
        BaseMessageCard(
            textMessage = template.text,
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
            BaseSpacerVertical()
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
