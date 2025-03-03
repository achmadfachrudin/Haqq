package feature.quran.service.source.remote

import core.data.ApiResponse
import core.data.NetworkSource.Quran
import core.data.safeRequest
import feature.other.service.model.AppSetting
import feature.quran.service.entity.ChaptersEntity
import feature.quran.service.entity.IndopakEntity
import feature.quran.service.entity.JuzsEntity
import feature.quran.service.entity.PageEntity
import feature.quran.service.entity.TranslationsEntity
import feature.quran.service.entity.UthmaniEntity
import feature.quran.service.entity.UthmaniTajweedEntity
import feature.quran.service.entity.VersesEntity
import io.ktor.client.HttpClient
import io.ktor.client.request.parameter
import io.ktor.client.request.url
import io.ktor.http.HttpMethod

class QuranRemoteImp(
    private val httpClient: HttpClient,
) : QuranRemote {
    override suspend fun fetchChapters(language: AppSetting.Language): ApiResponse<ChaptersEntity> =
        httpClient.safeRequest {
            method = HttpMethod.Get
            url("chapters")
            parameter(Quran.PARAM_LANGUAGE, language.id)
        }

    override suspend fun fetchJuzs(): ApiResponse<JuzsEntity> =
        httpClient.safeRequest {
            method = HttpMethod.Get
            url("juzs")
        }

    override suspend fun fetchPages(): ApiResponse<List<PageEntity>> =
        httpClient.safeRequest {
            method = HttpMethod.Get
            url("quran_pages")
        }

    override suspend fun fetchVersesByPage(pageNumber: Int): ApiResponse<VersesEntity> =
        httpClient.safeRequest {
            method = HttpMethod.Get
            url("verses/by_page/$pageNumber")
            parameter(Quran.PARAM_PER_PAGE, Quran.DEFAULT_PER_PAGE)
        }

    override suspend fun fetchVersesByChapter(
        chapterNumber: Int,
        translations: String,
    ): ApiResponse<VersesEntity> =
        httpClient.safeRequest {
            method = HttpMethod.Get
            url("verses/by_chapter/$chapterNumber")
            parameter(Quran.PARAM_TRANSLATIONS, translations)
            parameter(Quran.PARAM_PER_PAGE, Quran.DEFAULT_PER_PAGE)
        }

    override suspend fun fetchIndopak(chapterNumber: Int): ApiResponse<IndopakEntity> =
        httpClient.safeRequest {
            method = HttpMethod.Get
            url("quran/verses/${AppSetting.ArabicStyle.INDOPAK.path}")
            parameter(Quran.PARAM_CHAPTER_NUMBER, chapterNumber)
        }

    override suspend fun fetchUthmani(chapterNumber: Int): ApiResponse<UthmaniEntity> =
        httpClient.safeRequest {
            method = HttpMethod.Get
            url("quran/verses/${AppSetting.ArabicStyle.UTHMANI.path}")
            parameter(Quran.PARAM_CHAPTER_NUMBER, chapterNumber)
        }

    override suspend fun fetchUthmaniTajweed(chapterNumber: Int): ApiResponse<UthmaniTajweedEntity> =
        httpClient.safeRequest {
            method = HttpMethod.Get
            url("quran/verses/${AppSetting.ArabicStyle.UTHMANI_TAJWEED.path}")
            parameter(Quran.PARAM_CHAPTER_NUMBER, chapterNumber)
        }

    override suspend fun fetchLatin(chapterNumber: Int): ApiResponse<UthmaniTajweedEntity> =
        httpClient.safeRequest {
            method = HttpMethod.Get
            url("quran/verses/${AppSetting.ArabicStyle.UTHMANI_TAJWEED.path}")
            parameter(Quran.PARAM_CHAPTER_NUMBER, chapterNumber)
        }

    override suspend fun fetchTranslations(
        language: AppSetting.Language,
        chapterNumber: Int,
    ): ApiResponse<TranslationsEntity> =
        httpClient.safeRequest {
            method = HttpMethod.Get
            url("quran/translations/${language.translationId}")
            parameter(Quran.PARAM_CHAPTER_NUMBER, chapterNumber)
        }
}
