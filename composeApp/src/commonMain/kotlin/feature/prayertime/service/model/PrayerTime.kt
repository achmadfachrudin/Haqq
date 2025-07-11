package feature.prayertime.service.model

import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.Serializable
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
@Serializable
data class PrayerTime(
    val mapStringTime: Map<Salah, String>, // SUBUH to 05:00
    val mapLocalTime: Map<Salah, LocalTime>, // SUBUH to LocalTime(5,0)
    val gregorian: Gregorian,
    val hijri: Hijri,
    val day: HijriDay,
    val locationName: String,
) : HaqqCalendar.HaqqDay() {
    /**
     * @return Triple<Prayer, LocalTime, Boolean>
     *     Prayer: Prayer.SUBUH
     *     String: "05:00"
     *     Boolean: isToday
     */
    fun whatNextPrayerTime(
        tomorrow: PrayerTime,
        showFardOnly: Boolean = false,
    ): Triple<Salah, String, Boolean> {
        try {
            val thisTime =
                Clock.System
                    .now()
                    .toLocalDateTime(TimeZone.currentSystemDefault())
                    .time
            val todaySalahTimes =
                mapLocalTime
                    .toList()
                    .sortedBy { it.second }
                    .filter { if (showFardOnly) it.first != Salah.IMSAK && it.first != Salah.SYURUQ else true }
                    .firstOrNull { it.second > thisTime }

            val tomorrowSalahTimes =
                tomorrow.mapLocalTime
                    .toList()
                    .sortedBy { it.second }
                    .first { if (showFardOnly) it.first != Salah.IMSAK && it.first != Salah.SYURUQ else true }

            // Find the next prayer time
            val nextPrayer =
                if (todaySalahTimes != null) {
                    val stringTime = mapStringTime.get(todaySalahTimes.first).orEmpty()
                    Triple(todaySalahTimes.first, stringTime, true)
                } else {
                    val stringTime = tomorrow.mapStringTime.get(tomorrowSalahTimes.first).orEmpty()
                    Triple(tomorrowSalahTimes.first, stringTime, false)
                }

            return nextPrayer
        } catch (e: Exception) {
            return Triple(Salah.SUBUH, "", true)
        }
    }

    @Serializable
    data class Gregorian(
        val date: Int,
        val month: GregorianMonth,
        val year: Int,
    ) {
        val fullDate: String
            get() = "$date ${month.monthName} $year"
    }

    @Serializable
    data class Hijri(
        val date: Int,
        val month: HijriMonth,
        val year: Int,
    ) {
        val fullDate: String
            get() = "$date ${month.monthName} $year"
    }
}
