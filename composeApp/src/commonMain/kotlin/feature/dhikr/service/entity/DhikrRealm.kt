package feature.dhikr.service.entity

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

class DhikrRealm : RealmObject {
    @PrimaryKey
    var key = ""
    var id = 0
    var type = ""
    var titleId = ""
    var titleEn = ""
    var textIndopak = ""
    var textUthmani = ""
    var textTransliteration = ""
    var textTranslationId = ""
    var textTranslationEn = ""
    var hadithId = ""
    var hadithEn = ""
    var maxCount = 0
    var count = 0
}
