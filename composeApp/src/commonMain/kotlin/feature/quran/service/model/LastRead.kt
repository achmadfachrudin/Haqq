package feature.quran.service.model

import AppConstant
import kotlinx.serialization.Serializable

@Serializable
data class LastRead(
    val verseId: Int = AppConstant.DEFAULT_VERSE_ID,
    val verseNumber: Int = AppConstant.DEFAULT_VERSE_NUMBER,
    val chapterId: Int = AppConstant.DEFAULT_CHAPTER_ID,
    val chapterNameSimple: String = AppConstant.DEFAULT_CHAPTER_NAME,
    val progressFloat: Float = AppConstant.DEFAULT_PROGRESS_FLOAT,
    val progressInt: Int = AppConstant.DEFAULT_PROGRESS_INT,
)
