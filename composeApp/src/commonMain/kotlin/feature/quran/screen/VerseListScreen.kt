package feature.quran.screen

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
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
import feature.other.service.mapper.getString
import feature.other.service.model.AppString
import feature.quran.service.model.Chapter
import feature.quran.service.model.ReadMode
import feature.quran.service.model.Verse
import haqq.composeapp.generated.resources.Res
import haqq.composeapp.generated.resources.alert_circle
import haqq.composeapp.generated.resources.bg_frame_surah
import haqq.composeapp.generated.resources.bookmark
import haqq.composeapp.generated.resources.copy
import haqq.composeapp.generated.resources.corner_left_down
import haqq.composeapp.generated.resources.heart
import haqq.composeapp.generated.resources.search
import haqq.composeapp.generated.resources.share
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

class VerseListScreen(
    val readMode: ReadMode,
    val id: Int, // chapterId / juzId / pageId
    val verseNumber: Int = 1,
) : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val screenModel = koinScreenModel<VerseListScreenModel>()
        val state by screenModel.state.collectAsState()

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

        val title =
            when (readMode) {
                ReadMode.BY_CHAPTER -> state.chapter?.nameSimple.orEmpty()
                ReadMode.BY_JUZ -> "Juz ${state.juz?.juzNumber}"
                ReadMode.BY_PAGE -> "${AppString.PAGE.getString()} ${state.page?.pageNumber}"
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
                        navigator.pop()
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
                                            text = "${AppString.SEARCH_CONTENT.getString()} ${state.query}",
                                        )

                                        TextButton(onClick = {
                                            screenModel.updateQuery("")
                                        }) {
                                            Text(AppString.CLEAR_SEARCH.getString())
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
                                            HeaderChapter(screenModel.getChapter(verse.chapterId))
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
                                                text = screenModel.getNextText(),
                                                onClick = {
                                                    scope.launch {
                                                        screenModel.goToNext()
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
                                    val subList = screenModel.splitVersesById(display.verses)
                                    subList.forEach {
                                        if (it.first().verseNumber == 1) {
                                            HeaderChapter(screenModel.getChapter(it.first().chapterId))
                                        }

                                        BasePageQuran(
                                            it,
                                            modifier = Modifier.padding(horizontal = 16.dp),
                                        )

                                        BaseSpacerVertical()
                                    }

                                    BaseButton(
                                        modifier = Modifier.padding(16.dp).fillMaxWidth(),
                                        text = screenModel.getNextText(),
                                        onClick = {
                                            scope.launch {
                                                screenModel.goToNext()
                                            }
                                        },
                                    )
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
                    screenModel.updateQuery(it)
                },
            )
        }

        if (openResetDialog.value) {
            BaseDialog(
                onDismissRequest = { openResetDialog.value = false },
                title = AppString.RESET_CONFIRMATION_TITLE.getString(),
                desc = AppString.RESET_VERSE_NOTE.getString(),
                negativeButtonText = AppString.CANCEL.getString(),
                positiveButtonText = AppString.RESET.getString(),
                onPositiveClicked = {
                    screenModel.resetVerses(id)
                    openResetDialog.value = false
                },
            )
        }

        if (openJumpVerseDialog.value) {
            BaseDialog(
                onDismissRequest = { openJumpVerseDialog.value = false },
                title = AppString.JUMP_TO_AYAH.getString(),
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
                        label = AppString.SEARCH_AYAH.getString(),
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
                                        screenModel.updateQuery("")
                                        scope.launch {
                                            delay(500)
                                            listState.scrollToItem(
                                                index =
                                                    screenModel.getIndex(
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
                val chapterNameSimple = screenModel.getChapter(verse.chapterId).nameSimple
                val isFavorite = screenModel.isFavorite(verse.id)

                val shareTitle =
                    AppString.SURAH_AYAH
                        .getString()
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
                            BaseItemCard(
                                title = getVerseActionLabel(verseAction, isFavorite),
                                titleColor = getVerseActionColor(verseAction, isFavorite),
                                iconResource = getVerseActionIcon(verseAction),
                                onClick = {
                                    when (verseAction) {
                                        VerseListScreenModel.VerseAction.SAVE_AS_LASTREAD -> {
                                            screenModel.updateLastRead(verse.id)
                                        }

                                        VerseListScreenModel.VerseAction.ADD_OR_REMOVE_FAVORITE -> {
                                            screenModel.addOrRemoveFavorite(verse)
                                        }

                                        VerseListScreenModel.VerseAction.COPY -> {
                                            clipboardManager.setText(AnnotatedString(shareMessage))
                                        }

                                        VerseListScreenModel.VerseAction.SHARE -> {
                                            shareContent.value = shareMessage
                                            openShare.value = true
                                        }

                                        VerseListScreenModel.VerseAction.REPORT -> {
                                            shareContent.value = shareMessage
                                            openMail.value = true
                                        }
                                    }

                                    openVerseDialog.value = false
                                    selectedVerse.value = null
                                },
                            )
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
            scope.launch {
                when (readMode) {
                    ReadMode.BY_CHAPTER -> {
                        screenModel.getVersesByChapter(id)
                        listState.scrollToItem(index = verseNumber - 1)
                    }

                    ReadMode.BY_JUZ -> {
                        screenModel.getVersesByJuz(id)
                        listState.scrollToItem(index = 0)
                    }

                    ReadMode.BY_PAGE -> {
                        screenModel.getVersesByPage(id)
                        listState.scrollToItem(index = 0)
                    }
                }
            }
        }
    }

    private fun getVerseActionLabel(
        verseAction: VerseListScreenModel.VerseAction,
        isFavorite: Boolean,
    ): String =
        when (verseAction) {
            VerseListScreenModel.VerseAction.SAVE_AS_LASTREAD -> AppString.SAVE_AS_LASTREAD.getString()
            VerseListScreenModel.VerseAction.ADD_OR_REMOVE_FAVORITE -> {
                if (isFavorite) {
                    AppString.REMOVE_FROM_FAVORITE.getString()
                } else {
                    AppString.ADD_TO_FAVORITE.getString()
                }
            }

            VerseListScreenModel.VerseAction.COPY -> AppString.COPY.getString()
            VerseListScreenModel.VerseAction.SHARE -> AppString.SHARE.getString()
            VerseListScreenModel.VerseAction.REPORT -> AppString.REPORT.getString()
        }

    @Composable
    private fun getVerseActionColor(
        verseAction: VerseListScreenModel.VerseAction,
        isFavorite: Boolean,
    ): Color =
        when (verseAction) {
            VerseListScreenModel.VerseAction.ADD_OR_REMOVE_FAVORITE -> {
                if (isFavorite) {
                    MaterialTheme.colorScheme.error
                } else {
                    Color.Unspecified
                }
            }

            VerseListScreenModel.VerseAction.SAVE_AS_LASTREAD,
            VerseListScreenModel.VerseAction.COPY,
            VerseListScreenModel.VerseAction.SHARE,
            VerseListScreenModel.VerseAction.REPORT,
            -> {
                Color.Unspecified
            }
        }

    private fun getVerseActionIcon(verseAction: VerseListScreenModel.VerseAction): DrawableResource =
        when (verseAction) {
            VerseListScreenModel.VerseAction.SAVE_AS_LASTREAD -> Res.drawable.bookmark
            VerseListScreenModel.VerseAction.ADD_OR_REMOVE_FAVORITE -> Res.drawable.heart
            VerseListScreenModel.VerseAction.COPY -> Res.drawable.copy
            VerseListScreenModel.VerseAction.SHARE -> Res.drawable.share
            VerseListScreenModel.VerseAction.REPORT -> Res.drawable.alert_circle
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
                        AppString.SURAH_DESC
                            .getString()
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
                        label = AppString.SEARCH_AYAH.getString(),
                        trailingClick = { valueSearch.value = "" },
                        supportingText = {
                            BaseText(
                                text = AppString.SEARCH_AYAH_HINT.getString(),
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
                        text = AppString.SEARCH.getString(),
                        onClick = { onSearchClicked(valueSearch.value) },
                    )
                }
            }
        }
    }
}
