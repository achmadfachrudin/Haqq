package feature.prayertime.service.mapper

import core.util.orZero
import feature.prayertime.service.entity.ConvertDateEntity
import feature.prayertime.service.entity.HaqqCalendarEntity
import feature.prayertime.service.entity.PrayerTimeRoom
import feature.prayertime.service.entity.PrayertimeEntity
import feature.prayertime.service.model.GregorianMonth
import feature.prayertime.service.model.HaqqCalendar
import feature.prayertime.service.model.HijriDay
import feature.prayertime.service.model.HijriMonth
import feature.prayertime.service.model.PrayerTime
import feature.prayertime.service.model.Salah
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalTime

internal const val DEFAULT_HIJRI_DATE = 8
internal const val DEFAULT_HIJRI_MONTH = 6
internal const val DEFAULT_HIJRI_YEAR = 1444
internal const val DEFAULT_HIJRI_FULLDATE = "1444-06-08"
internal const val DEFAULT_GREGORIAN_WEEKDAY = "SUNDAY"
internal const val DEFAULT_GREGORIAN_DATE = 1
internal const val DEFAULT_GREGORIAN_MONTH = 1
internal const val DEFAULT_GREGORIAN_YEAR = 2023
internal const val DEFAULT_GREGORIAN_FULLDATE = "2023-01-01" // year month date
internal const val DEFAULT_TIME = "00:00"

/**
 * @param time: "05:00"
 * @return LocalTime(hour = 5, minute = 0)
 */
private fun mapHourToLocalTime(time: String): LocalTime {
    val cleanTime = time.filter { it.isDigit() || it == ':' }
    val hour = cleanTime.split(":").first().toIntOrNull() ?: 1
    val minute = cleanTime.split(":").last().toIntOrNull() ?: 0
    return LocalTime(hour = hour, minute = minute)
}

internal fun ConvertDateEntity.mapToHijri(): Triple<Int, Int, Int> {
    val date =
        data
            ?.hijri
            ?.day
            ?.toInt()
            .orZero()
    val month =
        data
            ?.hijri
            ?.month
            ?.number
            .orZero()
    val year =
        data
            ?.hijri
            ?.year
            ?.toInt()
            .orZero()
    return Triple(date, month, year)
}

internal fun PrayertimeEntity.Data.mapToRoom(locationName: String): PrayerTimeRoom {
    val gYear = date.gregorian.year?.toInt()
    val gMonth = date.gregorian.month?.number
    val gDate = date.gregorian.day?.toInt()

    return PrayerTimeRoom(
        gregorianFullDate = "$gYear-$gMonth-$gDate",
        gregorianDate = gDate ?: DEFAULT_GREGORIAN_DATE,
        gregorianMonth = gMonth ?: DEFAULT_GREGORIAN_MONTH,
        gregorianYear = gYear ?: DEFAULT_GREGORIAN_YEAR,
        hijriDate = date.hijri.day?.toInt() ?: DEFAULT_HIJRI_DATE,
        hijriMonth = date.hijri.month?.number ?: DEFAULT_HIJRI_MONTH,
        hijriYear = date.hijri.year?.toInt() ?: DEFAULT_HIJRI_YEAR,
        day =
            date.gregorian.weekday.en
                ?.uppercase() ?: DEFAULT_GREGORIAN_WEEKDAY,
        locationName = locationName,
        timeImsak = timings.Imsak ?: DEFAULT_TIME,
        timeSubuh = timings.Fajr ?: DEFAULT_TIME,
        timeSyuruq = timings.Sunrise ?: DEFAULT_TIME,
        timeZhuhur = timings.Dhuhr ?: DEFAULT_TIME,
        timeAshar = timings.Asr ?: DEFAULT_TIME,
        timeMaghrib = timings.Maghrib ?: DEFAULT_TIME,
        timeIsya = timings.Isha ?: DEFAULT_TIME,
        timeLastThird = timings.Lastthird ?: DEFAULT_TIME,
    )
}

