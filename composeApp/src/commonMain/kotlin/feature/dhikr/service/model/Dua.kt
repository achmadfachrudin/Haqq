package feature.dhikr.service.model

import kotlinx.serialization.Serializable

@Serializable
data class Dua(
    val id: Int,
    val title: String,
    val textArabic: String,
    val textTransliteration: String,
    val textTranslation: String,
    val hadith: String,
    val tags: List<String>,
)
