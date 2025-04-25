package feature.home.service.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class HomeTemplateRealm(
    @PrimaryKey
    var position: Int = 0,
    var type: String = "",
    var labelId: String = "",
    var labelEn: String = "",
    var textId: String = "",
    var textEn: String = "",
    var images: String = "",
    var links: String = "",
)