internal fun PrayertimeEntity.Data.mapToPrayerTime(): PrayerTime {
    val timeImsak = timings.Imsak ?: DEFAULT_TIME
    val timeSubuh = timings.Fajr ?: DEFAULT_TIME
    val timeSyuruq = timings.Sunrise ?: DEFAULT_TIME
    val timeZhuhur = timings.Dhuhr ?: DEFAULT_TIME
    val timeAshar = timings.Asr ?: DEFAULT_TIME
    val timeMaghrib = timings.Maghrib ?: DEFAULT_TIME
    val timeIsya = timings.Isha ?: DEFAULT_TIME
    val timeLastThird = timings.Lastthird ?: DEFAULT_TIME

    return PrayerTime(
        mapStringTime =
            mapOf(
                Salah.IMSAK to timeImsak,
                Salah.SUBUH to timeSubuh,
                Salah.SYURUQ to timeSyuruq,
                Salah.ZHUHUR to timeZhuhur,
                Salah.ASHAR to timeAshar,
                Salah.MAGHRIB to timeMaghrib,
                Salah.ISYA to timeIsya,
                Salah.LASTTHIRD to timeLastThird,
            ),
        mapLocalTime =
            mapOf(
                Salah.IMSAK to mapHourToLocalTime(timeImsak),
                Salah.SUBUH to mapHourToLocalTime(timeSubuh),
                Salah.SYURUQ to mapHourToLocalTime(timeSyuruq),
                Salah.ZHUHUR to mapHourToLocalTime(timeZhuhur),
                Salah.ASHAR to mapHourToLocalTime(timeAshar),
                Salah.MAGHRIB to mapHourToLocalTime(timeMaghrib),
                Salah.ISYA to mapHourToLocalTime(timeIsya),
                Salah.LASTTHIRD to mapHourToLocalTime(timeLastThird),
            ),
        gregorian =
            PrayerTime.Gregorian(
                date = date.gregorian.day?.toInt() ?: DEFAULT_GREGORIAN_DATE,
                month =
                    GregorianMonth.entries
                        .find {
                            it.monthNumber == (date.gregorian.month?.number ?: DEFAULT_GREGORIAN_MONTH)
                        }
                        ?: GregorianMonth.JANUARY,
                year = date.gregorian.year?.toInt() ?: DEFAULT_GREGORIAN_YEAR,
            ),
        hijri =
            PrayerTime.Hijri(
                date = date.hijri.day?.toInt() ?: DEFAULT_HIJRI_DATE,
                month =
                    HijriMonth.entries
                        .find { it.monthNumber == (date.hijri.month?.number ?: DEFAULT_HIJRI_MONTH) }
                        ?: HijriMonth.JUMADIL_AKHIR,
                year = date.hijri.year?.toInt() ?: DEFAULT_HIJRI_YEAR,
            ),
        day =
            getHijriDay(
                DayOfWeek.valueOf(
                    date.gregorian.weekday.en
                        ?.uppercase() ?: DEFAULT_GREGORIAN_WEEKDAY,
                ),
            ),
        locationName = "",
    )
}

