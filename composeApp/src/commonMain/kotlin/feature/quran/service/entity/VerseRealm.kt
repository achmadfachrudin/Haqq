package feature.quran.service.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class VerseRealm(
    @PrimaryKey
    var id: Int = 0,
    var chapterId: Int = 0,
    var verseNumber: Int = 0,
    var verseKey: String = "0:0",
    var hizbNumber: Int = 0,
    var rubElHizbNumber: Int = 0,
    var rukuNumber: Int = 0,
    var manzilNumber: Int = 0,
    var sajdahNumber: Int = 0,
    var pageNumber: Int = 0,
    var juzNumber: Int = 0,
    var textIndopak: String = "",
    var textUthmani: String = "",
    var textUthmaniTajweed: String = "",
    var textTransliteration: String = "",
    var textTranslationId: String = "",
    var textTranslationEn: String = "",
)
