package feature.prayertime.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import core.data.DataState
import feature.prayertime.service.PrayerRepository
import feature.prayertime.service.mapper.DEFAULT_HIJRI_DATE
import feature.prayertime.service.mapper.DEFAULT_HIJRI_MONTH
import feature.prayertime.service.mapper.DEFAULT_HIJRI_YEAR
import feature.prayertime.service.model.HaqqCalendar
import feature.prayertime.service.model.HijriMonth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.number
import kotlinx.datetime.todayIn
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
class CalendarScreenModel(
    private val prayerRepository: PrayerRepository,
) : ViewModel() {
    val mutableState = MutableStateFlow(State())
    val state: StateFlow<State> = mutableState.asStateFlow()

    data class State(
        var dateSelected: Int = DEFAULT_HIJRI_DATE,
        var monthSelected: Int = DEFAULT_HIJRI_MONTH,
        var yearSelected: Int = DEFAULT_HIJRI_YEAR,
        var calendarState: CalendarState = CalendarState.Loading,
    ) {
        val hijriMonth: String
            get() = HijriMonth.entries.first { it.monthNumber == monthSelected }.monthName
    }

    sealed class CalendarState {
        object Loading : CalendarState()

        data class Error(
            val message: String,
        ) : CalendarState()

        data class Content(
            val haqqCalendar: HaqqCalendar,
        ) : CalendarState()
    }

    fun onPrevClicked() {
        viewModelScope.launch {
            val yearSelected = state.value.yearSelected
            val monthSelected = state.value.monthSelected

            if (monthSelected == HijriMonth.entries.first().monthNumber) {
                mutableState.value =
                    state.value.copy(
                        yearSelected = yearSelected - 1,
                        monthSelected = HijriMonth.entries.last().monthNumber,
                    )
            } else {
                mutableState.value =
                    state.value.copy(
                        monthSelected = monthSelected - 1,
                    )
            }

            getHijriMonth()
        }
    }

    fun onNextClicked() {
        viewModelScope.launch {
            val yearSelected = state.value.yearSelected
            val monthSelected = state.value.monthSelected

            if (monthSelected == HijriMonth.entries.last().monthNumber) {
                mutableState.value =
                    state.value.copy(
                        yearSelected = yearSelected + 1,
                        monthSelected = HijriMonth.entries.first().monthNumber,
                    )
            } else {
                mutableState.value =
                    state.value.copy(
                        monthSelected = monthSelected + 1,
                    )
            }

            getHijriMonth()
        }
    }

    fun getHijriDate() {
        viewModelScope.launch {
            val today: LocalDate = Clock.System.todayIn(TimeZone.currentSystemDefault())

            prayerRepository
                .fetchConvertGregorianToHijri(
                    today.day,
                    today.month.number,
                    today.year,
                ).collectLatest {
                    when (it) {
                        is DataState.Error ->
                            mutableState.value =
                                state.value.copy(calendarState = CalendarState.Error(it.message))

                        DataState.Loading ->
                            mutableState.value =
                                state.value.copy(calendarState = CalendarState.Loading)

                        is DataState.Success -> {
                            mutableState.value =
                                state.value.copy(
                                    dateSelected = it.data.first,
                                    monthSelected = it.data.second,
                                    yearSelected = it.data.third,
                                )

                            getHijriMonth()
                        }
                    }
                }
        }
    }

    private fun getHijriMonth() {
        viewModelScope.launch {
            prayerRepository
                .fetchHijriMonthTimes(
                    state.value.monthSelected,
                    state.value.yearSelected,
                ).collectLatest {
                    val calendarState =
                        when (it) {
                            is DataState.Error -> CalendarState.Error(it.message)
                            DataState.Loading -> CalendarState.Loading
                            is DataState.Success ->
                                CalendarState.Content(
                                    haqqCalendar = it.data,
                                )
                        }

                    mutableState.value = state.value.copy(calendarState = calendarState)
                }
        }
    }
}
