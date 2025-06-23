package feature.home.screen

import AppConstant.DEFAULT_CHAPTER_ID
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import core.data.DataState
import feature.home.service.HomeRepository
import feature.home.service.model.HomeTemplate
import feature.other.service.AppRepository
import feature.other.service.model.AppSetting
import feature.quran.service.QuranRepository
import feature.quran.service.model.Chapter
import feature.quran.service.model.Juz
import feature.quran.service.model.LastRead
import feature.quran.service.model.Page
import feature.quran.service.model.QuranConstant.MAX_CHAPTER
import feature.quran.service.model.VerseFavorite
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn

class MainScreenModel(
    private val appRepository: AppRepository,
    private val homeRepository: HomeRepository,
    private val quranRepository: QuranRepository,
) : ViewModel() {
    val mutableState = MutableStateFlow(State())
    val state: StateFlow<State> = mutableState.asStateFlow()
    var shouldRefreshHome by mutableStateOf(true)
    var shouldRefreshQuranChapter by mutableStateOf(true)
    var shouldRefreshQuranJuz by mutableStateOf(true)
    var shouldRefreshQuranPage by mutableStateOf(true)
    var shouldRefreshQuranFavorite by mutableStateOf(true)

    data class State(
        val acceptPrivacyPolicy: Boolean = true, // for offline connection
        val location: AppSetting.Location? = null,
        val shouldShowSupport: Boolean = false,
        val mainPageState: MainPageState = MainPageState.HOME,
        val homeState: HomeState = HomeState.Loading,
        val lastRead: LastRead = LastRead(),
        val quranTabState: QuranTabState = QuranTabState.CHAPTER,
        val quranDownloadState: QuranDownloadState = QuranDownloadState.Done,
        val quranChapterState: QuranChapterState = QuranChapterState.Loading,
        val quranJuzState: QuranJuzState = QuranJuzState.Loading,
        val quranPageState: QuranPageState = QuranPageState.Loading,
        val verseFavorites: List<VerseFavorite> = listOf(),
        val verseDownloading: Int = 1,
    )

    enum class MainPageState {
        HOME,
        DHIKR,
        QURAN,
        PRAYER,
        ACTIVITY,
    }

    enum class QuranTabState {
        CHAPTER,
        JUZ,
        PAGE,
        FAVORITE,
    }

    sealed class HomeState {
        object Loading : HomeState()

        data class Error(
            val message: String,
        ) : HomeState()

        data class Content(
            val templates: List<HomeTemplate>,
        ) : HomeState()
    }

    sealed class QuranDownloadState {
        object Ready : QuranDownloadState()

        object Downloading : QuranDownloadState()

        object Done : QuranDownloadState()
    }

    sealed class QuranChapterState {
        object Loading : QuranChapterState()

        data class Error(
            val message: String,
        ) : QuranChapterState()

        data class Content(
            val chapters: List<Chapter>,
        ) : QuranChapterState()
    }

    sealed class QuranJuzState {
        object Loading : QuranJuzState()

        data class Error(
            val message: String,
        ) : QuranJuzState()

        data class Content(
            val juzs: List<Juz>,
        ) : QuranJuzState()
    }

    sealed class QuranPageState {
        object Loading : QuranPageState()

        data class Error(
            val message: String,
        ) : QuranPageState()

        data class Content(
            val pages: List<Page>,
        ) : QuranPageState()
    }

    fun updatePage(page: MainPageState) {
        viewModelScope.launch {
            mutableState.value = state.value.copy(mainPageState = page)
        }
    }

    fun updateQuranTab(tab: QuranTabState) {
        viewModelScope.launch {
            when (tab) {
                QuranTabState.CHAPTER -> {
                    getQuranChapters()
                }
                QuranTabState.JUZ -> {
                    getQuranJuzs()
                }
                QuranTabState.PAGE -> {
                    getQuranPages()
                }
                QuranTabState.FAVORITE -> {
                    getQuranFavorites()
                }
            }

            mutableState.value = state.value.copy(quranTabState = tab)
        }
    }

    fun onViewed() {
        viewModelScope.launch {
            // get first chapter
            if (!quranRepository.isChapterDownloaded(DEFAULT_CHAPTER_ID)) {
                quranRepository.fetchChapters().last()
                quranRepository.fetchVersesByChapter(DEFAULT_CHAPTER_ID).last()
            }

            val today = Clock.System.todayIn(TimeZone.currentSystemDefault())

            mutableState.value =
                state.value.copy(
                    acceptPrivacyPolicy = appRepository.getSetting().acceptPrivacyPolicy,
                    location = appRepository.getSetting().location,
                    shouldShowSupport = today.dayOfWeek == DayOfWeek.FRIDAY,
                )
        }
    }

    fun acceptPrivacyPolicy() {
        viewModelScope.launch {
            appRepository.updateAcceptPrivacyPolicy()

            mutableState.value =
                state.value.copy(
                    acceptPrivacyPolicy = appRepository.getSetting().acceptPrivacyPolicy,
                )
        }
    }

    fun getHome() {
        viewModelScope.launch {
            homeRepository.fetchHomeTemplates().collectLatest {
                val homeState =
                    when (it) {
                        is DataState.Error -> HomeState.Error(it.message)
                        DataState.Loading -> HomeState.Loading
                        is DataState.Success -> HomeState.Content(templates = it.data)
                    }

                mutableState.value = state.value.copy(homeState = homeState)
            }
        }
    }

    fun getQuran2() {
        viewModelScope.launch {
            val lastRead = quranRepository.getLastRead()
            val downloadState =
                if (quranRepository.checkIsAllDownloaded()) {
                    QuranDownloadState.Done
                } else {
                    QuranDownloadState.Ready
                }

            mutableState.value =
                state.value.copy(
                    lastRead = lastRead,
                    quranDownloadState = downloadState,
                )
        }
    }

    fun getQuranChapters() {
        viewModelScope.launch {
            quranRepository.fetchChapters().collectLatest {
                val chapterState =
                    when (it) {
                        is DataState.Error -> QuranChapterState.Error(it.message)
                        DataState.Loading -> QuranChapterState.Loading
                        is DataState.Success -> QuranChapterState.Content(chapters = it.data)
                    }

                mutableState.value =
                    state.value.copy(
                        quranChapterState = chapterState,
                    )
            }
        }
    }

    fun getQuranJuzs() {
        viewModelScope.launch {
            quranRepository.fetchJuzs().collectLatest {
                val juzState =
                    when (it) {
                        is DataState.Error -> QuranJuzState.Error(it.message)
                        DataState.Loading -> QuranJuzState.Loading
                        is DataState.Success -> QuranJuzState.Content(juzs = it.data)
                    }

                mutableState.value =
                    state.value.copy(
                        quranJuzState = juzState,
                    )
            }
        }
    }

    fun getQuranPages() {
        viewModelScope.launch {
            val pageState =
                QuranPageState.Content(
                    pages = quranRepository.fetchPages().last(),
                )

            mutableState.value =
                state.value.copy(
                    quranPageState = pageState,
                )
        }
    }

    fun getQuranFavorites() {
        viewModelScope.launch {
            mutableState.value =
                state.value.copy(
                    verseFavorites = quranRepository.getVerseFavorites(),
                )
        }
    }

    fun addOrRemoveFavorite(verse: VerseFavorite) {
        viewModelScope.launch {
            quranRepository.addOrRemoveVerseFavorites(verse)
            delay(300)
            getQuranFavorites()
        }
    }

    fun downloadAllVerses() {
        viewModelScope.launch {
            mutableState.value =
                state.value.copy(quranDownloadState = QuranDownloadState.Downloading)

            for (i in 1..MAX_CHAPTER) {
                quranRepository.downloadVerses(i).last()
                mutableState.value =
                    state.value.copy(verseDownloading = i)
            }

            getQuran2()
            getQuranChapters()
            getQuranJuzs()
            getQuranPages()
            getQuranFavorites()
        }
    }
}
