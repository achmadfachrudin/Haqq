package feature.quran.service.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ChaptersEntity(
    @SerialName("chapters") val chapters: List<ChapterEntity>?,
) {
    /**
     *    {
     *       "id": 1,
     *       "revelation_place": "makkah",
     *       "revelation_order": 5,
     *       "bismillah_pre": false,
     *       "name_simple": "Al-Fatihah",
     *       "name_complex": "Al-Fātiĥah",
     *       "name_arabic": "الفاتحة",
     *       "verses_count": 7,
     *       "pages": [
     *         1,
     *         1
     *       ],
     *       "translated_name": {
     *         "language_name": "indonesian",
     *         "name": "Pembukaan"
     *       }
     *     }
     */
    @Serializable
    data class ChapterEntity(
        @SerialName("id") val id: Int?,
        @SerialName("revelation_place") val revelationPlace: String?,
        @SerialName("revelation_order") val revelationOrder: Int?,
        @SerialName("bismillah_pre") val bismillahPre: Boolean?,
        @SerialName("name_simple") val nameSimple: String?,
        @SerialName("name_complex") val nameComplex: String?,
        @SerialName("name_arabic") val nameArabic: String?,
        @SerialName("verses_count") val versesCount: Int?,
        @SerialName("pages") val pages: List<Int>?,
        @SerialName("translated_name") val translatedName: TranslatedNameEntity?,
    ) {
        @Serializable
        data class TranslatedNameEntity(
            @SerialName("language_name") val languageName: String?,
            @SerialName("name") val name: String?,
        )
    }
}
