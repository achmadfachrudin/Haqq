package feature.quran.screen

import AnalyticsConstant.trackScreen
import SendMail
import ShareText
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import core.ui.component.ArabicCard
import core.ui.component.BaseButton
import core.ui.component.BaseDialog
import core.ui.component.BaseItemCard
import core.ui.component.BaseOutlineTextField
import core.ui.component.BasePageQuran
import core.ui.component.BaseSpacerVertical
import core.ui.component.BaseText
import core.ui.component.BaseTopAppBar
import core.ui.component.BismillahCard
import core.ui.component.ErrorState
import core.ui.component.LoadingState
import core.ui.theme.getHaqqTypography
import core.util.searchBy
import feature.quran.service.model.Chapter
import feature.quran.service.model.ReadMode
import feature.quran.service.model.Verse
import haqq.composeapp.generated.resources.Res
import haqq.composeapp.generated.resources.add_to_favorite
import haqq.composeapp.generated.resources.alert_circle
import haqq.composeapp.generated.resources.bg_frame_surah
import haqq.composeapp.generated.resources.bookmark
import haqq.composeapp.generated.resources.cancel
import haqq.composeapp.generated.resources.clear_search
import haqq.composeapp.generated.resources.copy
import haqq.composeapp.generated.resources.corner_left_down
import haqq.composeapp.generated.resources.heart
import haqq.composeapp.generated.resources.heart_filled
import haqq.composeapp.generated.resources.jump_to_ayah
import haqq.composeapp.generated.resources.page
import haqq.composeapp.generated.resources.remove_from_favorite
import haqq.composeapp.generated.resources.report
import haqq.composeapp.generated.resources.reset
import haqq.composeapp.generated.resources.reset_confirmation_title
import haqq.composeapp.generated.resources.reset_verse_note
import haqq.composeapp.generated.resources.save_as_lastread
import haqq.composeapp.generated.resources.search
import haqq.composeapp.generated.resources.search_ayah
import haqq.composeapp.generated.resources.search_ayah_hint
import haqq.composeapp.generated.resources.search_content
import haqq.composeapp.generated.resources.share
import haqq.composeapp.generated.resources.surah_ayah
import haqq.composeapp.generated.resources.surah_desc
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Serializable
data class VerseListNav(
    val readModeName: String,
    val id: Int, // chapterId / juzId / pageId
    val verseNumber: Int = 1,
)

