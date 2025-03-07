package feature.prayertime.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import core.data.DataState
import feature.prayertime.service.PrayerRepository
import feature.prayertime.service.mapper.DEFAULT_HIJRI_MONTH
import feature.prayertime.service.mapper.DEFAULT_HIJRI_YEAR
import feature.prayertime.service.model.HaqqCalendar
import feature.prayertime.service.model.PrayerTime
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class PrayerTimeDailyScreenModel(
    private val prayerRepository: PrayerRepository,
) : ViewModel() {
    val mutableState = MutableStateFlow(State())
    val state: StateFlow<State> = mutableState.asStateFlow()

    data class State(
        var monthSelected: Int = DEFAULT_HIJRI_MONTH,
        var yearSelected: Int = DEFAULT_HIJRI_YEAR,
        var todayTomorrowState: TodayTomorrowState = TodayTomorrowState.Loading,
        var calendarState: CalendarState = CalendarState.Loading,
    )

    sealed class TodayTomorrowState {
        object Loading : TodayTomorrowState()

        data class Error(
            val message: String,
        ) : TodayTomorrowState()

        data class Content(
            val times: List<PrayerTime>,
        ) : TodayTomorrowState()
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

    fun getPrayerTime() {
        viewModelScope.launch {
            prayerRepository.fetchTodayTomorrowPrayerTimes().collectLatest {
                if (it.data.isNotEmpty()) {
                    val monthSelected =
                        it.data
                            .first()
                            .hijri.month.monthNumber
                    val yearSelected =
                        it.data
                            .first()
                            .hijri.year
                    val timeState = TodayTomorrowState.Content(it.data)

                    mutableState.value =
                        state.value.copy(
                            todayTomorrowState = timeState,
                            monthSelected = monthSelected,
                            yearSelected = yearSelected,
                        )

                    getHijriMonth()
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
