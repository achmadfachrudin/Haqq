package feature.quran.service.entity

import AppConstant.DEFAULT_CHAPTER_ID
import AppConstant.DEFAULT_CHAPTER_NAME
import AppConstant.DEFAULT_PROGRESS_FLOAT
import AppConstant.DEFAULT_PROGRESS_INT
import AppConstant.DEFAULT_VERSE_ID
import AppConstant.DEFAULT_VERSE_NUMBER
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

class VerseRealm : RealmObject {
    @PrimaryKey
    var id: Int = 0
    var chapterId: Int = 0
    var verseNumber: Int = 0
    var verseKey: String = "0:0"
    var hizbNumber: Int = 0
    var rubElHizbNumber: Int = 0
    var rukuNumber: Int = 0
    var manzilNumber: Int = 0
    var sajdahNumber: Int = 0
    var pageNumber: Int = 0
    var juzNumber: Int = 0
    var textIndopak: String = ""
    var textUthmani: String = ""
    var textUthmaniTajweed: String = ""
    var textTransliteration: String = ""
    var textTranslationId: String = ""
    var textTranslationEn: String = ""
}

class VerseFavoriteRealm : RealmObject {
    @PrimaryKey
    var verseId: Int = DEFAULT_VERSE_ID
    var verseNumber: Int = DEFAULT_VERSE_NUMBER
    var chapterId: Int = DEFAULT_CHAPTER_ID
    var chapterNameSimple: String = DEFAULT_CHAPTER_NAME
}

class LastReadRealm : RealmObject {
    @PrimaryKey
    var id: Int = 0
    var verseId: Int = DEFAULT_VERSE_ID
    var verseNumber: Int = DEFAULT_VERSE_NUMBER
    var chapterId: Int = DEFAULT_CHAPTER_ID
    var chapterNameSimple: String = DEFAULT_CHAPTER_NAME
    var progressFloat: Float = DEFAULT_PROGRESS_FLOAT
    var progressInt: Int = DEFAULT_PROGRESS_INT
}
