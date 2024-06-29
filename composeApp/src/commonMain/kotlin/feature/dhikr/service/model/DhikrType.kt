package feature.dhikr.service.model

import kotlinx.serialization.Serializable

@Serializable
enum class DhikrType(
    val table: String,
) {
    MORNING("dhikr_morning"),
    AFTERNOON("dhikr_afternoon"),
    PRAY("dhikr_pray"),
    SLEEP("dhikr_sleep"),
    RUQYAH("dhikr_ruqyah"),
}
