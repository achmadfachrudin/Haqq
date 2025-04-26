package feature.quran.service.entity

import AppConstant.DEFAULT_CHAPTER_ID
import AppConstant.DEFAULT_CHAPTER_NAME
import AppConstant.DEFAULT_VERSE_ID
import AppConstant.DEFAULT_VERSE_NUMBER
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class VerseFavoriteRoom(
    @PrimaryKey
    var verseId: Int = DEFAULT_VERSE_ID,
    var verseNumber: Int = DEFAULT_VERSE_NUMBER,
    var chapterId: Int = DEFAULT_CHAPTER_ID,
    var chapterNameSimple: String = DEFAULT_CHAPTER_NAME,
)
