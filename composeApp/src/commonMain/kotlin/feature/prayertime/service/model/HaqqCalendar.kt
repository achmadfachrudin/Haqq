package feature.prayertime.service.model

import kotlinx.serialization.Serializable

@Serializable
data class HaqqCalendar(
    val haqqYear: Int,
    val haqqMonth: HijriMonth,
    val haqqDays: List<HaqqDay>,
    val timeZone: String,
) {
    @Serializable
    sealed class HaqqDay {
        @Serializable
        data class Label(
            val day: HijriDay,
        ) : HaqqDay()

        @Serializable
        data object Additional : HaqqDay()
    }
}
