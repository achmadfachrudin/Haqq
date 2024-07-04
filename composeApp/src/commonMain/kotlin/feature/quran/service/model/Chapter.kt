package feature.quran.service.model

import kotlinx.serialization.Serializable

@Serializable
data class Chapter(
    val id: Int,
    val revelationPlace: String,
    val revelationOrder: Int,
    val bismillahPre: Boolean,
    val nameSimple: String,
    val nameComplex: String,
    val nameArabic: String,
    val nameTranslation: String,
    val versesCount: Int,
    val pages: List<Int>,
    val isDownloaded: Boolean,
)
