package feature.prayertime.service.model

import kotlinx.serialization.Serializable

@Serializable
enum class GuidanceType(
    val table: String,
) {
    WUDHU("guidance_wudhu"),
    TAYAMMUM("guidance_tayammum"),
    JUNUB("guidance_junub"),
    SALAH("guidance_salah"),
    FASTING("guidance_fasting"),
    ZAKAT_FITRAH("guidance_zakat_fitrah"),
    ZAKAT_MAL("guidance_zakat_mal"),
    HAJJ("guidance_hajj"),
}
