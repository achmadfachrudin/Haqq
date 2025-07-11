package core.util

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.Month
import kotlinx.datetime.TimeZone
import kotlinx.datetime.number
import kotlinx.datetime.periodUntil
import kotlinx.datetime.toInstant
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
object DateUtil {
    val yyyy = "yyyy"
    val yy = "yy"
    val MMM = "MMM"
    val MM = "MM"
    val dd = "dd"
    val hh = "hh"
    val mm = "mm"
    val ss = "ss"
    val SSS = "SSS"
    val dd_MM_yyyy = "dd-MM-yyyy"

    /**
     * @param dateSource is String "2010-06-01T22:19:44.475Z"
     * @return String of 3 bulan lalu
     */
    fun getTimeAgo(dateSource: String): String {
        val nowTimeInstant = Clock.System.now()
        val videoTimeInstant = Instant.parse(dateSource)

        // Calculate the period between now and the source date
        val period = videoTimeInstant.periodUntil(nowTimeInstant, TimeZone.currentSystemDefault())

        // Determine the largest unit of time to describe the difference
        return when {
            period.years > 0 -> "${period.years} tahun lalu"
            period.months > 0 -> "${period.months} bulan lalu"
            period.days / 7 > 0 -> "${period.days / 7} minggu lalu"
            period.days > 0 -> "${period.days} hari lalu"
            period.hours > 0 -> "${period.hours} jam lalu"
            period.minutes > 0 -> "${period.minutes} menit lalu"
            else -> "Baru saja"
        }
    }

    /**
     * @param localDateTime is LocalDateTime
     * @param pattern is String dd-MM-yyyy
     * @return String of 21-12-2024
     */
    fun formatDateTimeToString(
        localDateTime: LocalDateTime,
        pattern: String,
    ): String {
        val formatted =
            pattern
                .replace(
                    yyyy,
                    localDateTime.date.year.addLeadingZero(4),
                ).replace(
                    yy,
                    localDateTime.date.year
                        .addLeadingZero()
                        .takeLast(2),
                ).replace(
                    MMM,
                    localDateTime.date.month.name
                        .lowercase()
                        .replaceFirstChar { it.uppercase() }
                        .take(3),
                ).replace(
                    MM,
                    localDateTime.date.monthNumber.addLeadingZero(),
                ).replace(
                    dd,
                    localDateTime.date.dayOfMonth.addLeadingZero(),
                ).replace(
                    hh,
                    localDateTime.time.hour.addLeadingZero(),
                ).replace(
                    mm,
                    localDateTime.time.minute.addLeadingZero(),
                ).replace(
                    ss,
                    localDateTime.time.second.addLeadingZero(),
                ).replace(
                    SSS,
                    localDateTime.time.nanosecond.addLeadingZero(3),
                )

        return formatted
    }

    /**
     * @param source 11-12-2004
     * @param pattern dd-MM-yyyy
     */
    fun formatDateTimeToLong(
        source: String,
        pattern: String,
    ): Long {
        val yearRangeIndex =
            when {
                pattern.contains(yyyy) -> pattern.findRangeIndex(yyyy)
                pattern.contains(yy) -> pattern.findRangeIndex(yy)
                else -> null
            }
        val monthRangeIndex =
            when {
                pattern.contains(MMM) -> pattern.findRangeIndex(MMM)
                pattern.contains(MM) -> pattern.findRangeIndex(MM)
                else -> null
            }
        val dayRangeIndex =
            when {
                pattern.contains(dd) -> pattern.findRangeIndex(dd)
                else -> null
            }
        val hourRangeIndex =
            when {
                pattern.contains(hh) -> pattern.findRangeIndex(hh)
                else -> null
            }
        val minuteRangeIndex =
            when {
                pattern.contains(mm) -> pattern.findRangeIndex(mm)
                else -> null
            }
        val secondRangeIndex =
            when {
                pattern.contains(ss) -> pattern.findRangeIndex(ss)
                else -> null
            }
        val nanosecondRangeIndex =
            when {
                pattern.contains(SSS) -> pattern.findRangeIndex(SSS)
                else -> null
            }

        val year = yearRangeIndex?.let { source.substring(it).toInt() }.orZero()
        val dayOfMonth = dayRangeIndex?.let { source.substring(it).toInt() }.orZero()

        val monthNumber =
            monthRangeIndex
                ?.let {
                    val monthString = source.substring(it)

                    when {
                        pattern.contains(MMM) -> {
                            val month = Month.entries.firstOrNull { it.name.contains(monthString) }

                            month?.number.orZero()
                        }

                        pattern.contains(MM) -> monthString.toInt()
                        else -> 0
                    }
                }.orZero()

        val hour = hourRangeIndex?.let { source.substring(it).toInt() }.orZero()
        val minute = minuteRangeIndex?.let { source.substring(it).toInt() }.orZero()
        val second = secondRangeIndex?.let { source.substring(it).toInt() }.orZero()
        val nanosecond = nanosecondRangeIndex?.let { source.substring(it).toInt() }.orZero()

        val localDateTime =
            LocalDateTime(
                year = year,
                monthNumber = monthNumber,
                dayOfMonth = dayOfMonth,
                hour = hour,
                minute = minute,
                second = second,
                nanosecond = nanosecond,
            )

        return localDateTime.toInstant(TimeZone.currentSystemDefault()).toEpochMilliseconds()
    }
}
