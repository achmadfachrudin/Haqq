package feature.prayertime.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.currentCompositeKeyHash
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinNavigatorScreenModel
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import core.ui.component.BaseSpacerHorizontal
import core.ui.component.BaseText
import core.ui.component.BaseTitle
import core.ui.component.BaseTopAppBar
import core.ui.component.ErrorState
import core.ui.component.LoadingState
import core.ui.theme.ExtraColor
import core.ui.theme.ExtraColor.BLUE
import core.ui.theme.ExtraColor.ORANGE
import core.ui.theme.ExtraColor.VIOLET
import core.ui.theme.getHaqqTypography
import feature.other.service.AppRepository
import feature.other.service.mapper.getString
import feature.other.service.model.AppSetting
import feature.other.service.model.AppString
import feature.prayertime.service.model.HaqqCalendar
import feature.prayertime.service.model.HijriDay
import feature.prayertime.service.model.HijriMonth
import feature.prayertime.service.model.PrayerTime
import haqq.composeapp.generated.resources.Res
import haqq.composeapp.generated.resources.calendar
import haqq.composeapp.generated.resources.chevron_left
import haqq.composeapp.generated.resources.chevron_right
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn
import org.jetbrains.compose.resources.painterResource
import org.koin.mp.KoinPlatform

class CalendarScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val screenModel = koinScreenModel<CalendarScreenModel>()
        val monthlyScreenModel = navigator.koinNavigatorScreenModel<MonthlyScreenModel>()
        val state by screenModel.state.collectAsState()
        val dateSelected = remember { mutableStateOf("") }

        Scaffold(
            topBar = {
                BaseTopAppBar(
                    title = AppString.CALENDAR_FASTING.getString(),
                    onLeftButtonClick = {
                        navigator.pop()
                    },
                    showRightButton = state.calendarState is CalendarScreenModel.CalendarState.Content,
                    rightButtonImage = painterResource(Res.drawable.calendar),
                    onRightButtonClick = {
                        when (val display = state.calendarState) {
                            is CalendarScreenModel.CalendarState.Content -> {
                                navigator.push(MonthlyPrayerTimeScreen())
                            }

                            is CalendarScreenModel.CalendarState.Error,
                            CalendarScreenModel.CalendarState.Loading,
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
                when (val display = state.calendarState) {
                    is CalendarScreenModel.CalendarState.Loading -> {
                        LoadingState()
                    }

                    is CalendarScreenModel.CalendarState.Content -> {
                        monthlyScreenModel.updateHaqqCalendar(display.haqqCalendar)

                        Row(modifier = Modifier.fillMaxWidth()) {
                            IconButton(onClick = { screenModel.onPrevClicked() }) {
                                Icon(
                                    painter = painterResource(Res.drawable.chevron_left),
                                    contentDescription = "",
                                )
                            }
                            BaseTitle(
                                text = "${state.hijriMonth} ${state.yearSelected}",
                                modifier = Modifier.weight(1f),
                            )

                            IconButton(onClick = { screenModel.onNextClicked() }) {
                                Icon(
                                    painter = painterResource(Res.drawable.chevron_right),
                                    contentDescription = "",
                                )
                            }
                        }

                        LazyVerticalGrid(columns = GridCells.Fixed(7)) {
                            items(display.haqqCalendar.haqqDays) { day ->
                                when (day) {
                                    HaqqCalendar.HaqqDay.Additional -> AdditionalCard()
                                    is HaqqCalendar.HaqqDay.Label -> LabelCard(day.day)
                                    is PrayerTime ->
                                        DayCard(
                                            day = day,
                                            onClick = {
                                                dateSelected.value = it
                                            },
                                        )
                                }
                            }
                        }

                        BaseText(
                            modifier = Modifier.fillMaxWidth().padding(8.dp),
                            text = dateSelected.value,
                        )

                        LegendCard(
                            MaterialTheme.colorScheme.error,
                            AppString.CALENDAR_HARAM.getString(),
                        )
                        LegendCard(
                            ExtraColor.getPairColor(VIOLET).first,
                            AppString.CALENDAR_DZULHIJJAH.getString(),
                        )
                        LegendCard(
                            ExtraColor.getPairColor(BLUE).first,
                            AppString.CALENDAR_MUHARRAM.getString(),
                        )
                        LegendCard(
                            MaterialTheme.colorScheme.primary,
                            AppString.CALENDAR_RAMADHAN.getString(),
                        )
                        LegendCard(
                            ExtraColor.getPairColor(ORANGE).first,
                            AppString.CALENDAR_AYYAMULBIDH.getString(),
                        )
                    }

                    is CalendarScreenModel.CalendarState.Error -> {
                        ErrorState(display.message)
                    }
                }
            }
        }

        LaunchedEffect(currentCompositeKeyHash) {
            screenModel.getHijriDate()
        }
    }

    @Composable
    private fun LabelCard(day: HijriDay) {
        val appRepository = KoinPlatform.getKoin().get<AppRepository>()
        val shortName =
            when (appRepository.getSetting().language) {
                AppSetting.Language.ENGLISH -> day.shortNameEn
                AppSetting.Language.INDONESIAN -> day.shortNameId
            }
        BaseText(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
            horizontalArrangement = Arrangement.Center,
            textAlign = TextAlign.Center,
            text = shortName,
        )
    }

    @Composable
    private fun DayCard(
        day: PrayerTime,
        onClick: (String) -> Unit,
    ) {
        val today = Clock.System.todayIn(TimeZone.currentSystemDefault())
        val isToday =
            LocalDate(
                day.gregorian.year,
                day.gregorian.month.monthNumber,
                day.gregorian.date,
            ) == today
        val isHaram =
            day.hijri.month == HijriMonth.DZULHIJJAH &&
                (day.hijri.date in 10..13) ||
                day.hijri.month == HijriMonth.SYAWWAL &&
                day.hijri.date == 1
        val isDzulhijjah = day.hijri.month == HijriMonth.DZULHIJJAH && day.hijri.date == 9
        val isMuharram =
            day.hijri.month == HijriMonth.MUHARRAM &&
                (day.hijri.date == 9 || day.hijri.date == 10)
        val isRamadhan = day.hijri.month == HijriMonth.RAMADHAN
        val isAyyamulBidh = day.hijri.date == 13 || day.hijri.date == 14 || day.hijri.date == 15

        val backgroundColor =
            when {
                isToday -> MaterialTheme.colorScheme.surfaceVariant
                isHaram -> MaterialTheme.colorScheme.error
                isDzulhijjah -> ExtraColor.getPairColor(VIOLET).first
                isMuharram -> ExtraColor.getPairColor(BLUE).first
                isRamadhan -> MaterialTheme.colorScheme.primary
                isAyyamulBidh -> ExtraColor.getPairColor(ORANGE).first
                else -> Color.Transparent
            }

        val textColor =
            when {
                isToday -> MaterialTheme.colorScheme.onSurfaceVariant
                isHaram -> MaterialTheme.colorScheme.onError
                isDzulhijjah -> ExtraColor.getPairColor(VIOLET).second
                isMuharram -> ExtraColor.getPairColor(BLUE).second
                isRamadhan -> MaterialTheme.colorScheme.onPrimary
                isAyyamulBidh -> ExtraColor.getPairColor(ORANGE).second
                else -> Color.Unspecified
            }

        Box(
            modifier = Modifier.background(backgroundColor),
            contentAlignment = Alignment.Center,
        ) {
            BaseText(
                modifier =
                    Modifier
                        .clickable {
                            onClick("${day.hijri.fullDate} / ${day.gregorian.fullDate}")
                        }.fillMaxWidth()
                        .padding(vertical = 16.dp),
                horizontalArrangement = Arrangement.Center,
                textAlign = TextAlign.Center,
                text = "${day.hijri.date}",
                color = textColor,
                style = getHaqqTypography().bodyMedium,
            )
        }
    }

    @Composable
    private fun AdditionalCard() {
        Box(modifier = Modifier.fillMaxWidth().background(Color.Transparent))
    }

    @Composable
    private fun LegendCard(
        color: Color,
        message: String,
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier.size(16.dp).background(color),
            )
            BaseSpacerHorizontal()
            BaseText(message)
        }
    }
}
