package feature.prayertime.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.currentCompositeKeyHash
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinNavigatorScreenModel
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import core.ui.component.BaseLabelValueCard
import core.ui.component.BaseSpacerVertical
import core.ui.component.BaseTabRow
import core.ui.component.BaseText
import core.ui.component.BaseTopAppBar
import core.ui.component.ErrorState
import core.ui.component.LoadingState
import core.ui.theme.getHaqqTypography
import feature.other.service.AppRepository
import feature.other.service.mapper.getString
import feature.other.service.model.AppSetting
import feature.other.service.model.AppString
import feature.prayertime.service.model.PrayerTime
import haqq.composeapp.generated.resources.Res
import haqq.composeapp.generated.resources.calendar
import org.jetbrains.compose.resources.painterResource
import org.koin.mp.KoinPlatform

class PrayerTimeScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val screenModel = koinScreenModel<PrayerTimeScreenModel>()
        val monthlyScreenModel = navigator.koinNavigatorScreenModel<MonthlyScreenModel>()
        val state by screenModel.state.collectAsState()

        Scaffold(
            topBar = {
                BaseTopAppBar(
                    title = AppString.PRAYER_TIME_TITLE.getString(),
                    showRightButton =
                        state.todayTomorrowState is PrayerTimeScreenModel.TodayTomorrowState.Content &&
                            state.calendarState is PrayerTimeScreenModel.CalendarState.Content,
                    rightButtonImage = painterResource(Res.drawable.calendar),
                    onLeftButtonClick = {
                        navigator.pop()
                    },
                    onRightButtonClick = {
                        when (val display = state.calendarState) {
                            is PrayerTimeScreenModel.CalendarState.Content -> {
                                monthlyScreenModel.updateHaqqCalendar(display.haqqCalendar)
                                navigator.push(MonthlyPrayerTimeScreen())
                            }

                            is PrayerTimeScreenModel.CalendarState.Error,
                            PrayerTimeScreenModel.CalendarState.Loading,
                            -> {
                            }
                        }
                    },
                )
            },
            snackbarHost = {
            },
        ) { paddingValues ->
            Column(modifier = Modifier.padding(paddingValues).fillMaxSize()) {
                when (val display = state.todayTomorrowState) {
                    is PrayerTimeScreenModel.TodayTomorrowState.Loading -> {
                        LoadingState()
                    }

                    is PrayerTimeScreenModel.TodayTomorrowState.Content -> {
                        PrayerSuccessContent(display.times)
                    }

                    is PrayerTimeScreenModel.TodayTomorrowState.Error -> {
                        ErrorState(
                            message = display.message,
                            showRetryButton = !display.message.contains("[GPS]"),
                            onRetryButtonClick = { screenModel.getPrayerTime() },
                        )
                    }
                }

                BaseSpacerVertical()

                BaseText(
                    text = AppString.PRAYER_NOTE.getString(),
                    style = getHaqqTypography().labelMedium,
                    modifier = Modifier.padding(horizontal = 16.dp),
                )
            }
        }

        LaunchedEffect(currentCompositeKeyHash) {
            screenModel.onViewed()
        }
    }

    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    private fun PrayerSuccessContent(times: List<PrayerTime>) {
        val appRepository = KoinPlatform.getKoin().get<AppRepository>()

        val tabTitles =
            listOf(
                AppString.TODAY.getString(),
                AppString.TOMORROW.getString(),
            )
        val pagerState = rememberPagerState(pageCount = { tabTitles.size })

        BaseTabRow(
            pagerState = pagerState,
            tabTitles = tabTitles,
        )

        BaseSpacerVertical()

        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Top,
        ) { index ->
            val selected = times[index]
            val dayName =
                when (appRepository.getSetting().language) {
                    AppSetting.Language.ENGLISH -> selected.day.dayNameEn
                    AppSetting.Language.INDONESIAN -> selected.day.dayNameId
                }
            val header =
                """
                $dayName, ${selected.hijri.fullDate} / ${selected.gregorian.fullDate}
                ${selected.locationName}
                """.trimIndent()
            val nextTime = times.first().whatNextPrayerTime(times.last())
            val nextTimeIsToday = nextTime.third
            val shouldShowHighlight =
                (nextTimeIsToday && index == 0 || !nextTimeIsToday && index == 1)

            Column {
                BaseText(
                    header,
                    modifier = Modifier.padding(horizontal = 16.dp),
                )

                selected.mapStringTime.forEach {
                    BaseLabelValueCard(
                        label = it.key.title.getString(),
                        value = it.value,
                        showHighlight = nextTime.first == it.key && shouldShowHighlight,
                    )
                }
            }
        }
    }
}
