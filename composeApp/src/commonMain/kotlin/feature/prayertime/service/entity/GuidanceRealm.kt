package feature.prayertime.service.entity

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

class GuidanceRealm : RealmObject {
    @PrimaryKey
    var id = ""
    var position = 0
    var type = ""
    var image = ""
    var titleId = ""
    var titleEn = ""
    var descId = ""
    var descEn = ""
    var textIndopak = ""
    var textUthmani = ""
    var textTransliteration = ""
    var textTranslationId = ""
    var textTranslationEn = ""
    var hadithId = ""
    var hadithEn = ""
}
