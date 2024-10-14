package feature.quran.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import core.data.DataState
import core.util.orZero
import feature.other.service.mapper.getString
import feature.other.service.model.AppString
import feature.quran.service.QuranRepository
import feature.quran.service.model.Chapter
import feature.quran.service.model.Juz
import feature.quran.service.model.Page
import feature.quran.service.model.QuranConstant.MAX_CHAPTER
import feature.quran.service.model.QuranConstant.MAX_JUZ
import feature.quran.service.model.QuranConstant.MAX_PAGE
import feature.quran.service.model.ReadMode
import feature.quran.service.model.Verse
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class VerseListScreenModel(
    private val repository: QuranRepository,
) : ViewModel() {
    val mutableState = MutableStateFlow(State())
    val state: StateFlow<State> = mutableState.asStateFlow()

    data class State(
        val title: String = "",
        val chapter: Chapter? = null,
        val nextChapter: Chapter? = null,
        val chapters: List<Chapter> = listOf(),
        val chapterActive: Chapter? = null,
        val juz: Juz? = null,
        val nextJuz: Juz? = null,
        val page: Page? = null,
        val nextPage: Page? = null,
        val query: String = "",
        val verseState: VerseState = VerseState.Loading,
        val readMode: ReadMode = ReadMode.BY_CHAPTER,
    )

    sealed class VerseState {
        object Loading : VerseState()

        data class Error(
            val message: String,
        ) : VerseState()

        data class Content(
            val verses: List<Verse>,
        ) : VerseState()
    }

    enum class VerseAction {
        SAVE_AS_LASTREAD,
        ADD_OR_REMOVE_FAVORITE,
        SHARE,
        COPY,
        REPORT,
    }

    fun getVersesByChapter(chapterId: Int) {
        viewModelScope.launch {
            repository.fetchVersesByChapter(chapterId).collectLatest {
                val verseState =
                    when (it) {
                        is DataState.Error -> VerseState.Error(it.message)
                        DataState.Loading -> VerseState.Loading
                        is DataState.Success -> VerseState.Content(verses = it.data)
                    }

                mutableState.value =
                    state.value.copy(
                        readMode = ReadMode.BY_CHAPTER,
                        chapter = repository.getChapterById(chapterId),
                        nextChapter =
                            if ((chapterId + 1) <= MAX_CHAPTER) {
                                repository.getChapterById(
                                    chapterId + 1,
                                )
                            } else {
                                null
                            },
                        verseState = verseState,
                    )
            }
        }
    }

    fun getVersesByJuz(juzId: Int) {
        viewModelScope.launch {
            repository.fetchVersesByJuz(juzId).collectLatest {
                val verseState =
                    when (it) {
                        is DataState.Error -> VerseState.Error(it.message)
                        DataState.Loading -> VerseState.Loading
                        is DataState.Success -> VerseState.Content(verses = it.data)
                    }

                mutableState.value =
                    state.value.copy(
                        readMode = ReadMode.BY_JUZ,
                        juz = repository.getJuzById(juzId),
                        nextJuz = if ((juzId + 1) <= MAX_JUZ) repository.getJuzById(juzId + 1) else null,
                        verseState = verseState,
                    )
            }
        }
    }

    fun getVersesByPage(pageId: Int) {
        viewModelScope.launch {
            repository.fetchVersesByPage(pageId).collectLatest {
                val verseState =
                    when (it) {
                        is DataState.Error -> VerseState.Error(it.message)
                        DataState.Loading -> VerseState.Loading
                        is DataState.Success -> VerseState.Content(verses = it.data)
                    }

                mutableState.value =
                    state.value.copy(
                        readMode = ReadMode.BY_PAGE,
                        page = repository.getPageById(pageId),
                        nextPage = if ((pageId + 1) <= MAX_PAGE) repository.getPageById(pageId + 1) else null,
                        verseState = verseState,
                    )
            }
        }
    }

    fun resetVerses(id: Int) {
        viewModelScope.launch {
            when (state.value.readMode) {
                ReadMode.BY_CHAPTER -> {
                    repository.resetVersesByChapter(id)
                    delay(300)
                    getVersesByChapter(id)
                }

                ReadMode.BY_JUZ -> {
                    repository.resetVersesByJuz(id)
                    delay(300)
                    getVersesByJuz(id)
                }

                ReadMode.BY_PAGE -> {
                    repository.resetVersesByPage(id)
                    delay(300)
                    getVersesByPage(id)
                }
            }
        }
    }

    fun updateLastRead(verseId: Int) {
        viewModelScope.launch {
            repository.updateLastRead(verseId)
        }
    }

    fun addOrRemoveFavorite(verse: Verse) {
        viewModelScope.launch {
            repository.addOrRemoveVerseFavorites(verse)
        }
    }

    fun updateQuery(newQuery: String) {
        viewModelScope.launch {
            mutableState.value =
                state.value.copy(
                    query = newQuery,
                )
        }
    }

    fun splitVersesById(verses: List<Verse>): List<List<Verse>> {
        val subList = mutableListOf<List<Verse>>()
        val currentList = mutableListOf<Verse>()
        for (verse in verses) {
            if (verse.verseNumber == 1) {
                if (currentList.isNotEmpty()) {
                    subList.add(currentList.toList()) // Create an immutable copy for safety
                    currentList.clear()
                }
            }
            currentList.add(verse)
        }
        if (currentList.isNotEmpty()) {
            subList.add(currentList.toList())
        }
        return subList
    }

    fun getChapter(chapterNumber: Int): Chapter = repository.getChapterById(chapterNumber)

    fun goToNext() =
        when (state.value.readMode) {
            ReadMode.BY_CHAPTER -> {
                getVersesByChapter(
                    state.value.nextChapter
                        ?.id
                        .orZero(),
                )
            }

            ReadMode.BY_JUZ -> {
                getVersesByJuz(
                    state.value.nextJuz
                        ?.id
                        .orZero(),
                )
            }

            ReadMode.BY_PAGE -> {
                getVersesByPage(
                    state.value.nextPage
                        ?.id
                        .orZero(),
                )
            }
        }

    fun getNextText(): String {
        val state = state.value
        val nextText =
            when (state.readMode) {
                ReadMode.BY_CHAPTER -> state.nextChapter?.nameSimple.orEmpty()
                ReadMode.BY_JUZ -> "Juz ${state.nextJuz?.juzNumber}"
                ReadMode.BY_PAGE ->
                    "${AppString.PAGE.getString()} ${state.nextPage?.pageNumber}"
            }

        return AppString.CONTINUE_READ.getString().replace("%1", nextText)
    }

    fun getIndex(verse: Verse): Int =
        when (val currentState = state.value.verseState) {
            is VerseState.Content -> currentState.verses.indexOf(verse)
            is VerseState.Error,
            VerseState.Loading,
            -> 0
        }

    fun isFavorite(verseId: Int): Boolean = repository.isFavorite(verseId)
}
