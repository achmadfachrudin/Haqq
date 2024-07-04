package feature.prayertime.service.entity

import kotlinx.serialization.Serializable

@Serializable
data class HaqqCalendarEntity(
    val data: List<PrayertimeEntity.Data>?,
)
