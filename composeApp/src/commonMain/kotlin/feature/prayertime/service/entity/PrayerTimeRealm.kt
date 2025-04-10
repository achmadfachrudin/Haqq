package feature.prayertime.service.entity

import feature.prayertime.service.mapper.DEFAULT_GREGORIAN_DATE
import feature.prayertime.service.mapper.DEFAULT_GREGORIAN_FULLDATE
import feature.prayertime.service.mapper.DEFAULT_GREGORIAN_MONTH
import feature.prayertime.service.mapper.DEFAULT_GREGORIAN_YEAR
import feature.prayertime.service.mapper.DEFAULT_HIJRI_DATE
import feature.prayertime.service.mapper.DEFAULT_HIJRI_MONTH
import feature.prayertime.service.mapper.DEFAULT_HIJRI_YEAR
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

class PrayerTimeRealm : RealmObject {
    @PrimaryKey
    var gregorianFullDate = DEFAULT_GREGORIAN_FULLDATE
    var gregorianDate = DEFAULT_GREGORIAN_DATE
    var gregorianMonth = DEFAULT_GREGORIAN_MONTH
    var gregorianYear = DEFAULT_GREGORIAN_YEAR
    var hijriDate = DEFAULT_HIJRI_DATE
    var hijriMonth = DEFAULT_HIJRI_MONTH
    var hijriYear = DEFAULT_HIJRI_YEAR
    var day = ""
    var locationName = ""
    var timeImsak = ""
    var timeSubuh = ""
    var timeSyuruq = ""
    var timeZhuhur = ""
    var timeAshar = ""
    var timeMaghrib = ""
    var timeIsya = ""
    var timeLastThird = ""
}
