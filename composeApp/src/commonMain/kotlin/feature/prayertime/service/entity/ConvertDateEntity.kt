package feature.prayertime.service.entity

import kotlinx.serialization.Serializable

@Serializable
data class ConvertDateEntity(
    val data: PrayertimeEntity.Data.Date?,
)
