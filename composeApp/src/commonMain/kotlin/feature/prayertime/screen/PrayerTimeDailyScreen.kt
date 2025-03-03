package feature.prayertime.screen

import AnalyticsConstant.trackScreen
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberModalBottomSheetState
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
import kotlinx.serialization.Serializable
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.mp.KoinPlatform

@Serializable
object PrayerTimeDailyNav

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrayerTimeDailyScreen(onBackClick: () -> Unit) {
    val vm = koinViewModel<PrayerTimeDailyScreenModel>()
    val state by vm.state.collectAsState()

    var showMonthlyBottomSheet by remember { mutableStateOf(false) }
    val sheetState =
        rememberModalBottomSheetState(
            skipPartiallyExpanded = true,
        )

    Scaffold(
        topBar = {
            BaseTopAppBar(
                title = AppString.PRAYER_TIME_TITLE.getString(),
                showRightButton =
                    state.todayTomorrowState is PrayerTimeDailyScreenModel.TodayTomorrowState.Content &&
                        state.calendarState is PrayerTimeDailyScreenModel.CalendarState.Content,
                rightButtonImage = painterResource(Res.drawable.calendar),
                onLeftButtonClick = {
                    onBackClick()
                },
                onRightButtonClick = {
                    showMonthlyBottomSheet = true
                },
            )
        },
        snackbarHost = {
        },
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues).fillMaxSize()) {
            when (val display = state.todayTomorrowState) {
                is PrayerTimeDailyScreenModel.TodayTomorrowState.Loading -> {
                    LoadingState()
                }

                is PrayerTimeDailyScreenModel.TodayTomorrowState.Content -> {
                    PrayerSuccessContent(display.times)
                }

                is PrayerTimeDailyScreenModel.TodayTomorrowState.Error -> {
                    ErrorState(
                        message = display.message,
                        showRetryButton = !display.message.contains("[GPS]"),
                        onRetryButtonClick = { vm.getPrayerTime() },
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

        if (showMonthlyBottomSheet &&
            state.calendarState is PrayerTimeDailyScreenModel.CalendarState.Content
        ) {
            val haqqCalendar =
                (state.calendarState as PrayerTimeDailyScreenModel.CalendarState.Content).haqqCalendar

            ModalBottomSheet(
                modifier = Modifier.fillMaxHeight(),
                sheetState = sheetState,
                onDismissRequest = { showMonthlyBottomSheet = false },
            ) {
                PrayerTimeMonthlyBottomSheet(haqqCalendar)
            }
        }
    }

    LaunchedEffect(Unit) {
        trackScreen("PrayerTimeDailyScreen")
        vm.getPrayerTime()
    }
}

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
