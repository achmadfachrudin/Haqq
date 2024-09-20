package feature.home.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import core.ui.component.BaseArabic
import core.ui.component.BaseButton
import core.ui.component.BaseDialog
import core.ui.component.BaseOutlineTextField
import core.ui.component.BaseSpacerHorizontal
import core.ui.component.BaseSpacerVertical
import core.ui.component.BaseTabRow
import core.ui.component.BaseText
import core.ui.component.BaseTitle
import core.ui.component.ErrorState
import core.ui.component.LoadingState
import core.ui.theme.getHaqqTypography
import core.util.searchBy
import feature.other.service.mapper.getString
import feature.other.service.model.AppString
import feature.quran.service.QuranRepository
import feature.quran.service.model.Chapter
import feature.quran.service.model.Juz
import feature.quran.service.model.LastRead
import feature.quran.service.model.Page
import feature.quran.service.model.QuranConstant.MAX_CHAPTER
import feature.quran.service.model.VerseFavorite
import haqq.composeapp.generated.resources.Res
import haqq.composeapp.generated.resources.download
import haqq.composeapp.generated.resources.hexagonal
import org.jetbrains.compose.resources.painterResource
import org.koin.mp.KoinPlatform

@Composable
internal fun MainPageQuran(
    state: MainScreenModel.QuranState,
    onRetryClick: () -> Unit,
    onLastReadClick: (lastRead: LastRead) -> Unit,
    onDownloadClick: () -> Unit,
    onChapterClick: (chapter: Chapter) -> Unit,
    onJuzClick: (juz: Juz) -> Unit,
    onPageClick: (page: Page) -> Unit,
    onFavoriteClick: (verse: VerseFavorite) -> Unit,
    onRemoveFavoriteClick: (verse: VerseFavorite) -> Unit,
) {
    val openDownloadConfirmationDialog = remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize(),
    ) {
        val tabTitles =
            listOf(
                AppString.SURAH_TITLE.getString(),
                AppString.JUZ_TITLE.getString(),
                AppString.PAGES_TITLE.getString(),
                AppString.QURAN_FAVORITE_TITLE.getString(),
            )
        val pagerState = rememberPagerState(pageCount = { tabTitles.size })

        state.lastRead?.let {
            LastReadCard(
                lastRead = state.lastRead,
                onClick = {
                    onLastReadClick(state.lastRead)
                },
            )
        }

        if (state.downloadState == MainScreenModel.QuranDownloadState.Ready) {
            BaseButton(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                onClick = { openDownloadConfirmationDialog.value = true },
                text = AppString.DOWNLOAD_ALL_SURAHS.getString(),
            )
        }

        BaseTabRow(
            pagerState = pagerState,
            tabTitles = tabTitles,
        )

        HorizontalPager(
            state = pagerState,
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.Top,
        ) { index ->
            when (index) {
                0 -> {
                    ChapterContent(
                        state = state.chapterState,
                        onRetryClick = onRetryClick,
                        onChapterClick = onChapterClick,
                    )
                }

                1 -> {
                    JuzContent(
                        state = state.juzState,
                        onRetryClick = onRetryClick,
                        onJuzClick = onJuzClick,
                    )
                }

                2 -> {
                    if (state.downloadState == MainScreenModel.QuranDownloadState.Done) {
                        PageContent(
                            state = state.pageState,
                            onRetryClick = onRetryClick,
                            onPageClick = onPageClick,
                        )
                    } else {
                        ErrorState(AppString.PLEASE_DOWNLOAD_MUSHAF.getString())
                    }
                }

                else -> {
                    FavoriteContent(
                        favorites = state.favorites,
                        onFavoriteClick = onFavoriteClick,
                        onRemoveFavoriteClick = onRemoveFavoriteClick,
                    )
                }
            }
        }
    }

    if (openDownloadConfirmationDialog.value) {
        BaseDialog(
            onDismissRequest = { openDownloadConfirmationDialog.value = false },
            title = AppString.DOWNLOAD_ALL_SURAHS.getString(),
            desc = AppString.DOWNLOAD_ALERT.getString(),
            negativeButtonText = AppString.CANCEL.getString(),
            positiveButtonText = AppString.DOWNLOAD.getString(),
            onPositiveClicked = {
                openDownloadConfirmationDialog.value = false
                onDownloadClick()
            },
        )
    }

    if (state.downloadState == MainScreenModel.QuranDownloadState.Downloading) {
        val downloadMessage = "${state.verseDownloading}/${MAX_CHAPTER}"
        val downloadPercent = state.verseDownloading.toFloat() / MAX_CHAPTER.toFloat()

        DownloadProgressDialog(
            title = AppString.DOWNLOAD_ALL_SURAHS.getString(),
            downloadMessage = downloadMessage,
            downloadProgress = downloadPercent,
        )
    }
}

