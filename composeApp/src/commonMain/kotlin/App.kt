import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import core.ui.theme.HaqqTheme
import feature.article.screen.ArticleListNav
import feature.article.screen.ArticleListScreen
import feature.charity.screen.CharityDetailNav
import feature.charity.screen.CharityDetailScreen
import feature.charity.screen.CharityListNav
import feature.charity.screen.CharityListScreen
import feature.conversation.screen.ConversationNav
import feature.conversation.screen.ConversationScreen
import feature.dhikr.screen.AsmaulHusnaNav
import feature.dhikr.screen.AsmaulHusnaScreen
import feature.dhikr.screen.DhikrPagerNav
import feature.dhikr.screen.DhikrPagerScreen
import feature.dhikr.screen.DuaListNav
import feature.dhikr.screen.DuaListScreen
import feature.dhikr.screen.DuaPagerNav
import feature.dhikr.screen.DuaPagerScreen
import feature.dhikr.screen.DuaSunnahNav
import feature.dhikr.screen.DuaSunnahScreen
import feature.home.screen.MainNav
import feature.home.screen.MainScreen
import feature.other.screen.OtherNav
import feature.other.screen.OtherScreen
import feature.other.screen.SettingNav
import feature.other.screen.SettingScreen
import feature.other.service.AppRepository
import feature.prayertime.screen.CalendarNav
import feature.prayertime.screen.CalendarScreen
import feature.prayertime.screen.GuidanceNav
import feature.prayertime.screen.GuidanceScreen
import feature.prayertime.screen.PrayerTimeDailyNav
import feature.prayertime.screen.PrayerTimeDailyScreen
import feature.quran.screen.VerseListNav
import feature.quran.screen.VerseListScreen
import feature.study.screen.ChannelListNav
import feature.study.screen.ChannelListScreen
import feature.study.screen.NoteDetailNav
import feature.study.screen.NoteDetailScreen
import feature.study.screen.NoteListNav
import feature.study.screen.NoteListScreen
import feature.study.screen.VideoListNav
import feature.study.screen.VideoListScreen
import feature.web.screen.WebNav
import feature.web.screen.WebScreen
import feature.web.screen.YoutubeNav
import feature.web.screen.YoutubeScreen
import feature.zakat.screen.ZakatNav
import feature.zakat.screen.ZakatScreen
import org.koin.compose.koinInject

