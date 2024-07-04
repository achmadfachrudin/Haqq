package feature.prayertime.service.model

import kotlinx.serialization.Serializable

@Serializable
data class Guidance(
    val position: Int,
    val type: GuidanceType,
    val image: String,
    val title: String,
    val desc: String,
    val textArabic: String,
    val textTransliteration: String,
    val textTranslation: String,
    val hadith: String,
)
