package feature.dhikr.service.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class DuaRoom(
    @PrimaryKey
    var id: Int = 0,
    var titleId: String = "",
    var titleEn: String = "",
    var textIndopak: String = "",
    var textUthmani: String = "",
    var textTransliteration: String = "",
    var textTranslationId: String = "",
    var textTranslationEn: String = "",
    var hadithId: String = "",
    var hadithEn: String = "",
    var tag: String = "",
)