@Composable
private fun DownloadProgressDialog(
    title: String,
    downloadMessage: String,
    downloadProgress: Float,
) {
    Dialog(
        onDismissRequest = {},
        properties = DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false),
    ) {
        Card {
            Column(modifier = Modifier.fillMaxWidth().padding(24.dp)) {
                BaseTitle(text = title)
                BaseSpacerVertical()
                LinearProgressIndicator(
                    modifier = Modifier.fillMaxWidth(),
                    progress = { downloadProgress },
                    trackColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.4f),
                )
                BaseText(text = downloadMessage)
                BaseSpacerVertical()
                BaseText(text = AppString.DOWNLOAD_IN_PROGRESS.getString())
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ChapterContent(
    state: MainScreenModel.QuranChapterState,
    onRetryClick: () -> Unit,
    onChapterClick: (chapter: Chapter) -> Unit,
) {
    when (state) {
        is MainScreenModel.QuranChapterState.Loading -> {
            LoadingState()
        }

        is MainScreenModel.QuranChapterState.Content -> {
            val valueSearch = remember { mutableStateOf("") }
            val query = valueSearch.value.lowercase()
            val chapterFiltered =
                state.chapters.filter {
                    it.nameSimple.searchBy(query) ||
                        it.nameTranslation.searchBy(query) ||
                        it.id.toString().searchBy(query)
                }

            LazyColumn(contentPadding = PaddingValues(bottom = 16.dp)) {
                stickyHeader {
                    Surface(Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            BaseOutlineTextField(
                                modifier =
                                    Modifier.weight(1f).padding(horizontal = 16.dp, vertical = 8.dp),
                                value = valueSearch.value,
                                onValueChange = { newText ->
                                    valueSearch.value =
                                        newText
                                            .trim()
                                            .filter { it.isLetterOrDigit() }
                                },
                                label = AppString.SEARCH_SURAH.getString(),
                                trailingClick = { valueSearch.value = "" },
                                keyboardOptions =
                                    KeyboardOptions.Default.copy(
                                        keyboardType = KeyboardType.Text,
                                        imeAction = ImeAction.Done,
                                    ),
                            )
                        }
                    }
                }
                items(chapterFiltered) { chapter ->
                    ChapterCard(
                        chapter = chapter,
                        onClick = { onChapterClick(chapter) },
                    )
                }
            }
        }

        is MainScreenModel.QuranChapterState.Error -> {
            ErrorState(
                message = state.message,
                showRetryButton = true,
                onRetryButtonClick = onRetryClick,
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun JuzContent(
    state: MainScreenModel.QuranJuzState,
    onRetryClick: () -> Unit,
    onJuzClick: (juz: Juz) -> Unit,
) {
    when (state) {
        is MainScreenModel.QuranJuzState.Loading -> {
            LoadingState()
        }

        is MainScreenModel.QuranJuzState.Content -> {
            val valueSearch = remember { mutableStateOf("") }
            val query = valueSearch.value.lowercase()
            val juzFiltered =
                state.juzs.filter {
                    it.juzNumber.toString().searchBy(query)
                }

            LazyColumn(contentPadding = PaddingValues(bottom = 16.dp)) {
                stickyHeader {
                    Surface(Modifier.fillMaxWidth()) {
                        BaseOutlineTextField(
                            modifier =
                                Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 8.dp),
                            value = valueSearch.value,
                            onValueChange = { newText ->
                                valueSearch.value =
                                    newText
                                        .trim()
                                        .filter { it.isLetterOrDigit() }
                            },
                            label = AppString.SEARCH_JUZ.getString(),
                            trailingClick = { valueSearch.value = "" },
                            keyboardOptions =
                                KeyboardOptions.Default.copy(
                                    keyboardType = KeyboardType.Number,
                                    imeAction = ImeAction.Done,
                                ),
                        )
                    }
                }
                items(juzFiltered) { juz ->
                    JuzCard(
                        juz = juz,
                        onClick = { onJuzClick(juz) },
                    )
                }
            }
        }

        is MainScreenModel.QuranJuzState.Error -> {
            ErrorState(
                message = state.message,
                showRetryButton = true,
                onRetryButtonClick = onRetryClick,
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun PageContent(
    state: MainScreenModel.QuranPageState,
    onRetryClick: () -> Unit,
    onPageClick: (page: Page) -> Unit,
) {
    when (state) {
        is MainScreenModel.QuranPageState.Loading -> {
            LoadingState()
        }

        is MainScreenModel.QuranPageState.Content -> {
            val valueSearch = remember { mutableStateOf("") }
            val query = valueSearch.value.lowercase()
            val pageFiltered =
                state.pages.filter {
                    it.pageNumber.toString().searchBy(query)
                }

            LazyColumn(contentPadding = PaddingValues(bottom = 16.dp)) {
                stickyHeader {
                    Surface(Modifier.fillMaxWidth()) {
                        BaseOutlineTextField(
                            modifier =
                                Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 8.dp),
                            value = valueSearch.value,
                            onValueChange = { newText ->
                                valueSearch.value =
                                    newText
                                        .trim()
                                        .filter { it.isLetterOrDigit() }
                            },
                            label = AppString.SEARCH_PAGE.getString(),
                            trailingClick = { valueSearch.value = "" },
                            keyboardOptions =
                                KeyboardOptions.Default.copy(
                                    keyboardType = KeyboardType.Number,
                                    imeAction = ImeAction.Done,
                                ),
                        )
                    }
                }

                items(pageFiltered) { page ->
                    PageCard(
                        page = page,
                        onClick = { onPageClick(page) },
                    )
                }
            }
        }

        is MainScreenModel.QuranPageState.Error -> {
            ErrorState(
                message = state.message,
                showRetryButton = true,
                onRetryButtonClick = onRetryClick,
            )
        }
    }
}

@Composable
private fun FavoriteContent(
    favorites: List<VerseFavorite>,
    onFavoriteClick: (verse: VerseFavorite) -> Unit,
    onRemoveFavoriteClick: (verse: VerseFavorite) -> Unit,
) {
    val openDeleteFavoriteDialog = remember { mutableStateOf(false) }
    val selectedVerse = remember { mutableStateOf<VerseFavorite?>(null) }

    LazyColumn(
        contentPadding = PaddingValues(bottom = 16.dp),
    ) {
        items(favorites) { verse ->
            FavoriteCard(
                verse = verse,
                onClick = { onFavoriteClick(verse) },
                onLongClick = {
                    selectedVerse.value = verse
                    openDeleteFavoriteDialog.value = true
                },
            )
        }
    }

    if (openDeleteFavoriteDialog.value && selectedVerse.value != null) {
        val title =
            AppString.SURAH_AYAH
                .getString()
                .replace("%1", selectedVerse.value!!.chapterNameSimple)
                .replace("%2", "${selectedVerse.value!!.verseNumber}")

        BaseDialog(
            onDismissRequest = {
                selectedVerse.value = null
                openDeleteFavoriteDialog.value = false
            },
            title = AppString.DELETE_CONFIRMATION_TITLE.getString(),
            desc =
                """
                $title
                ${AppString.DELETE_CONFIRMATION_DESC.getString()}
                """.trimIndent(),
            negativeButtonText = AppString.CANCEL.getString(),
            positiveButtonText = AppString.DELETE.getString(),
            onPositiveClicked = {
                onRemoveFavoriteClick(selectedVerse.value!!)
                selectedVerse.value = null
                openDeleteFavoriteDialog.value = false
            },
        )
    }
}

@Composable
private fun LastReadCard(
    lastRead: LastRead,
    onClick: () -> Unit,
) {
    val title = AppString.LASTREAD_CONTINUE.getString()
    val description =
        AppString.SURAH_AYAH
            .getString()
            .replace("%1", lastRead.chapterNameSimple)
            .replace("%2", lastRead.verseNumber.toString())

    Card(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        onClick = { onClick() },
    ) {
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(
                modifier = Modifier.weight(1f),
            ) {
                BaseText(text = title)

                BaseTitle(text = description)
            }
            Box(contentAlignment = Alignment.Center) {
                CircularProgressIndicator(
                    modifier = Modifier.size(50.dp),
                    progress = { lastRead.progressFloat },
                    strokeWidth = 5.dp,
                    trackColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.4f),
                )

                BaseText(
                    text = "${lastRead.progressInt}%",
                    style = getHaqqTypography().bodySmall,
                )
            }
        }
    }
}

@Composable
private fun ChapterCard(
    chapter: Chapter,
    onClick: (chapter: Chapter) -> Unit,
) {
    val verses = "${chapter.versesCount} ${AppString.AYAHS.getString()}"
    val chapterNumber = "${chapter.id}"

    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .clickable {
                    onClick(chapter)
                }.padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Box(contentAlignment = Alignment.Center) {
            Icon(
                modifier = Modifier.size(38.dp),
                painter = painterResource(Res.drawable.hexagonal),
                tint = MaterialTheme.colorScheme.primary,
                contentDescription = chapterNumber,
            )
            BaseText(text = chapterNumber, style = getHaqqTypography().bodySmall)
        }
        Column(modifier = Modifier.weight(1f).padding(horizontal = 16.dp)) {
            BaseText(
                text = chapter.nameComplex,
            )
            BaseText(
                text = chapter.nameTranslation,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
        Column(horizontalAlignment = Alignment.End) {
            BaseArabic(
                text = chapter.nameArabic,
            )
            BaseText(
                text = verses,
            )
        }
        if (!chapter.isDownloaded) {
            BaseSpacerHorizontal(8)

            Icon(
                painter = painterResource(Res.drawable.download),
                contentDescription = null,
            )
        }
    }
}

@Composable
private fun JuzCard(
    juz: Juz,
    onClick: () -> Unit,
) {
    val quranRepository = KoinPlatform.getKoin().get<QuranRepository>()
    val firstChapterName = quranRepository.getChapterById(juz.firstChapterNumber)
    val start =
        AppString.FROM_SURAH_AND_AYAH
            .getString()
            .replace("%1", firstChapterName.nameSimple)
            .replace("%2", juz.firstVerseNumber.toString())
    val verses = "${juz.versesCount} ${AppString.AYAHS.getString()}"
    val juzNumber = "${juz.juzNumber}"

    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .clickable {
                    onClick()
                }.padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Box(contentAlignment = Alignment.Center) {
            Icon(
                modifier = Modifier.size(38.dp),
                painter = painterResource(Res.drawable.hexagonal),
                tint = MaterialTheme.colorScheme.primary,
                contentDescription = juzNumber,
            )
            BaseText(text = juzNumber, style = getHaqqTypography().bodySmall)
        }
        BaseText(
            modifier = Modifier.weight(1f).padding(horizontal = 16.dp),
            text = start,
        )
        BaseText(
            text = verses,
        )
    }
}

@Composable
private fun PageCard(
    page: Page,
    onClick: () -> Unit,
) {
    val quranRepository = KoinPlatform.getKoin().get<QuranRepository>()
    val chapterName = quranRepository.getChapterNameSimple(page.firstChapterNumber)
    val start =
        AppString.FROM_SURAH_AND_AYAH
            .getString()
            .replace("%1", chapterName)
            .replace("%2", page.firstVerseNumber.toString())
    val verses = "${page.versesCount} ${AppString.AYAHS.getString()}"
    val pageNumber = "${page.pageNumber}"

    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .clickable {
                    onClick()
                }.padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Box(contentAlignment = Alignment.Center) {
            Icon(
                modifier = Modifier.size(38.dp),
                painter = painterResource(Res.drawable.hexagonal),
                tint = MaterialTheme.colorScheme.primary,
                contentDescription = pageNumber,
            )
            BaseText(text = pageNumber, style = getHaqqTypography().bodySmall)
        }
        BaseText(
            modifier = Modifier.weight(1f).padding(horizontal = 16.dp),
            text = start,
        )
        BaseText(
            text = verses,
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun FavoriteCard(
    verse: VerseFavorite,
    onClick: () -> Unit,
    onLongClick: () -> Unit,
) {
    val title =
        AppString.SURAH_AYAH
            .getString()
            .replace("%1", verse.chapterNameSimple)
            .replace("%2", "${verse.verseNumber}")

    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .combinedClickable(
                    onClick = { onClick() },
                    onLongClick = { onLongClick() },
                ).padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        BaseText(
            modifier = Modifier.weight(1f).padding(horizontal = 16.dp),
            text = title,
        )
    }
}
