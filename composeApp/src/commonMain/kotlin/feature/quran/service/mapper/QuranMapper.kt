package feature.quran.service.mapper

import core.util.orZero
import feature.other.service.AppRepository
import feature.other.service.model.AppSetting
import feature.quran.service.entity.ChapterRealm
import feature.quran.service.entity.ChaptersEntity
import feature.quran.service.entity.IndopakEntity
import feature.quran.service.entity.JuzRealm
import feature.quran.service.entity.JuzsEntity
import feature.quran.service.entity.PageEntity
import feature.quran.service.entity.PageRealm
import feature.quran.service.entity.TranslationsEntity
import feature.quran.service.entity.UthmaniEntity
import feature.quran.service.entity.UthmaniTajweedEntity
import feature.quran.service.entity.VerseRealm
import feature.quran.service.entity.VersesEntity
import feature.quran.service.model.Chapter
import feature.quran.service.model.Juz
import feature.quran.service.model.Page
import feature.quran.service.model.Verse
import org.koin.mp.KoinPlatform

internal fun List<ChaptersEntity.ChapterEntity>.mapToRealm(
    translationId: List<String>,
    translationEn: List<String>,
): List<ChapterRealm> =
    mapIndexed { index, entity ->
        entity.mapToRealm(
            translationId = translationId[index],
            translationEn = translationEn[index],
        )
    }

internal fun ChaptersEntity.ChapterEntity.mapToRealm(
    translationId: String,
    translationEn: String,
): ChapterRealm =
    ChapterRealm().apply {
        id = this@mapToRealm.id.orZero()
        revelationPlace = this@mapToRealm.revelationPlace.orEmpty().replaceFirstChar { it.uppercase() }
        revelationOrder = this@mapToRealm.revelationOrder.orZero()
        bismillahPre = this@mapToRealm.bismillahPre ?: false
        nameSimple = this@mapToRealm.nameSimple.orEmpty()
        nameComplex = this@mapToRealm.nameComplex.orEmpty()
        nameArabic = this@mapToRealm.nameArabic.orEmpty()
        nameTranslationId = cleanChapterNameTranslation(translationId)
        nameTranslationEn = cleanChapterNameTranslation(translationEn)
        versesCount = this@mapToRealm.versesCount.orZero()
        pages = this@mapToRealm.pages?.joinToString(",").orEmpty()
        isDownloaded = false
    }

internal fun List<JuzsEntity.JuzEntity>.mapToRealm(): List<JuzRealm> =
    map { entity ->
        entity.mapToRealm()
    }

internal fun ChapterRealm.mapToModel(): Chapter {
    val appRepository = KoinPlatform.getKoin().get<AppRepository>()
    val language = appRepository.getSetting().language

    val translation =
        when (language) {
            AppSetting.Language.ENGLISH -> nameTranslationEn
            AppSetting.Language.INDONESIAN -> nameTranslationId
        }

    return Chapter(
        id = id,
        revelationPlace = revelationPlace,
        revelationOrder = revelationOrder,
        bismillahPre = bismillahPre,
        nameSimple = nameSimple,
        nameComplex = nameComplex,
        nameArabic = nameArabic,
        nameTranslation = translation,
        versesCount = versesCount,
        pages = pages.split(",").map { it.toInt() },
        isDownloaded = isDownloaded,
    )
}

internal fun JuzsEntity.JuzEntity.mapToRealm(): JuzRealm {
    val verseMapping = this@mapToRealm.verseMapping ?: emptyMap()
    verseMapping.entries.sortedBy { it.key.toInt() }

    val chaptersMapping =
        verseMapping.entries.joinToString(",") { entry ->
            entry.key
        }

    var fChapterNumber = 0
    var fVerseNumber = 0
    var lChapterNumber = 0
    var lVerseNumber = 0

    if (verseMapping.isNotEmpty()) {
        // Assuming the verse mapping is sorted and the first key is the first chapter
        val firstEntry = verseMapping.entries.first()
        fChapterNumber = firstEntry.key.toIntOrNull() ?: fChapterNumber
        fVerseNumber = firstEntry.value
            .split("-")
            .first()
            .toIntOrNull() ?: fVerseNumber

        // Assuming the last key is the last chapter
        val lastEntry = verseMapping.entries.last()
        lChapterNumber = lastEntry.key.toIntOrNull() ?: lChapterNumber
        lVerseNumber = lastEntry.value
            .split("-")
            .last()
            .toIntOrNull() ?: lVerseNumber
    }

    return JuzRealm().apply {
        id = this@mapToRealm.id.orZero()
        juzNumber = this@mapToRealm.juzNumber.orZero()
        chapters = chaptersMapping // "2,3"
        firstChapterNumber = fChapterNumber
        firstVerseNumber = fVerseNumber
        firstVerseId = this@mapToRealm.firstVerseId.orZero()
        lastChapterNumber = lChapterNumber
        lastVerseNumber = lVerseNumber
        lastVerseId = this@mapToRealm.lastVerseId.orZero()
        versesCount = this@mapToRealm.versesCount.orZero()
    }
}

internal fun JuzRealm.mapToModel(): Juz {
    val chapterIds = chapters.split(",").map { it.toInt() }

    return Juz(
        id = id,
        juzNumber = juzNumber,
        chapterIds = chapterIds,
        firstChapterNumber = firstChapterNumber,
        firstVerseNumber = firstVerseNumber,
        firstVerseId = firstVerseId,
        lastChapterNumber = lastChapterNumber,
        lastVerseNumber = lastVerseNumber,
        lastVerseId = lastVerseId,
        versesCount = versesCount,
    )
}

