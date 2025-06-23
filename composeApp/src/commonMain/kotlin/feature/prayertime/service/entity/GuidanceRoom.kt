package feature.prayertime.service.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class GuidanceRoom(
    @PrimaryKey
    var pkey: String = "",
    var position: Int = 0,
    var type: String = "",
    var image: String = "",
    var titleId: String = "",
    var titleEn: String = "",
    var descId: String = "",
    var descEn: String = "",
    var textIndopak: String = "",
    var textUthmani: String = "",
    var textTransliteration: String = "",
    var textTranslationId: String = "",
    var textTranslationEn: String = "",
    var hadithId: String = "",
    var hadithEn: String = "",
)
