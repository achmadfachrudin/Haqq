package feature.study.service.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class NoteRealm(
    @PrimaryKey
    var id: Int = 1,
    var title: String = "",
    var text: String = "",
    var kitab: String = "",
    var speaker: String = "",
    var createdAt: Long = 0,
    var studyAt: Long = 0,
)