internal fun PageEntity.mapToRealm(): PageRealm =
    PageRealm().apply {
        id = this@mapToRealm.id.orZero()
        pageNumber = this@mapToRealm.pageNumber.orZero()
        chapters = this@mapToRealm.chapters.orEmpty()
        firstChapterNumber = this@mapToRealm.firstChapterNumber.orZero()
        firstVerseNumber = this@mapToRealm.firstVerseNumber.orZero()
        firstVerseId = this@mapToRealm.firstVerseId.orZero()
        lastChapterNumber = this@mapToRealm.lastChapterNumber.orZero()
        lastVerseNumber = this@mapToRealm.lastVerseNumber.orZero()
        lastVerseId = this@mapToRealm.lastVerseId.orZero()
        versesCount = this@mapToRealm.versesCount.orZero()
    }

internal fun PageRealm.mapToModel(): Page {
    val chapterIds = chapters.split(",").map { it.toInt() }

    return Page(
        id = id,
        pageNumber = pageNumber,
        chapterIds = chapterIds,
        firstChapterNumber = firstChapterNumber,
        firstVerseNumber = firstVerseNumber,
        firstVerseId = firstVerseId,
        lastChapterNumber = lastChapterNumber,
        lastVerseNumber = lastVerseNumber,
        lastVerseId = lastVerseId,
        versesCount = versesCount,
    )
}

internal fun VersesEntity.mapToRealm(
    indopak: List<String>,
    uthmani: List<String>,
    uthmaniTajweed: List<String>,
    transliteration: List<String>,
): List<VerseRealm> =
    verses?.mapIndexed { index, entity ->
        entity.mapToRealm(
            indopak = indopak[index],
            uthmani = uthmani[index],
            uthmaniTajweed = uthmaniTajweed[index],
            transliteration = transliteration[index],
        )
    } ?: listOf()

internal fun VersesEntity.VerseEntity.mapToRealm(
    indopak: String,
    uthmani: String,
    uthmaniTajweed: String,
    transliteration: String,
): VerseRealm {
    val translationId =
        translations
            ?.first { it.resourceId == AppSetting.Language.INDONESIAN.translation }
            ?.text
            .orEmpty()
    val translationEn =
        translations
            ?.first { it.resourceId == AppSetting.Language.ENGLISH.translation }
            ?.text
            .orEmpty()

    return VerseRealm().apply {
        id = this@mapToRealm.id.orZero()
        chapterId =
            this@mapToRealm
                .verseKey
                .orEmpty()
                .split(":")
                .first()
                .toInt()
        verseNumber = this@mapToRealm.verseNumber.orZero()
        verseKey = this@mapToRealm.verseKey.orEmpty()
        hizbNumber = this@mapToRealm.hizbNumber.orZero()
        rubElHizbNumber = this@mapToRealm.rubElHizbNumber.orZero()
        rukuNumber = this@mapToRealm.rukuNumber.orZero()
        manzilNumber = this@mapToRealm.manzilNumber.orZero()
        sajdahNumber = this@mapToRealm.sajdahNumber.orZero()
        pageNumber = this@mapToRealm.pageNumber.orZero()
        juzNumber = this@mapToRealm.juzNumber.orZero()
        textIndopak = indopak
        textUthmani = uthmani
        textUthmaniTajweed = uthmaniTajweed
        textTransliteration = transliteration
        textTranslationId = cleanVerseTranslation(translationId)
        textTranslationEn = cleanVerseTranslation(translationEn)
    }
}

internal fun VerseRealm.mapToModel(setting: AppSetting): Verse {
    val textArabic =
        when (setting.arabicStyle) {
            AppSetting.ArabicStyle.INDOPAK -> textIndopak
            AppSetting.ArabicStyle.UTHMANI -> textUthmani
            AppSetting.ArabicStyle.UTHMANI_TAJWEED -> textUthmaniTajweed
        }

    val textTranslation =
        when (setting.language) {
            AppSetting.Language.ENGLISH -> textTranslationEn
            AppSetting.Language.INDONESIAN -> textTranslationId
        }

    return Verse(
        id = id,
        chapterId = chapterId,
        verseNumber = verseNumber,
        verseKey = verseKey,
        hizbNumber = hizbNumber,
        rubElHizbNumber = rubElHizbNumber,
        rukuNumber = rukuNumber,
        manzilNumber = manzilNumber,
        sajdahNumber = sajdahNumber,
        pageNumber = pageNumber,
        juzNumber = juzNumber,
        textArabic = textArabic,
        textTransliteration = textTransliteration,
        textTranslation = textTranslation,
    )
}

internal fun IndopakEntity.mapToListString(): List<String> = verses?.map { it.textIndopak.orEmpty() } ?: listOf()

internal fun UthmaniEntity.mapToListString(): List<String> = verses?.map { it.textUthmani.orEmpty() } ?: listOf()

internal fun UthmaniTajweedEntity.mapToListString(): List<String> = verses?.map { it.textUthmaniTajweed.orEmpty() } ?: listOf()

internal fun TranslationsEntity.mapToListString(): List<String> = translations?.map { it.text.orEmpty() } ?: listOf()

private fun cleanChapterNameTranslation(input: String): String = input.replace("\\", "")

private fun cleanVerseTranslation(input: String): String {
    val newInput = input.replace("<sup ", "</sup>")
    val words =
        newInput
            .split("</sup>")
            .filter { !it.contains("<") && !it.contains(">") && !it.contains("=") }

    return words
        .joinToString("")
        .trim()
        .replace("1", "")
        .replace("2", "")
        .replace("3", "")
        .replace("4", "")
}
