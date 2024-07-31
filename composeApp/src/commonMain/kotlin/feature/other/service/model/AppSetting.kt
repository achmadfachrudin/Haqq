package feature.other.service.model

import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

@Serializable
data class AppSetting(
    val language: Language,
    val theme: Theme,
    val themeColor: ThemeColor,
    val arabicStyle: ArabicStyle,
    val arabicFontSize: Int,
    val transliterationVisibility: Boolean,
    val transliterationFontSize: Int,
    val translationVisibility: Boolean,
    val translationFontSize: Int,
    val location: Location,
    val acceptPrivacyPolicy: Boolean,
    val lastUpdate: LocalDate,
) {
    enum class Language(
        val display: String,
        val id: String,
        val translation: Int,
    ) {
        ENGLISH("English", "en", 131), // Dr. Mustafa Khattab, The Clear Quran
        INDONESIAN("Bahasa", "id", 134), //
    }

    enum class Theme(
        val display: String,
    ) {
        AUTO("Auto"),
        LIGHT("Light"),
        DARK("Dark"),
    }

    enum class ThemeColor(
        val display: String,
    ) {
        GREEN("Green"),
        BLUE("Blue"),
        PURPLE("Purple"),
        RED("Red"),
        PINK("Pink"),
    }

    enum class ArabicStyle(
        val display: String,
        val path: String,
    ) {
        INDOPAK("Indopak", "indopak"),
        UTHMANI("Uthmani", "uthmani"),
        UTHMANI_TAJWEED("Uthmani Tajweed", "uthmani_tajweed"),
    }

    val isLocationValid: Boolean
        get() =
            location.latitude != 0.0 &&
                location.longitude != 0.0 &&
                location.name.isNotEmpty()

    @Serializable
    data class Location(
        val latitude: Double,
        val longitude: Double,
        val name: String,
    )
}
