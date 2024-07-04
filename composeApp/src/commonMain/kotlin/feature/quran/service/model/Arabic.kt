package feature.quran.service.model

import kotlinx.serialization.Serializable

@Serializable
data class Arabic(
    val indopak: List<String>,
    val uthmani: List<String>,
    val uthmaniTajweed: List<String>,
)
