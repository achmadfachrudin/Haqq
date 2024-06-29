package feature.quran.service.entity

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

class JuzRealm : RealmObject {
    @PrimaryKey
    var id: Int = 0
    var juzNumber: Int = 0
    var chapters: String = ""
    var firstChapterNumber: Int = 0
    var firstVerseNumber: Int = 0
    var firstVerseId: Int = 0
    var lastChapterNumber: Int = 0
    var lastVerseNumber: Int = 0
    var lastVerseId: Int = 0
    var versesCount: Int = 0
}
