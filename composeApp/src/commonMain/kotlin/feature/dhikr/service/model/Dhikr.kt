package feature.dhikr.service.model

import kotlinx.serialization.Serializable

@Serializable
data class Dhikr(
    val id: Int,
    val title: String,
    val textArabic: String,
    val textTransliteration: String,
    val textTranslation: String,
    val hadith: String,
    val maxCount: Int,
    val count: Int = 0,
)
