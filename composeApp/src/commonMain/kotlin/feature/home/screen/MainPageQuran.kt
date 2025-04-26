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
import androidx.compose.runtime.saveable.rememberSaveable
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
import core.ui.component.BaseSpacerVertical
import core.ui.component.BaseTabRow
import core.ui.component.BaseText
import core.ui.component.BaseTitle
import core.ui.component.ErrorState
import core.ui.component.LoadingState
import core.ui.theme.getHaqqTypography
import core.util.searchBy
import feature.quran.service.QuranRepository
import feature.quran.service.model.Chapter
import feature.quran.service.model.Juz
import feature.quran.service.model.LastRead
import feature.quran.service.model.Page
import feature.quran.service.model.QuranConstant.MAX_CHAPTER
import feature.quran.service.model.VerseFavorite
import haqq.composeapp.generated.resources.Res
import haqq.composeapp.generated.resources.ayahs
import haqq.composeapp.generated.resources.cancel
import haqq.composeapp.generated.resources.delete
import haqq.composeapp.generated.resources.delete_confirmation_desc
import haqq.composeapp.generated.resources.delete_confirmation_title
import haqq.composeapp.generated.resources.download
import haqq.composeapp.generated.resources.download_alert
import haqq.composeapp.generated.resources.download_all_surahs
import haqq.composeapp.generated.resources.download_in_progress
import haqq.composeapp.generated.resources.from_surah_and_ayah
import haqq.composeapp.generated.resources.hexagonal
import haqq.composeapp.generated.resources.juz_title
import haqq.composeapp.generated.resources.lastread_continue
import haqq.composeapp.generated.resources.pages_title
import haqq.composeapp.generated.resources.please_download_mushaf
import haqq.composeapp.generated.resources.quran_favorite_title
import haqq.composeapp.generated.resources.search_juz
import haqq.composeapp.generated.resources.search_page
import haqq.composeapp.generated.resources.search_surah
import haqq.composeapp.generated.resources.surah_ayah
import haqq.composeapp.generated.resources.surah_title
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.mp.KoinPlatform

@Composable
internal fun MainPageQuran(
    state: MainScreenModel.State,
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
                stringResource(Res.string.surah_title),
                stringResource(Res.string.juz_title),
                stringResource(Res.string.pages_title),
                stringResource(Res.string.quran_favorite_title),
            )
        val pagerState = rememberPagerState(pageCount = { tabTitles.size })

        LastReadCard(
            lastRead = state.lastRead,
            onClick = {
                onLastReadClick(state.lastRead)
            },
        )

        if (state.quranDownloadState == MainScreenModel.QuranDownloadState.Ready) {
            BaseButton(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                onClick = { openDownloadConfirmationDialog.value = true },
                text = stringResource(Res.string.download_all_surahs),
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
                        state = state.quranChapterState,
                        onRetryClick = onRetryClick,
                        onChapterClick = onChapterClick,
                    )
                }

                1 -> {
                    if (state.quranDownloadState == MainScreenModel.QuranDownloadState.Done) {
                        JuzContent(
                            state = state.quranJuzState,
                            onRetryClick = onRetryClick,
                            onJuzClick = onJuzClick,
                        )
                    } else {
                        ErrorState(stringResource(Res.string.please_download_mushaf))
                    }
                }

                2 -> {
                    if (state.quranDownloadState == MainScreenModel.QuranDownloadState.Done) {
                        PageContent(
                            state = state.quranPageState,
                            onRetryClick = onRetryClick,
                            onPageClick = onPageClick,
                        )
                    } else {
                        ErrorState(stringResource(Res.string.please_download_mushaf))
                    }
                }

                else -> {
                    FavoriteContent(
                        favorites = state.verseFavorites,
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
            title = stringResource(Res.string.download_all_surahs),
            desc = stringResource(Res.string.download_alert),
            negativeButtonText = stringResource(Res.string.cancel),
            positiveButtonText = stringResource(Res.string.download),
            onPositiveClicked = {
                openDownloadConfirmationDialog.value = false
                onDownloadClick()
            },
        )
    }

    if (state.quranDownloadState == MainScreenModel.QuranDownloadState.Downloading) {
        val downloadMessage = "${state.verseDownloading}/${MAX_CHAPTER}"
        val downloadPercent = state.verseDownloading.toFloat() / MAX_CHAPTER.toFloat()

        DownloadProgressDialog(
            title = stringResource(Res.string.download_all_surahs),
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
                BaseText(text = stringResource(Res.string.download_in_progress))
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
            val valueSearch = rememberSaveable { mutableStateOf("") }
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
                                label = stringResource(Res.string.search_surah),
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
            val valueSearch = rememberSaveable { mutableStateOf("") }
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
                            label = stringResource(Res.string.search_juz),
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
            val valueSearch = rememberSaveable { mutableStateOf("") }
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
                            label = stringResource(Res.string.search_page),
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
            stringResource(Res.string.surah_ayah)
                .replace("%1", selectedVerse.value!!.chapterNameSimple)
                .replace("%2", "${selectedVerse.value!!.verseNumber}")

        BaseDialog(
            onDismissRequest = {
                selectedVerse.value = null
                openDeleteFavoriteDialog.value = false
            },
            title = stringResource(Res.string.delete_confirmation_title),
            desc =
                """
                $title
                ${stringResource(Res.string.delete_confirmation_desc)}
                """.trimIndent(),
            negativeButtonText = stringResource(Res.string.cancel),
            positiveButtonText = stringResource(Res.string.delete),
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
    val title = stringResource(Res.string.lastread_continue)
    val description =
        stringResource(Res.string.surah_ayah)
            .replace("%1", lastRead.chapterNameSimple)
            .replace("%2", lastRead.verseNumber.toString())

    Card(
        modifier = Modifier.fillMaxWidth().padding(top = 16.dp, start = 16.dp, end = 16.dp, bottom = 8.dp),
        onClick = { onClick() },
    ) {
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
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
                    modifier = Modifier.size(42.dp),
                    progress = { lastRead.progressFloat },
                    strokeWidth = 5.dp,
                    trackColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.4f),
                )

                BaseText(
                    text = "${lastRead.progressInt}%",
                    style = getHaqqTypography().labelSmall,
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
    val verses = "${chapter.versesCount} ${stringResource(Res.string.ayahs)}"
    val chapterNumber = "${chapter.id}"

    if (chapter.isDownloaded) {
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .clickable { onClick(chapter) }
                    .padding(vertical = 4.dp, horizontal = 16.dp),
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
        stringResource(Res.string.from_surah_and_ayah)
            .replace("%1", firstChapterName.nameSimple)
            .replace("%2", juz.firstVerseNumber.toString())
    val verses = "${juz.versesCount} ${stringResource(Res.string.ayahs)}"
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
        stringResource(Res.string.from_surah_and_ayah)
            .replace("%1", chapterName)
            .replace("%2", page.firstVerseNumber.toString())
    val verses = "${page.versesCount} ${stringResource(Res.string.ayahs)}"
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
        stringResource(Res.string.surah_ayah)
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