internal fun PrayerTimeRoom.mapToModel(): PrayerTime {
    val timeLastThird = timeLastThird
    val timeImsak = timeImsak
    val timeSubuh = timeSubuh
    val timeSyuruq = timeSyuruq
    val timeZhuhur = timeZhuhur
    val timeAshar = timeAshar
    val timeMaghrib = timeMaghrib
    val timeIsya = timeIsya

    return PrayerTime(
        mapStringTime =
            mapOf(
                Salah.LASTTHIRD to timeLastThird,
                Salah.IMSAK to timeImsak,
                Salah.SUBUH to timeSubuh,
                Salah.SYURUQ to timeSyuruq,
                Salah.ZHUHUR to timeZhuhur,
                Salah.ASHAR to timeAshar,
                Salah.MAGHRIB to timeMaghrib,
                Salah.ISYA to timeIsya,
            ),
        mapLocalTime =
            mapOf(
                Salah.LASTTHIRD to mapHourToLocalTime(timeLastThird),
                Salah.IMSAK to mapHourToLocalTime(timeImsak),
                Salah.SUBUH to mapHourToLocalTime(timeSubuh),
                Salah.SYURUQ to mapHourToLocalTime(timeSyuruq),
                Salah.ZHUHUR to mapHourToLocalTime(timeZhuhur),
                Salah.ASHAR to mapHourToLocalTime(timeAshar),
                Salah.MAGHRIB to mapHourToLocalTime(timeMaghrib),
                Salah.ISYA to mapHourToLocalTime(timeIsya),
            ),
        gregorian =
            PrayerTime.Gregorian(
                date = gregorianDate,
                month =
                    GregorianMonth.entries
                        .find { it.monthNumber == gregorianMonth }
                        ?: GregorianMonth.JANUARY,
                year = gregorianYear,
            ),
        hijri =
            PrayerTime.Hijri(
                date = hijriDate,
                month =
                    HijriMonth.entries
                        .find { it.monthNumber == hijriMonth }
                        ?: HijriMonth.JUMADIL_AKHIR,
                year = hijriYear,
            ),
        day = getHijriDay(DayOfWeek.valueOf(day)),
        locationName = locationName,
    )
}

