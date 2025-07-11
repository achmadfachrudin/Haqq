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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.multiplatform.webview.web.LoadingState
import com.multiplatform.webview.web.WebView
import com.multiplatform.webview.web.rememberWebViewState
import core.ui.component.BaseButton
import core.ui.component.BaseTopAppBar
import core.ui.component.TabNavigationItem
import feature.article.screen.ArticleListNav
import feature.charity.screen.CharityListNav
import feature.conversation.screen.ConversationNav
import feature.dhikr.screen.AsmaulHusnaNav
import feature.dhikr.screen.DhikrPagerNav
import feature.dhikr.screen.DuaListNav
import feature.dhikr.screen.DuaSunnahNav
import feature.other.screen.OtherNav
import feature.other.screen.SettingNav
import feature.other.service.AppRepository
import feature.prayertime.screen.CalendarNav
import feature.prayertime.screen.GuidanceNav
import feature.prayertime.screen.PrayerTimeDailyNav
import feature.quran.screen.VerseListNav
import feature.quran.service.model.ReadMode
import feature.study.screen.ChannelListNav
import feature.study.screen.NoteListNav
import feature.web.screen.WebNav
import feature.web.screen.YoutubeNav
import feature.zakat.screen.ZakatNav
import getPlatform
import haqq.composeapp.generated.resources.Res
import haqq.composeapp.generated.resources.accept
import haqq.composeapp.generated.resources.app_name
import haqq.composeapp.generated.resources.dua_quran
import haqq.composeapp.generated.resources.kids_activity_title
import haqq.composeapp.generated.resources.more_horizontal
import haqq.composeapp.generated.resources.privacy_policy
import haqq.composeapp.generated.resources.settings
import haqq.composeapp.generated.resources.support_title
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import kottieComposition.KottieCompositionSpec
import kottieComposition.animateKottieCompositionAsState
import kottieComposition.rememberKottieComposition
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.mp.KoinPlatform
import utils.KottieConstants

@Serializable
object MainNav

