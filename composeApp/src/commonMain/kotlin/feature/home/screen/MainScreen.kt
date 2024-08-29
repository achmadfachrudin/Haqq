package feature.home.screen

import AppConstant
import KottieAnimation
import OpenPage
import PlatformPage
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.multiplatform.webview.web.LoadingState
import com.multiplatform.webview.web.WebView
import com.multiplatform.webview.web.rememberWebViewState
import core.ui.component.BaseButton
import core.ui.component.BaseTopAppBar
import core.ui.component.TabNavigationItem
import feature.article.screen.ArticleListScreen
import feature.charity.screen.CharityListScreen
import feature.charity.screen.openSupport
import feature.conversation.screen.ConversationScreen
import feature.dhikr.screen.AsmaulHusnaScreen
import feature.dhikr.screen.DhikrListScreen
import feature.dhikr.screen.DuaListScreen
import feature.dhikr.screen.DuaSunnahScreen
import feature.other.screen.OtherScreen
import feature.other.screen.SettingScreen
import feature.other.service.AppRepository
import feature.other.service.mapper.getString
import feature.other.service.model.AppString
import feature.prayertime.screen.CalendarScreen
import feature.prayertime.screen.GuidanceScreen
import feature.prayertime.screen.PrayerTimeScreen
import feature.quran.screen.VerseListScreen
import feature.quran.service.model.ReadMode
import feature.study.screen.ChannelListScreen
import feature.study.screen.NoteListScreen
import feature.web.screen.WebScreen
import feature.web.screen.YoutubeScreen
import feature.zakat.screen.ZakatScreen
import getPlatform
import haqq.composeapp.generated.resources.Res
import haqq.composeapp.generated.resources.more_horizontal
import haqq.composeapp.generated.resources.settings
import kottieComposition.KottieCompositionSpec
import kottieComposition.animateKottieCompositionAsState
import kottieComposition.rememberKottieComposition
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import org.koin.mp.KoinPlatform
import utils.KottieConstants

