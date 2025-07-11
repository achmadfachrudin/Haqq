package feature.dhikr.service.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class DhikrRoom(
    @PrimaryKey
    var pkey: String = "",
    var id: Int = 0,
    var type: String = "",
    var titleId: String = "",
    var titleEn: String = "",
    var textIndopak: String = "",
    var textUthmani: String = "",
    var textTransliteration: String = "",
    var textTranslationId: String = "",
    var textTranslationEn: String = "",
    var hadithId: String = "",
    var hadithEn: String = "",
    var maxCount: Int = 0,
    var count: Int = 0,
    var latestUpdate: String = "",
)
