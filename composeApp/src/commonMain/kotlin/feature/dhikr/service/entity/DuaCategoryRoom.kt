package feature.dhikr.service.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class DuaCategoryRoom(
    @PrimaryKey
    var tag: String = "",
    var titleId: String = "",
    var titleEn: String = "",
)