class MainScreen : Screen {
    @OptIn(ExperimentalResourceApi::class)
    @Composable
    override fun Content() {
        val screenModel = koinScreenModel<MainScreenModel>()
        val state by screenModel.state.collectAsState()
        val navigator = LocalNavigator.currentOrThrow

        var animation by remember { mutableStateOf("") }
        val composition =
            rememberKottieComposition(
                spec = KottieCompositionSpec.File(animation),
            )
        val animationState by animateKottieCompositionAsState(
            composition = composition,
            iterations = KottieConstants.IterateForever,
        )

        val openActivity = remember { mutableStateOf(false) }
        val platformPage = remember { mutableStateOf(PlatformPage.NONE) }

        if (!state.acceptPrivacyPolicy) {
            DialogPrivacyPolicy()
        } else {
            Scaffold(
                topBar = {
                    BaseTopAppBar(
                        title = AppString.APP_NAME.getString(),
                        showLeftButton = true,
                        showRightButton = true,
                        showOptionalLottie = true,
                        leftButtonImage = painterResource(Res.drawable.settings),
                        rightButtonImage = painterResource(Res.drawable.more_horizontal),
                        optionalLottie = {
                            KottieAnimation(
                                modifier = Modifier.size(32.dp),
                                composition = composition,
                                progress = { animationState.progress },
                            )
                        },
                        onLeftButtonClick = { navigator.push(SettingScreen()) },
                        onOptionalButtonClick = {
                            openSupport(navigator)
                        },
                        onRightButtonClick = { navigator.push(OtherScreen()) },
                    )
                },
                content = {
                    Column(modifier = Modifier.padding(it).fillMaxSize()) {
                        when (state.pageState) {
                            MainScreenModel.PageState.HOME -> {
                                MainPageHome(
                                    state = state.mainState,
                                    onPrayerTimeClick = {
                                        navigator.push(PrayerTimeScreen())
                                    },
                                    onDhikrClick = { dhikrType ->
                                        navigator.push(DhikrListScreen(dhikrType))
                                    },
                                    onWebClick = { url ->
                                        navigator.push(WebScreen(url))
                                    },
                                    onVerseClick = { chapterNumber: Int, verseNumber: Int ->
                                        navigator.push(
                                            VerseListScreen(
                                                readMode = ReadMode.BY_CHAPTER,
                                                id = chapterNumber,
                                                verseNumber = verseNumber,
                                            ),
                                        )
                                    },
                                    onVideoClick = { videoId ->
                                        navigator.push(YoutubeScreen(videoId))
                                    },
                                )
                            }

                            MainScreenModel.PageState.DHIKR -> {
                                MainPageDhikr(
                                    onDzikirClick = { dhikrType ->
                                        navigator.push(DhikrListScreen(dhikrType))
                                    },
                                    onDuaQuranClick = {
                                        screenModel.getQuranicDuaCategory().let { category ->
                                            navigator.push(
                                                DuaListScreen(
                                                    category.tag,
                                                    category.title,
                                                ),
                                            )
                                        }
                                    },
                                    onDuaSunnahClick = { navigator.push(DuaSunnahScreen()) },
                                    onAsmaClick = { navigator.push(AsmaulHusnaScreen()) },
                                )
                            }

                            MainScreenModel.PageState.QURAN -> {
                                MainPageQuran(
                                    state = state.quranState,
                                    onRetryClick = { screenModel.getQuran() },
                                    onLastReadClick = { verse ->
                                        navigator.push(
                                            VerseListScreen(
                                                readMode = ReadMode.BY_CHAPTER,
                                                id = verse.chapterId,
                                                verseNumber = verse.verseNumber,
                                            ),
                                        )
                                    },
                                    onDownloadClick = {
                                        screenModel.downloadAllVerses()
                                    },
                                    onChapterClick = { chapter ->
                                        navigator.push(
                                            VerseListScreen(
                                                readMode = ReadMode.BY_CHAPTER,
                                                id = chapter.id,
                                            ),
                                        )
                                    },
                                    onJuzClick = { juz ->
                                        navigator.push(
                                            VerseListScreen(
                                                readMode = ReadMode.BY_JUZ,
                                                id = juz.id,
                                            ),
                                        )
                                    },
                                    onPageClick = { page ->
                                        navigator.push(
                                            VerseListScreen(
                                                readMode = ReadMode.BY_PAGE,
                                                id = page.id,
                                            ),
                                        )
                                    },
                                    onFavoriteClick = { verse ->
                                        navigator.push(
                                            VerseListScreen(
                                                readMode = ReadMode.BY_CHAPTER,
                                                id = verse.chapterId,
                                                verseNumber = verse.verseNumber,
                                            ),
                                        )
                                    },
                                    onRemoveFavoriteClick = { verse ->
                                        screenModel.addOrRemoveFavorite(verse)
                                    },
                                )
                            }

                            MainScreenModel.PageState.PRAYER -> {
                                MainPagePrayer(
                                    onOpenActivity = { name ->
                                        platformPage.value = name
                                        openActivity.value = true
                                    },
                                    onVideoClick = { url ->
                                        navigator.push(YoutubeScreen(url))
                                    },
                                    onTimeClick = { navigator.push(PrayerTimeScreen()) },
                                    onCalendarClick = { navigator.push(CalendarScreen()) },
                                    onCalculateZakatClick = { navigator.push(ZakatScreen()) },
                                    onGuideClick = { guidanceType ->
                                        navigator.push(GuidanceScreen(guidanceType))
                                    },
                                )
                            }

                            MainScreenModel.PageState.ACTIVITY -> {
                                MainPageActivity(
                                    onArticleClick = { navigator.push(ArticleListScreen()) },
                                    onStudyVideoClick = { navigator.push(ChannelListScreen()) },
                                    onStudyNoteClick = { navigator.push(NoteListScreen()) },
                                    onConversationClick = { navigator.push(ConversationScreen()) },
                                    onCharityClick = { navigator.push(CharityListScreen()) },
                                    onContentClick = { },
                                )
                            }
                        }
                    }
                },
                bottomBar = {
                    NavigationBar {
                        MainScreenModel.PageState.entries.forEach {
                            TabNavigationItem(
                                pageState = it,
                                pageSelected = state.pageState,
                                onClick = { screenModel.updatePage(it) },
                            )
                        }
                    }
                },
            )
        }

        if (openActivity.value && platformPage.value != PlatformPage.NONE) {
            OpenPage(platformPage.value)
            openActivity.value = false
        }

        LaunchedEffect(Unit) {
            animation = Res.readBytes("files/love.json").decodeToString()

            screenModel.onViewed()
        }
    }

    @Composable
    private fun DialogPrivacyPolicy() {
        val screenModel = koinScreenModel<MainScreenModel>()
        val appRepository = KoinPlatform.getKoin().get<AppRepository>()

        Scaffold(
            topBar = {
                BaseTopAppBar(
                    title = AppString.PRIVACY_POLICY.getString(),
                    showLeftButton = false,
                    showRightButton = false,
                )
            },
        ) { paddingValues ->
            Box(modifier = Modifier.padding(paddingValues).fillMaxSize()) {
                val url = AppConstant.getPrivacyPoilicyUrl(appRepository.getSetting().language.id)

                val state = rememberWebViewState(url)

                state.webSettings.apply {
                    isJavaScriptEnabled = true
                    customUserAgentString = getPlatform().name
                    androidWebSettings.domStorageEnabled = true
                    androidWebSettings.safeBrowsingEnabled = true
                }

                when (val loadingState = state.loadingState) {
                    LoadingState.Finished -> {
                    }

                    LoadingState.Initializing -> {
                        LinearProgressIndicator(
                            progress = { 20f },
                            modifier = Modifier.fillMaxWidth(),
                        )
                    }

                    is LoadingState.Loading -> {
                        LinearProgressIndicator(
                            progress = { loadingState.progress },
                            modifier = Modifier.fillMaxWidth(),
                        )
                    }
                }

                WebView(
                    modifier = Modifier.padding(bottom = 16.dp).fillMaxSize(),
                    state = state,
                )

                BaseButton(
                    modifier = Modifier.padding(16.dp).fillMaxWidth().align(Alignment.BottomCenter),
                    text = AppString.ACCEPT.getString(),
                    onClick = { screenModel.acceptPrivacyPolicy() },
                )
            }
        }
    }
}
