package feature.dhikr.service.model

import kotlinx.serialization.Serializable

@Serializable
data class AsmaulHusna(
    val id: Int,
    val textArabic: String,
    val textTransliteration: String,
    val textTranslation: String,
)