@Composable
fun VerseListScreen(
    nav: VerseListNav,
    onBackClick: () -> Unit,
) {
    val vm = koinViewModel<VerseListScreenModel>()
    val state by vm.state.collectAsState()

    val openSearchDialog = remember { mutableStateOf(false) }
    val openResetDialog = remember { mutableStateOf(false) }
    val openJumpVerseDialog = remember { mutableStateOf(false) }
    val openVerseDialog = remember { mutableStateOf(false) }
    val openMail = remember { mutableStateOf(false) }
    val openShare = remember { mutableStateOf(false) }
    val shareContent = remember { mutableStateOf("") }
    val selectedVerse = remember { mutableStateOf<Verse?>(null) }
    val clipboardManager = LocalClipboardManager.current

    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    val readMode = enumValueOf<ReadMode>(nav.readModeName)

    val title =
        when (readMode) {
            ReadMode.BY_CHAPTER -> state.chapter?.nameSimple.orEmpty()
            ReadMode.BY_JUZ -> "Juz ${state.juz?.juzNumber}"
            ReadMode.BY_PAGE -> "${stringResource(Res.string.page)} ${state.page?.pageNumber}"
        }

    Scaffold(
        topBar = {
            BaseTopAppBar(
                title = title,
                showOptionalButton = state.verseState is VerseListScreenModel.VerseState.Content && state.readMode != ReadMode.BY_PAGE,
                showRightButton = state.verseState is VerseListScreenModel.VerseState.Content && state.readMode != ReadMode.BY_PAGE,
                optionalButtonImage = painterResource(Res.drawable.corner_left_down),
                rightButtonImage = painterResource(Res.drawable.search),
                onLeftButtonClick = {
                    onBackClick()
                },
                onOptionalButtonClick = { openJumpVerseDialog.value = true },
                onRightButtonClick = { openSearchDialog.value = true },
            )
        },
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues).fillMaxSize()) {
            when (val display = state.verseState) {
                is VerseListScreenModel.VerseState.Loading -> {
                    LoadingState()
                }

                is VerseListScreenModel.VerseState.Content -> {
                    when (state.readMode) {
                        ReadMode.BY_CHAPTER,
                        ReadMode.BY_JUZ,
                        -> {
                            if (state.query.isNotEmpty()) {
                                Column(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                ) {
                                    BaseText(
                                        color = MaterialTheme.colorScheme.tertiary,
                                        text = "${stringResource(Res.string.search_content)} ${state.query}",
                                    )

                                    TextButton(onClick = {
                                        vm.updateQuery("")
                                    }) {
                                        Text(stringResource(Res.string.clear_search))
                                    }
                                }
                            }

                            val query = state.query
                            val versesFiltered =
                                display.verses.filter {
                                    it.textTranslation.searchBy(query) ||
                                        it.textTransliteration.searchBy(query) ||
                                        it.verseNumber.toString().searchBy(query)
                                }

                            LazyColumn(state = listState) {
                                itemsIndexed(versesFiltered) { index, verse ->
                                    if (verse.verseNumber == 1 && state.query.isEmpty()) {
                                        HeaderChapter(vm.getChapter(verse.chapterId))
                                    }

                                    ArabicCard(
                                        title = "${verse.chapterId}:${verse.verseNumber}",
                                        textArabic = verse.textArabic,
                                        textTransliteration = verse.textTransliteration,
                                        textTranslation = verse.textTranslation,
                                        verseNumber = verse.verseNumber,
                                        sajdahNumber = verse.sajdahNumber,
                                        onClick = {
                                            selectedVerse.value = verse
                                            openVerseDialog.value = true
                                        },
                                    )
                                }

                                // next button
                                if (query.isEmpty() &&
                                    (state.nextChapter != null || state.nextJuz != null)
                                ) {
                                    item {
                                        BaseButton(
                                            modifier = Modifier.padding(16.dp).fillMaxWidth(),
                                            text = vm.getNextText(),
                                            onClick = {
                                                scope.launch {
                                                    vm.goToNext()
                                                    listState.scrollToItem(index = 0)
                                                }
                                            },
                                        )
                                    }
                                }
                            }
                        }

                        ReadMode.BY_PAGE -> {
                            Column(
                                modifier =
                                    Modifier
                                        .fillMaxSize()
                                        .verticalScroll(rememberScrollState()),
                            ) {
                                val subList = vm.splitVersesById(display.verses)
                                subList.forEach {
                                    if (it.first().verseNumber == 1) {
                                        HeaderChapter(vm.getChapter(it.first().chapterId))
                                    }

                                    BasePageQuran(
                                        it,
                                        modifier = Modifier.padding(horizontal = 16.dp),
                                    )

                                    BaseSpacerVertical()
                                }

                                // next button
                                if (state.nextPage != null) {
                                    BaseButton(
                                        modifier = Modifier.padding(16.dp).fillMaxWidth(),
                                        text = vm.getNextText(),
                                        onClick = {
                                            scope.launch {
                                                vm.goToNext()
                                            }
                                        },
                                    )
                                }
                            }
                        }
                    }
                }

                is VerseListScreenModel.VerseState.Error -> {
                    ErrorState(
                        message = display.message,
                        showRetryButton = state.readMode != ReadMode.BY_CHAPTER,
                        onRetryButtonClick = { openResetDialog.value = true },
                    )
                }
            }
        }
    }

    if (openSearchDialog.value) {
        SearchDialog(
            currentQuery = state.query,
            onDismissRequest = { openSearchDialog.value = false },
            onSearchClicked = {
                openSearchDialog.value = false
                vm.updateQuery(it)
            },
        )
    }

    if (openResetDialog.value) {
        BaseDialog(
            onDismissRequest = { openResetDialog.value = false },
            title = stringResource(Res.string.reset_confirmation_title),
            desc = stringResource(Res.string.reset_verse_note),
            negativeButtonText = stringResource(Res.string.cancel),
            positiveButtonText = stringResource(Res.string.reset),
            onPositiveClicked = {
                vm.resetVerses(nav.id)
                openResetDialog.value = false
            },
        )
    }

    if (openJumpVerseDialog.value) {
        BaseDialog(
            onDismissRequest = { openJumpVerseDialog.value = false },
            title = stringResource(Res.string.jump_to_ayah),
            shouldCustomContent = true,
            content = {
                val valueSearch = remember { mutableStateOf("") }

                BaseOutlineTextField(
                    value = valueSearch.value,
                    onValueChange = { newText ->
                        valueSearch.value =
                            newText
                                .trim()
                                .filter { it.isDigit() }
                    },
                    label = stringResource(Res.string.search_ayah),
                    trailingClick = { valueSearch.value = "" },
                    keyboardOptions =
                        KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Search,
                        ),
                )

                BaseSpacerVertical()

                if (state.verseState is VerseListScreenModel.VerseState.Content) {
                    val versesFiltered =
                        (state.verseState as VerseListScreenModel.VerseState.Content).verses.filter {
                            it.verseNumber.toString().searchBy(valueSearch.value)
                        }

                    LazyColumn(modifier = Modifier.height(300.dp)) {
                        items(versesFiltered) { verse ->
                            BaseItemCard(
                                title = "${verse.chapterId}:${verse.verseNumber}",
                                onClick = {
                                    vm.updateQuery("")
                                    scope.launch {
                                        delay(300)
                                        listState.scrollToItem(
                                            index =
                                                vm.getIndex(
                                                    verse,
                                                ),
                                        )
                                    }
                                    openJumpVerseDialog.value = false
                                },
                            )
                        }
                    }
                }
            },
        )
    }

    if (openVerseDialog.value) {
        selectedVerse.value?.let { verse ->
            val chapterNameSimple = vm.getChapter(verse.chapterId).nameSimple
            val isFavorite = vm.isFavorite(verse.id)

            val shareTitle =
                stringResource(Res.string.surah_ayah)
                    .replace("%1", chapterNameSimple)
                    .replace("%2", "${verse.verseNumber}")

            val shareMessage =
                """
                ${verse.textArabic}
                ${verse.textTransliteration}
                ${verse.textTranslation}
                ($shareTitle)
                """.trimIndent()

            BaseDialog(
                onDismissRequest = { openVerseDialog.value = false },
                title = shareTitle,
                shouldCustomContent = true,
                content = {
                    VerseListScreenModel.VerseAction.entries.forEach { verseAction ->
                        when (verseAction) {
                            VerseListScreenModel.VerseAction.SAVE_AS_LASTREAD -> {
                                BaseItemCard(
                                    title = stringResource(Res.string.save_as_lastread),
                                    iconResource = Res.drawable.bookmark,
                                    onClick = {
                                        vm.updateLastRead(verse.id)
                                        openVerseDialog.value = false
                                        selectedVerse.value = null
                                    },
                                )
                            }

                            VerseListScreenModel.VerseAction.ADD_OR_REMOVE_FAVORITE -> {
                                val itemTitle =
                                    if (isFavorite) {
                                        stringResource(Res.string.remove_from_favorite)
                                    } else {
                                        stringResource(Res.string.add_to_favorite)
                                    }

                                val itemIcon =
                                    if (isFavorite) {
                                        Res.drawable.heart_filled
                                    } else {
                                        Res.drawable.heart
                                    }

                                BaseItemCard(
                                    title = itemTitle,
                                    iconResource = itemIcon,
                                    iconTint = MaterialTheme.colorScheme.error,
                                    onClick = {
                                        vm.addOrRemoveFavorite(verse)
                                        openVerseDialog.value = false
                                        selectedVerse.value = null
                                    },
                                )
                            }

                            VerseListScreenModel.VerseAction.SHARE -> {
                                BaseItemCard(
                                    title = stringResource(Res.string.share),
                                    iconResource = Res.drawable.share,
                                    onClick = {
                                        shareContent.value = shareMessage
                                        openShare.value = true
                                        openVerseDialog.value = false
                                        selectedVerse.value = null
                                    },
                                )
                            }

                            VerseListScreenModel.VerseAction.COPY -> {
                                BaseItemCard(
                                    title = stringResource(Res.string.copy),
                                    iconResource = Res.drawable.copy,
                                    onClick = {
                                        clipboardManager.setText(AnnotatedString(shareMessage))
                                        openVerseDialog.value = false
                                        selectedVerse.value = null
                                    },
                                )
                            }

                            VerseListScreenModel.VerseAction.REPORT -> {
                                BaseItemCard(
                                    title = stringResource(Res.string.report),
                                    iconResource = Res.drawable.alert_circle,
                                    onClick = {
                                        shareContent.value = shareMessage
                                        openMail.value = true
                                        openVerseDialog.value = false
                                        selectedVerse.value = null
                                    },
                                )
                            }
                        }
                    }
                },
            )
        }
    }

    if (openMail.value) {
        SendMail(subject = "[Verse-Report]", message = shareContent.value)
        openMail.value = false
        openVerseDialog.value = false
    }

    if (openShare.value) {
        ShareText(shareContent.value)
        openShare.value = false
        openVerseDialog.value = false
    }

    LaunchedEffect(Unit) {
        trackScreen("VerseListScreen-$readMode-${nav.id}-${nav.verseNumber}")

        scope.launch {
            when (readMode) {
                ReadMode.BY_CHAPTER -> {
                    vm.getVersesByChapter(nav.id)
                    listState.scrollToItem(index = nav.verseNumber - 1)
                }

                ReadMode.BY_JUZ -> {
                    vm.getVersesByJuz(nav.id)
                    listState.scrollToItem(index = 0)
                }

                ReadMode.BY_PAGE -> {
                    vm.getVersesByPage(nav.id)
                    listState.scrollToItem(index = 0)
                }
            }
        }
    }
}

