package feature.quran.service.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class VersesEntity(
    @SerialName("verses") val verses: List<VerseEntity>?,
    @SerialName("pagination") val pagination: PaginationEntity?,
) {
    @Serializable
    data class VerseEntity(
        @SerialName("id") val id: Int?,
        @SerialName("verse_number") val verseNumber: Int?,
        @SerialName("verse_key") val verseKey: String?,
        @SerialName("hizb_number") val hizbNumber: Int?,
        @SerialName("rub_el_hizb_number") val rubElHizbNumber: Int?,
        @SerialName("ruku_number") val rukuNumber: Int?,
        @SerialName("manzil_number") val manzilNumber: Int?,
        @SerialName("sajdah_number") val sajdahNumber: Int?,
        @SerialName("page_number") val pageNumber: Int?,
        @SerialName("juz_number") val juzNumber: Int?,
        val translations: List<TranslationEntity>?,
    ) {
        @Serializable
        data class TranslationEntity(
            @SerialName("id") val id: Int?,
            @SerialName("resource_id") val resourceId: Int?,
            @SerialName("text") val text: String?,
        )
    }

    @Serializable
    data class PaginationEntity(
        @SerialName("per_page") val perPage: Int?,
        @SerialName("current_page") val currentPage: Int?,
        @SerialName("next_page") val nextPage: Int?,
        @SerialName("total_pages") val totalPages: Int?,
        @SerialName("total_records") val totalRecords: Int?,
    )
}
