package feature.quran.service.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class JuzRoom(
    @PrimaryKey
    var id: Int = 0,
    var juzNumber: Int = 0,
    var chapters: String = "",
    var firstChapterNumber: Int = 0,
    var firstVerseNumber: Int = 0,
    var firstVerseId: Int = 0,
    var lastChapterNumber: Int = 0,
    var lastVerseNumber: Int = 0,
    var lastVerseId: Int = 0,
    var versesCount: Int = 0,
)