@Composable
private fun HeaderChapter(chapter: Chapter) {
    Box {
        Image(
            modifier = Modifier.fillMaxWidth(),
            painter = painterResource(Res.drawable.bg_frame_surah),
            contentDescription = "",
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary),
        )
        Column(modifier = Modifier.fillMaxSize().align(Alignment.Center)) {
            BaseText(
                modifier = Modifier.fillMaxWidth(),
                text = chapter.nameArabic,
                style = getHaqqTypography().titleLarge,
                horizontalArrangement = Arrangement.Center,
            )
            BaseText(
                modifier = Modifier.fillMaxWidth(),
                text =
                    stringResource(Res.string.surah_desc)
                        .replace("%1", chapter.nameTranslation)
                        .replace(
                            "%2",
                            chapter.versesCount.toString(),
                        ).replace("%3", chapter.revelationPlace),
                horizontalArrangement = Arrangement.Center,
            )
            if (chapter.bismillahPre) {
                BismillahCard()
            }
        }
    }
}

@Composable
private fun SearchDialog(
    currentQuery: String,
    onDismissRequest: () -> Unit,
    onSearchClicked: (keyword: String) -> Unit,
) {
    val valueSearch = remember { mutableStateOf("") }
    valueSearch.value = currentQuery

    Dialog(onDismissRequest = { onDismissRequest() }) {
        Card {
            Column(modifier = Modifier.fillMaxWidth().padding(24.dp)) {
                BaseOutlineTextField(
                    value = valueSearch.value,
                    onValueChange = { newText ->
                        valueSearch.value =
                            newText
                                .trim()
                                .filter { it.isLetterOrDigit() }
                    },
                    label = stringResource(Res.string.search_ayah),
                    trailingClick = { valueSearch.value = "" },
                    supportingText = {
                        BaseText(
                            text = stringResource(Res.string.search_ayah_hint),
                            style = getHaqqTypography().labelSmall,
                        )
                    },
                    keyboardOptions =
                        KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Done,
                        ),
                )

                BaseSpacerVertical()

                BaseButton(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(Res.string.search),
                    onClick = { onSearchClicked(valueSearch.value) },
                )
            }
        }
    }
}