@OptIn(ExperimentalResourceApi::class)
@Composable
fun MainScreen(
    onArticleListClick: (ArticleListNav) -> Unit,
    onAsmaulHusnaClick: (AsmaulHusnaNav) -> Unit,
    onDhikrListClick: (DhikrPagerNav) -> Unit,
    onDuaListClick: (DuaListNav) -> Unit,
    onDuaSunnahClick: (DuaSunnahNav) -> Unit,
    onCalendarClick: (CalendarNav) -> Unit,
    onGuidanceClick: (GuidanceNav) -> Unit,
    onPrayerTimeDailyClick: (PrayerTimeDailyNav) -> Unit,
    onChannelListClick: (ChannelListNav) -> Unit,
    onNoteListClick: (NoteListNav) -> Unit,
    onCharityListClick: (CharityListNav) -> Unit,
    onConversationClick: (ConversationNav) -> Unit,
    onVerseListClick: (VerseListNav) -> Unit,
    onOtherClick: (OtherNav) -> Unit,
    onSettingClick: (SettingNav) -> Unit,
    onWebClick: (WebNav) -> Unit,
    onYoutubeClick: (YoutubeNav) -> Unit,
    onZakatClick: (ZakatNav) -> Unit,
) {
    val vm = koinViewModel<MainScreenModel>()
    val state by vm.state.collectAsState()
    val appRepository = KoinPlatform.getKoin().get<AppRepository>()
    val languageId = appRepository.getSetting().language.id
    val scope = rememberCoroutineScope()

    var animation by remember { mutableStateOf("") }
    scope.launch {
        animation = Res.readBytes("files/love.json").decodeToString()
    }
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
                val supportTitle = stringResource(Res.string.support_title)

                BaseTopAppBar(
                    title = stringResource(Res.string.app_name),
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
                    onLeftButtonClick = {
                        onSettingClick(SettingNav)
                    },
                    onOptionalButtonClick = {
                        onWebClick(
                            WebNav(
                                url = AppConstant.getSupportUrl(languageId),
                                title = supportTitle,
                                openExternalIOS = true,
                            ),
                        )
                    },
                    onRightButtonClick = {
                        onOtherClick(OtherNav)
                    },
                )
            },
            content = {
                Column(modifier = Modifier.padding(it).fillMaxSize()) {
                    when (state.mainPageState) {
                        MainScreenModel.MainPageState.HOME -> {
                            MainPageHome(
                                vm = vm,
                                state = state.homeState,
                                onPrayerTimeClick = {
                                    onPrayerTimeDailyClick(PrayerTimeDailyNav)
                                },
                                onDhikrClick = { dhikrType ->
                                    onDhikrListClick(DhikrPagerNav(dhikrTypeName = dhikrType.name))
                                },
                                onWebClick = { url ->
                                    onWebClick(WebNav(url = url))
                                },
                                onVerseClick = { chapterNumber: Int, verseNumber: Int ->
                                    onVerseListClick(
                                        VerseListNav(
                                            readModeName = ReadMode.BY_CHAPTER.name,
                                            id = chapterNumber,
                                            verseNumber = verseNumber,
                                        ),
                                    )
                                },
                                onVideoClick = { videoId ->
                                    onYoutubeClick(
                                        YoutubeNav(
                                            videoId = videoId,
                                        ),
                                    )
                                },
                            )
                        }

                        MainScreenModel.MainPageState.DHIKR -> {
                            val duaTitle = stringResource(Res.string.dua_quran)
                            MainPageDhikr(
                                onDzikirClick = { dhikrType ->
                                    onDhikrListClick(DhikrPagerNav(dhikrTypeName = dhikrType.name))
                                },
                                onDuaQuranClick = {
                                    onDuaListClick(
                                        DuaListNav(
                                            duaCategoryTitle = duaTitle,
                                            duaCategoryTag = "quran",
                                        ),
                                    )
                                },
                                onDuaSunnahClick = {
                                    onDuaSunnahClick(DuaSunnahNav)
                                },
                                onAsmaClick = {
                                    onAsmaulHusnaClick(AsmaulHusnaNav)
                                },
                            )
                        }

                        MainScreenModel.MainPageState.QURAN -> {
                            MainPageQuran(
                                vm = vm,
                                state = state,
                                onRetryClick = { vm.getQuran2() },
                                onLastReadClick = { verse ->
                                    onVerseListClick(
                                        VerseListNav(
                                            readModeName = ReadMode.BY_CHAPTER.name,
                                            id = verse.chapterId,
                                            verseNumber = verse.verseNumber,
                                        ),
                                    )
                                },
                                onDownloadClick = {
                                    vm.downloadAllVerses()
                                },
                                onChapterClick = { chapter ->
                                    onVerseListClick(
                                        VerseListNav(
                                            readModeName = ReadMode.BY_CHAPTER.name,
                                            id = chapter.id,
                                        ),
                                    )
                                },
                                onJuzClick = { juz ->
                                    onVerseListClick(
                                        VerseListNav(
                                            readModeName = ReadMode.BY_JUZ.name,
                                            id = juz.id,
                                        ),
                                    )
                                },
                                onPageClick = { page ->
                                    onVerseListClick(
                                        VerseListNav(
                                            readModeName = ReadMode.BY_PAGE.name,
                                            id = page.id,
                                        ),
                                    )
                                },
                                onFavoriteClick = { verse ->
                                    onVerseListClick(
                                        VerseListNav(
                                            readModeName = ReadMode.BY_CHAPTER.name,
                                            id = verse.chapterId,
                                            verseNumber = verse.verseNumber,
                                        ),
                                    )
                                },
                                onRemoveFavoriteClick = { verse ->
                                    vm.addOrRemoveFavorite(verse)
                                },
                            )
                        }

                        MainScreenModel.MainPageState.PRAYER -> {
                            MainPagePrayer(
                                onOpenActivity = { name ->
                                    platformPage.value = name
                                    openActivity.value = true
                                },
                                onVideoClick = { videoId ->
                                    onYoutubeClick(
                                        YoutubeNav(
                                            videoId = videoId,
                                        ),
                                    )
                                },
                                onTimeClick = {
                                    onPrayerTimeDailyClick(PrayerTimeDailyNav)
                                },
                                onCalendarClick = {
                                    onCalendarClick(CalendarNav)
                                },
                                onCalculateZakatClick = {
                                    onZakatClick(ZakatNav)
                                },
                                onGuideClick = { guidanceType ->
                                    onGuidanceClick(GuidanceNav(guidanceTypeName = guidanceType.name))
                                },
                            )
                        }

                        MainScreenModel.MainPageState.ACTIVITY -> {
                            val kidsTitle = stringResource(Res.string.kids_activity_title)
                            MainPageActivity(
                                onArticleClick = {
                                    onArticleListClick(ArticleListNav)
                                },
                                onStudyVideoClick = {
                                    onChannelListClick(ChannelListNav)
                                },
                                onStudyNoteClick = {
                                    onNoteListClick(NoteListNav)
                                },
                                onConversationClick = {
                                    onConversationClick(ConversationNav)
                                },
                                onCharityClick = {
                                    onCharityListClick(CharityListNav)
                                },
                                onContentClick = { },
                                onKidsActivityClick = {
                                    onWebClick(
                                        WebNav(
                                            url = AppConstant.URL_LYNK_KIDS_ACTIVITY,
                                            title = kidsTitle,
                                        ),
                                    )
                                },
                            )
                        }
                    }
                }
            },
            bottomBar = {
                NavigationBar {
                    MainScreenModel.MainPageState.entries.forEach {
                        TabNavigationItem(
                            mainPageState = it,
                            pageSelected = state.mainPageState,
                            onClick = { vm.updatePage(it) },
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
        if (vm.shouldRefreshHome) {
            vm.onViewed()
            vm.shouldRefreshHome = false
        }
    }
}

@Composable
private fun DialogPrivacyPolicy() {
    val vm = koinViewModel<MainScreenModel>()
    val appRepository = KoinPlatform.getKoin().get<AppRepository>()

    Scaffold(
        topBar = {
            BaseTopAppBar(
                title = stringResource(Res.string.privacy_policy),
                showLeftButton = false,
                showRightButton = false,
            )
        },
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues).fillMaxSize()) {
            val url = AppConstant.getPrivacyPolicyUrl(appRepository.getSetting().language.id)

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
                text = stringResource(Res.string.accept),
                onClick = { vm.acceptPrivacyPolicy() },
            )
        }
    }
}
