package feature.dhikr.service.entity

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

class DuaCategoryRealm : RealmObject {
    @PrimaryKey
    var tag = ""
    var titleId = ""
    var titleEn = ""
}
