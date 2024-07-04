package feature.quran.service.source.remote

import core.data.ApiResponse
import feature.other.service.model.AppSetting
import feature.quran.service.entity.ChaptersEntity
import feature.quran.service.entity.IndopakEntity
import feature.quran.service.entity.JuzsEntity
import feature.quran.service.entity.PageEntity
import feature.quran.service.entity.TranslationsEntity
import feature.quran.service.entity.UthmaniEntity
import feature.quran.service.entity.UthmaniTajweedEntity
import feature.quran.service.entity.VersesEntity

interface QuranRemote {
    suspend fun fetchChapters(language: AppSetting.Language): ApiResponse<ChaptersEntity>

    suspend fun fetchJuzs(): ApiResponse<JuzsEntity>

    suspend fun fetchPages(): ApiResponse<List<PageEntity>>

    suspend fun fetchVersesByChapter(
        chapterNumber: Int,
        translations: String,
    ): ApiResponse<VersesEntity>

    suspend fun fetchVersesByPage(pageNumber: Int): ApiResponse<VersesEntity>

    suspend fun fetchIndopak(chapterNumber: Int): ApiResponse<IndopakEntity>

    suspend fun fetchUthmani(chapterNumber: Int): ApiResponse<UthmaniEntity>

    suspend fun fetchUthmaniTajweed(chapterNumber: Int): ApiResponse<UthmaniTajweedEntity>

    suspend fun fetchLatin(chapterNumber: Int): ApiResponse<UthmaniTajweedEntity>

    suspend fun fetchTranslations(
        language: AppSetting.Language,
        chapterNumber: Int,
    ): ApiResponse<TranslationsEntity>
}
