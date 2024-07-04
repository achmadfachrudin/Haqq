package feature.study.service.entity

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

class NoteRealm : RealmObject {
    @PrimaryKey
    var id: Int = 1
    var title: String = ""
    var text: String = ""
    var kitab: String = ""
    var speaker: String = ""
    var createdAt: Long = 0
    var studyAt: Long = 0
}
