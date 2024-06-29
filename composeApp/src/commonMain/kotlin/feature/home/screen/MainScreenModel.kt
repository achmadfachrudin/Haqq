package feature.home.screen

import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import core.data.DataState
import feature.dhikr.service.model.DuaCategory
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
) : StateScreenModel<MainScreenModel.State>(State()) {
    data class State(
        val acceptPrivacyPolicy: Boolean = true, // for offline connection
        val location: AppSetting.Location? = null,
        val pageState: PageState = PageState.HOME,
        val mainState: MainState = MainState.Loading,
        val quranState: QuranState = QuranState(),
        val shouldShowSupport: Boolean = false,
    )

    enum class PageState {
        HOME,
        DHIKR,
        QURAN,
        PRAYER,
        ACTIVITY,
    }

    sealed class MainState {
        object Loading : MainState()

        data class Error(
            val message: String,
        ) : MainState()

        data class Content(
            val templates: List<HomeTemplate>,
        ) : MainState()
    }

    data class QuranState(
        val lastRead: LastRead? = null,
        val downloadState: QuranDownloadState = QuranDownloadState.Done,
        val chapterState: QuranChapterState = QuranChapterState.Loading,
        val juzState: QuranJuzState = QuranJuzState.Loading,
        val pageState: QuranPageState = QuranPageState.Loading,
        val favorites: List<VerseFavorite> = listOf(),
        val verseDownloading: Int = 1,
    )

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
        screenModelScope.launch {
            mutableState.value = state.value.copy(pageState = page)
        }
    }

    fun onViewed() {
        screenModelScope.launch {
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
        screenModelScope.launch {
            appRepository.updateAcceptPrivacyPolicy()
            mutableState.value =
                state.value.copy(
                    acceptPrivacyPolicy = appRepository.getSetting().acceptPrivacyPolicy,
                )
        }
    }

    fun getMain() {
        screenModelScope.launch {
            homeRepository.fetchHomeTemplates().collectLatest {
                val mainState =
                    when (it) {
                        is DataState.Error -> MainState.Error(it.message)
                        DataState.Loading -> MainState.Loading
                        is DataState.Success -> MainState.Content(templates = it.data)
                    }

                mutableState.value = state.value.copy(mainState = mainState)
            }
        }
    }

    fun getQuran() {
        screenModelScope.launch {
            quranRepository.fetchChapters().collectLatest {
                var downloadState: QuranDownloadState = QuranDownloadState.Done
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
                        quranState =
                            QuranState(
                                lastRead = quranRepository.getLastRead(),
                                downloadState = downloadState,
                                chapterState = chapterState,
                                juzState = juzState,
                                pageState = pageState,
                                favorites = quranRepository.getVerseFavorites(),
                            ),
                    )
            }
        }
    }

    fun getQuranicDuaCategory(): DuaCategory {
        val title =
            when (appRepository.getSetting().language) {
                AppSetting.Language.ENGLISH -> "Quran"
                AppSetting.Language.INDONESIAN -> "al-Quran"
            }
        return DuaCategory("quran", title)
    }

    fun addOrRemoveFavorite(verse: VerseFavorite) {
        screenModelScope.launch {
            quranRepository.addOrRemoveVerseFavorites(verse)

            mutableState.value =
                state.value.copy(
                    quranState =
                        state.value.quranState.copy(favorites = quranRepository.getVerseFavorites()),
                )
        }
    }

    fun downloadAllVerses() {
        screenModelScope.launch {
            mutableState.value =
                state.value.copy(
                    quranState = state.value.quranState.copy(downloadState = QuranDownloadState.Downloading),
                )

            for (i in 1..MAX_CHAPTER) {
                quranRepository.downloadVerses(i).collect()
                mutableState.value =
                    state.value.copy(
                        quranState = state.value.quranState.copy(verseDownloading = i),
                    )
            }

            getQuran()
        }
    }
}
