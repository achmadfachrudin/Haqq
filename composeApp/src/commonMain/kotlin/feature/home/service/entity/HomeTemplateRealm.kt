package feature.home.service.entity

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

class HomeTemplateRealm : RealmObject {
    @PrimaryKey
    var position = 0
    var type = ""
    var labelId = ""
    var labelEn = ""
    var textId = ""
    var textEn = ""
    var images = ""
    var links = ""
}
