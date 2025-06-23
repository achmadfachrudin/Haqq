package feature.quran.service.entity

import AppConstant.DEFAULT_CHAPTER_ID
import AppConstant.DEFAULT_CHAPTER_NAME
import AppConstant.DEFAULT_PROGRESS_FLOAT
import AppConstant.DEFAULT_PROGRESS_INT
import AppConstant.DEFAULT_VERSE_ID
import AppConstant.DEFAULT_VERSE_NUMBER
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class LastReadRoom(
    @PrimaryKey
    var id: Int = 0,
    var verseId: Int = DEFAULT_VERSE_ID,
    var verseNumber: Int = DEFAULT_VERSE_NUMBER,
    var chapterId: Int = DEFAULT_CHAPTER_ID,
    var chapterNameSimple: String = DEFAULT_CHAPTER_NAME,
    var progressFloat: Float = DEFAULT_PROGRESS_FLOAT,
    var progressInt: Int = DEFAULT_PROGRESS_INT,
)
