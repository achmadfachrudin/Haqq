package feature.home.screen

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
import kotlinx.coroutines.flow.collect
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
    var shouldRefresh by mutableStateOf(true)

    data class State(
        val acceptPrivacyPolicy: Boolean = true, // for offline connection
        val location: AppSetting.Location? = null,
        val shouldShowSupport: Boolean = false,
        val pageState: PageState = PageState.HOME,
        val homeState: HomeState = HomeState.Loading,
        val lastRead: LastRead = LastRead(),
        val quranDownloadState: QuranDownloadState = QuranDownloadState.Done,
        val quranChapterState: QuranChapterState = QuranChapterState.Loading,
        val quranJuzState: QuranJuzState = QuranJuzState.Loading,
        val quranPageState: QuranPageState = QuranPageState.Loading,
        val verseFavorites: List<VerseFavorite> = listOf(),
        val verseDownloading: Int = 1,
    )

    enum class PageState {
        HOME,
        DHIKR,
        QURAN,
        PRAYER,
        ACTIVITY,
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

    fun updatePage(page: PageState) {
        viewModelScope.launch {
            mutableState.value = state.value.copy(pageState = page)
        }
    }

    fun onViewed() {
        viewModelScope.launch {
            val today = Clock.System.todayIn(TimeZone.currentSystemDefault())

            mutableState.value =
                state.value.copy(
                    acceptPrivacyPolicy = appRepository.getSetting().acceptPrivacyPolicy,
                    location = appRepository.getSetting().location,
                    shouldShowSupport = today.dayOfWeek == DayOfWeek.FRIDAY,
                )

            getQuran()
            getMain()
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

    fun getMain() {
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

    fun getQuran() {
        viewModelScope.launch {
            quranRepository.fetchChapters().collectLatest {
                var downloadState: QuranDownloadState = QuranDownloadState.Ready
                var juzState: QuranJuzState = QuranJuzState.Loading
                var pageState: QuranPageState = QuranPageState.Loading

                val chapterState =
                    when (it) {
                        is DataState.Error -> QuranChapterState.Error(it.message)
                        DataState.Loading -> QuranChapterState.Loading
                        is DataState.Success -> QuranChapterState.Content(chapters = it.data)
                    }

                if (chapterState is QuranChapterState.Content) {
                    downloadState =
                        if (quranRepository.checkIsAllDownloaded()) {
                            quranRepository.addPages().last()

                            QuranDownloadState.Done
                        } else {
                            QuranDownloadState.Ready
                        }

                    juzState =
                        when (val result = quranRepository.fetchJuzs().last()) {
                            is DataState.Error -> QuranJuzState.Error(result.message)
                            DataState.Loading -> QuranJuzState.Loading
                            is DataState.Success -> QuranJuzState.Content(juzs = result.data)
                        }

                    pageState =
                        QuranPageState.Content(
                            pages = quranRepository.fetchPages().last(),
                        )
                }

                mutableState.value =
                    state.value.copy(
                        lastRead = quranRepository.getLastRead(),
                        quranDownloadState = downloadState,
                        quranChapterState = chapterState,
                        quranJuzState = juzState,
                        quranPageState = pageState,
                        verseFavorites = quranRepository.getVerseFavorites(),
                    )
            }
        }
    }

    fun getLastRead() {
        viewModelScope.launch {
            val lastRead = quranRepository.getLastRead()

            mutableState.value = state.value.copy(lastRead = lastRead)
        }
    }

    fun getVerseFavorites() {
        viewModelScope.launch {
            val verseFavorites = quranRepository.getVerseFavorites()

            mutableState.value = state.value.copy(verseFavorites = verseFavorites)
        }
    }

    fun addOrRemoveFavorite(verse: VerseFavorite) {
        viewModelScope.launch {
            quranRepository.addOrRemoveVerseFavorites(verse)
            delay(300)
            getVerseFavorites()
        }
    }

    fun downloadAllVerses() {
        viewModelScope.launch {
            mutableState.value =
                state.value.copy(quranDownloadState = QuranDownloadState.Downloading)

            for (i in 1..MAX_CHAPTER) {
                quranRepository.downloadVerses(i).collect()
                mutableState.value =
                    state.value.copy(verseDownloading = i)
            }

            getQuran()
        }
    }
}