internal fun HaqqCalendarEntity.mapToHaqqCalendar(): HaqqCalendar {
    val firstDayName =
        DayOfWeek.valueOf(
            data
                ?.first()
                ?.date
                ?.gregorian
                ?.weekday
                ?.en
                ?.uppercase() ?: DEFAULT_GREGORIAN_WEEKDAY,
        )
    val lastDayName =
        DayOfWeek.valueOf(
            data
                ?.last()
                ?.date
                ?.gregorian
                ?.weekday
                ?.en
                ?.uppercase() ?: DEFAULT_GREGORIAN_WEEKDAY,
        )
    val hijriYear =
        data
            ?.first()
            ?.date
            ?.hijri
            ?.year
            ?.toInt() ?: DEFAULT_HIJRI_YEAR
    val hijriMonth =
        data
            ?.first()
            ?.date
            ?.hijri
            ?.month
            ?.number ?: DEFAULT_HIJRI_MONTH
    val timeZone =
        data
            ?.first()
            ?.timings
            ?.Fajr
            ?.substringAfterLast("(")
            ?.substringBeforeLast(")")
            .orEmpty()

    val fullCalendarList = mutableListOf<HaqqCalendar.HaqqDay>()

    val labelDays =
        mutableListOf(
            HaqqCalendar.HaqqDay.Label(HijriDay.SENIN),
            HaqqCalendar.HaqqDay.Label(HijriDay.SELASA),
            HaqqCalendar.HaqqDay.Label(HijriDay.RABU),
            HaqqCalendar.HaqqDay.Label(HijriDay.KAMIS),
            HaqqCalendar.HaqqDay.Label(HijriDay.JUMAT),
            HaqqCalendar.HaqqDay.Label(HijriDay.SABTU),
            HaqqCalendar.HaqqDay.Label(HijriDay.AHAD),
        )

    val additionalStartDays =
        when (firstDayName) {
            DayOfWeek.MONDAY -> mutableListOf()

            DayOfWeek.TUESDAY ->
                mutableListOf(
                    HaqqCalendar.HaqqDay.Additional,
                )

            DayOfWeek.WEDNESDAY ->
                mutableListOf(
                    HaqqCalendar.HaqqDay.Additional,
                    HaqqCalendar.HaqqDay.Additional,
                )

            DayOfWeek.THURSDAY ->
                mutableListOf(
                    HaqqCalendar.HaqqDay.Additional,
                    HaqqCalendar.HaqqDay.Additional,
                    HaqqCalendar.HaqqDay.Additional,
                )

            DayOfWeek.FRIDAY ->
                mutableListOf(
                    HaqqCalendar.HaqqDay.Additional,
                    HaqqCalendar.HaqqDay.Additional,
                    HaqqCalendar.HaqqDay.Additional,
                    HaqqCalendar.HaqqDay.Additional,
                )

            DayOfWeek.SATURDAY ->
                mutableListOf(
                    HaqqCalendar.HaqqDay.Additional,
                    HaqqCalendar.HaqqDay.Additional,
                    HaqqCalendar.HaqqDay.Additional,
                    HaqqCalendar.HaqqDay.Additional,
                    HaqqCalendar.HaqqDay.Additional,
                )

            DayOfWeek.SUNDAY ->
                mutableListOf(
                    HaqqCalendar.HaqqDay.Additional,
                    HaqqCalendar.HaqqDay.Additional,
                    HaqqCalendar.HaqqDay.Additional,
                    HaqqCalendar.HaqqDay.Additional,
                    HaqqCalendar.HaqqDay.Additional,
                    HaqqCalendar.HaqqDay.Additional,
                )

            else -> mutableListOf()
        }

    val haqqDays =
        data
            ?.map { day ->
                day.mapToPrayerTime()
            }?.toMutableList() ?: listOf()

    val additionalEndDays =
        when (lastDayName) {
            DayOfWeek.MONDAY ->
                mutableListOf(
                    HaqqCalendar.HaqqDay.Additional,
                    HaqqCalendar.HaqqDay.Additional,
                    HaqqCalendar.HaqqDay.Additional,
                    HaqqCalendar.HaqqDay.Additional,
                    HaqqCalendar.HaqqDay.Additional,
                    HaqqCalendar.HaqqDay.Additional,
                )

            DayOfWeek.TUESDAY ->
                mutableListOf(
                    HaqqCalendar.HaqqDay.Additional,
                    HaqqCalendar.HaqqDay.Additional,
                    HaqqCalendar.HaqqDay.Additional,
                    HaqqCalendar.HaqqDay.Additional,
                    HaqqCalendar.HaqqDay.Additional,
                )

            DayOfWeek.WEDNESDAY ->
                mutableListOf(
                    HaqqCalendar.HaqqDay.Additional,
                    HaqqCalendar.HaqqDay.Additional,
                    HaqqCalendar.HaqqDay.Additional,
                    HaqqCalendar.HaqqDay.Additional,
                )

            DayOfWeek.THURSDAY ->
                mutableListOf(
                    HaqqCalendar.HaqqDay.Additional,
                    HaqqCalendar.HaqqDay.Additional,
                    HaqqCalendar.HaqqDay.Additional,
                )

            DayOfWeek.FRIDAY ->
                mutableListOf(
                    HaqqCalendar.HaqqDay.Additional,
                    HaqqCalendar.HaqqDay.Additional,
                )

            DayOfWeek.SATURDAY ->
                mutableListOf(
                    HaqqCalendar.HaqqDay.Additional,
                )

            DayOfWeek.SUNDAY -> mutableListOf()

            else -> mutableListOf()
        }

    fullCalendarList.addAll(labelDays)
    fullCalendarList.addAll(additionalStartDays)
    fullCalendarList.addAll(haqqDays)
    fullCalendarList.addAll(additionalEndDays)

    return HaqqCalendar(
        haqqYear = hijriYear,
        haqqMonth = HijriMonth.entries.first { it.monthNumber == hijriMonth },
        haqqDays = fullCalendarList,
        timeZone = timeZone,
    )
}

private fun getHijriDay(dayOfWeek: DayOfWeek): HijriDay =
    when (dayOfWeek) {
        DayOfWeek.MONDAY -> HijriDay.SENIN
        DayOfWeek.TUESDAY -> HijriDay.SELASA
        DayOfWeek.WEDNESDAY -> HijriDay.RABU
        DayOfWeek.THURSDAY -> HijriDay.KAMIS
        DayOfWeek.FRIDAY -> HijriDay.JUMAT
        DayOfWeek.SATURDAY -> HijriDay.SABTU
        DayOfWeek.SUNDAY -> HijriDay.AHAD
        else -> HijriDay.SENIN
    }
