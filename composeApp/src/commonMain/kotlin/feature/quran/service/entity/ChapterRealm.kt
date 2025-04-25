package feature.quran.service.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ChapterRealm(
    @PrimaryKey
    var id: Int = 0,
    var revelationPlace: String = "",
    var revelationOrder: Int = 0,
    var bismillahPre: Boolean = false,
    var nameSimple: String = "",
    var nameComplex: String = "",
    var nameArabic: String = "",
    var nameTranslationId: String = "",
    var nameTranslationEn: String = "",
    var versesCount: Int = 0,
    var pages: String = "",
    var isDownloaded: Boolean = false,
)
