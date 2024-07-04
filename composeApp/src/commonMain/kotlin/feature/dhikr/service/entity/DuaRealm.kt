package feature.dhikr.service.entity

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

class DuaRealm : RealmObject {
    @PrimaryKey
    var id = 0
    var titleId = ""
    var titleEn = ""
    var textIndopak = ""
    var textUthmani = ""
    var textTransliteration = ""
    var textTranslationId = ""
    var textTranslationEn = ""
    var hadithId = ""
    var hadithEn = ""
    var tag = ""
}
