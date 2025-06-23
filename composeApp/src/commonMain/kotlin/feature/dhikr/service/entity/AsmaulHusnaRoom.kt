package feature.dhikr.service.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class AsmaulHusnaRoom(
    @PrimaryKey
    var id: Int = 0,
    var textIndopak: String = "",
    var textUthmani: String = "",
    var textTransliteration: String = "",
    var textTranslationId: String = "",
    var textTranslationEn: String = "",
)
