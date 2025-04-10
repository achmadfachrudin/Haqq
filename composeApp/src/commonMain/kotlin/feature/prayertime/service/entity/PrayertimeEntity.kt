package feature.prayertime.service.entity

import kotlinx.serialization.Serializable

@Serializable
data class PrayertimeEntity(
    val data: Data?,
) {
    @Serializable
    data class Data(
        val timings: Timings,
        val date: Date,
    ) {
        @Serializable
        data class Timings(
            val Fajr: String?,
            val Imsak: String?,
            val Sunrise: String?,
            val Dhuhr: String?,
            val Asr: String?,
            val Maghrib: String?,
            val Isha: String?,
            val Lastthird: String?,
        )

        @Serializable
        data class Date(
            val gregorian: Gregorian,
            val hijri: Hijri,
        ) {
            @Serializable
            data class Gregorian(
                val date: String?,
                val day: String?,
                val month: Month?,
                val year: String?,
                val weekday: WeekDay,
            )

            @Serializable
            data class Hijri(
                val date: String?,
                val day: String?,
                val month: Month?,
                val year: String?,
            )

            @Serializable
            data class Month(
                val number: Int?,
                val en: String?,
            )

            @Serializable
            data class WeekDay(
                val en: String?,
            )
        }
    }
}
