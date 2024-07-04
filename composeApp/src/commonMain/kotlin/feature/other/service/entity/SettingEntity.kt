package feature.other.service.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SettingEntity(
    @SerialName("id") val id: Int,
    @SerialName("days_to_update") val daysToUpdate: Int,
    @SerialName("version_name") val versionName: String,
    @SerialName("version_code") val versionCode: Int,
    @SerialName("url_update") val urlUpdate: String,
)