@Composable
fun App() {
    HaqqTheme {
        val appRepository = koinInject<AppRepository>()
        val languageIso = appRepository.getSetting().language.iso
        val localization = koinInject<Localization>()
        localization.applyLanguage(languageIso)
        val navController: NavHostController = rememberNavController()

        NavHost(navController, startDestination = MainNav) {
            composable<MainNav> {
                MainScreen(
                    onArticleListClick = { navController.navigate(it) },
                    onAsmaulHusnaClick = { navController.navigate(it) },
                    onCalendarClick = { navController.navigate(it) },
                    onDhikrListClick = { navController.navigate(it) },
                    onDuaListClick = { navController.navigate(it) },
                    onDuaSunnahClick = { navController.navigate(it) },
                    onGuidanceClick = { navController.navigate(it) },
                    onPrayerTimeDailyClick = { navController.navigate(it) },
                    onCharityListClick = { navController.navigate(it) },
                    onConversationClick = { navController.navigate(it) },
                    onChannelListClick = { navController.navigate(it) },
                    onNoteListClick = { navController.navigate(it) },
                    onOtherClick = { navController.navigate(it) },
                    onSettingClick = { navController.navigate(it) },
                    onVerseListClick = { navController.navigate(it) },
                    onWebClick = { navController.navigate(it) },
                    onYoutubeClick = { navController.navigate(it) },
                    onZakatClick = { navController.navigate(it) },
                )
            }
            composable<ArticleListNav> {
                ArticleListScreen(
                    onBackClick = { navController.popBackStack() },
                    onWebClick = {
                        navController.navigate(it)
                    },
                )
            }
            composable<ChannelListNav> {
                ChannelListScreen(
                    onBackClick = { navController.popBackStack() },
                    onVideoList = { navController.navigate(it) },
                )
            }
            composable<NoteListNav> {
                NoteListScreen(
                    onBackClick = { navController.popBackStack() },
                    onNoteDetailClick = { navController.navigate(it) },
                )
            }
            composable<NoteDetailNav> { backStackEntry ->
                val nav: NoteDetailNav = backStackEntry.toRoute()

                NoteDetailScreen(
                    nav = nav,
                    onBackClick = { navController.popBackStack() },
                )
            }
            composable<VideoListNav> { backStackEntry ->
                val nav: VideoListNav = backStackEntry.toRoute()

                VideoListScreen(
                    nav = nav,
                    onBackClick = { navController.popBackStack() },
                    onWebClick = { navController.navigate(it) },
                    onYoutubeClick = { navController.navigate(it) },
                )
            }
            composable<CharityListNav> {
                CharityListScreen(
                    onBackClick = { navController.popBackStack() },
                    onCharityDetailClick = { navController.navigate(it) },
                    onSupportClick = { navController.navigate(it) },
                )
            }
            composable<CharityDetailNav> { backStackEntry ->
                val nav: CharityDetailNav = backStackEntry.toRoute()

                CharityDetailScreen(
                    nav = nav,
                    onBackClick = { navController.popBackStack() },
                )
            }
            composable<ConversationNav> {
                ConversationScreen(onBackClick = { navController.popBackStack() })
            }
            composable<AsmaulHusnaNav> {
                AsmaulHusnaScreen(onBackClick = { navController.popBackStack() })
            }
            composable<DhikrPagerNav> { backStackEntry ->
                val nav: DhikrPagerNav = backStackEntry.toRoute()

                DhikrPagerScreen(
                    nav = nav,
                    onBackClick = { navController.popBackStack() },
                )
            }
            composable<DuaListNav> { backStackEntry ->
                val nav: DuaListNav = backStackEntry.toRoute()

                DuaListScreen(
                    nav = nav,
                    onBackClick = { navController.popBackStack() },
                    onDuaDetailClick = { navController.navigate(it) },
                )
            }
            composable<DuaPagerNav> { backStackEntry ->
                val nav: DuaPagerNav = backStackEntry.toRoute()

                DuaPagerScreen(
                    nav = nav,
                    onBackClick = { navController.popBackStack() },
                )
            }
            composable<DuaSunnahNav> {
                DuaSunnahScreen(
                    onBackClick = { navController.popBackStack() },
                    onDuaListClick = { navController.navigate(it) },
                )
            }
            composable<CalendarNav> {
                CalendarScreen(
                    onBackClick = { navController.popBackStack() },
                )
            }
            composable<GuidanceNav> { backStackEntry ->
                val nav: GuidanceNav = backStackEntry.toRoute()

                GuidanceScreen(
                    nav = nav,
                    onBackClick = { navController.popBackStack() },
                    onYoutubeClick = { navController.navigate(it) },
                )
            }
            composable<PrayerTimeDailyNav> {
                PrayerTimeDailyScreen(
                    onBackClick = { navController.popBackStack() },
                )
            }
            composable<VerseListNav> { backStackEntry ->
                val nav: VerseListNav = backStackEntry.toRoute()

                VerseListScreen(
                    nav = nav,
                    onBackClick = { navController.popBackStack() },
                )
            }
            composable<OtherNav> {
                OtherScreen(
                    onBackClick = { navController.popBackStack() },
                    onWebClick = { navController.navigate(it) },
                )
            }
            composable<SettingNav> {
                SettingScreen(onBackClick = { navController.popBackStack() })
            }
            composable<WebNav> { backStackEntry ->
                val webNav: WebNav = backStackEntry.toRoute()
                WebScreen(
                    webNav = webNav,
                    onBackClick = { navController.popBackStack() },
                )
            }
            composable<YoutubeNav> { backStackEntry ->
                val youtubeNav: YoutubeNav = backStackEntry.toRoute()
                YoutubeScreen(youtubeNav = youtubeNav)
            }
            composable<ZakatNav> {
                ZakatScreen(onBackClick = { navController.popBackStack() })
            }
        }
    }
}
