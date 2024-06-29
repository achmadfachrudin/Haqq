package feature.study.service.model

import kotlinx.serialization.Serializable

@Serializable
data class Note(
    val id: Int = 0,
    val title: String = "",
    val text: String = "",
    val kitab: String = "",
    val speaker: String = "",
    val createdAt: Long = 0,
    val studyAt: Long = 0,
)
