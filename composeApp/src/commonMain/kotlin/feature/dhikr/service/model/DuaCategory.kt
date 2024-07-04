package feature.dhikr.service.model

import kotlinx.serialization.Serializable

@Serializable
data class DuaCategory(
    val tag: String,
    val title: String,
)
