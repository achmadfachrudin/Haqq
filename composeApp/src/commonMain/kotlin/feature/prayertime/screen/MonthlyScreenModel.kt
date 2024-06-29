package feature.prayertime.screen

import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import feature.prayertime.service.mapper.DEFAULT_HIJRI_YEAR
import feature.prayertime.service.model.HaqqCalendar
import feature.prayertime.service.model.HijriMonth
import kotlinx.coroutines.launch

class MonthlyScreenModel : StateScreenModel<MonthlyScreenModel.State>(State()) {
    data class State(
        val haqqCalendar: HaqqCalendar =
            HaqqCalendar(
                haqqYear = DEFAULT_HIJRI_YEAR,
                haqqMonth = HijriMonth.SYAWWAL,
                haqqDays = listOf(),
                timeZone = "",
            ),
    )

    fun updateHaqqCalendar(haqqCalendar: HaqqCalendar) {
        screenModelScope.launch {
            mutableState.value = state.value.copy(haqqCalendar = haqqCalendar)
        }
    }
}
