package feature.prayertime.service.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import feature.prayertime.service.mapper.DEFAULT_GREGORIAN_DATE
import feature.prayertime.service.mapper.DEFAULT_GREGORIAN_FULLDATE
import feature.prayertime.service.mapper.DEFAULT_GREGORIAN_MONTH
import feature.prayertime.service.mapper.DEFAULT_GREGORIAN_YEAR
import feature.prayertime.service.mapper.DEFAULT_HIJRI_DATE
import feature.prayertime.service.mapper.DEFAULT_HIJRI_MONTH
import feature.prayertime.service.mapper.DEFAULT_HIJRI_YEAR

@Entity
data class PrayerTimeRealm(
    @PrimaryKey
    var gregorianFullDate: String = DEFAULT_GREGORIAN_FULLDATE,
    var gregorianDate: Int = DEFAULT_GREGORIAN_DATE,
    var gregorianMonth: Int = DEFAULT_GREGORIAN_MONTH,
    var gregorianYear: Int = DEFAULT_GREGORIAN_YEAR,
    var hijriDate: Int = DEFAULT_HIJRI_DATE,
    var hijriMonth: Int = DEFAULT_HIJRI_MONTH,
    var hijriYear: Int = DEFAULT_HIJRI_YEAR,
    var day: String = "",
    var locationName: String = "",
    var timeImsak: String = "",
    var timeSubuh: String = "",
    var timeSyuruq: String = "",
    var timeZhuhur: String = "",
    var timeAshar: String = "",
    var timeMaghrib: String = "",
    var timeIsya: String = "",
    var timeLastThird: String = "",
)
