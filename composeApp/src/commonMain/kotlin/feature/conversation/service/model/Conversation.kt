package feature.conversation.service.model

import kotlinx.serialization.Serializable

@Serializable
data class Conversation(
    val id: Int,
    val textArabic: String,
    val textTransliteration: String,
    val textTranslation: String,
)
