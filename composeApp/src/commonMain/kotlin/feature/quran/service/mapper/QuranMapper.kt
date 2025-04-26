package feature.quran.service.mapper

import core.util.orZero
import feature.other.service.AppRepository
import feature.other.service.model.AppSetting
import feature.quran.service.entity.ChapterRoom
import feature.quran.service.entity.ChaptersEntity
import feature.quran.service.entity.IndopakEntity
import feature.quran.service.entity.JuzRoom
import feature.quran.service.entity.JuzsEntity
import feature.quran.service.entity.PageEntity
import feature.quran.service.entity.PageRoom
import feature.quran.service.entity.TranslationsEntity
import feature.quran.service.entity.UthmaniEntity
import feature.quran.service.entity.UthmaniTajweedEntity
import feature.quran.service.entity.VerseRoom
import feature.quran.service.entity.VersesEntity
import feature.quran.service.model.Chapter
import feature.quran.service.model.Juz
import feature.quran.service.model.Page
import feature.quran.service.model.Verse
import org.koin.mp.KoinPlatform

internal fun List<ChaptersEntity.ChapterEntity>.mapToRoom(
    translationId: List<String>,
    translationEn: List<String>,
): List<ChapterRoom> =
    mapIndexed { index, entity ->
        entity.mapToRoom(
            translationId = translationId[index],
            translationEn = translationEn[index],
        )
    }

internal fun ChaptersEntity.ChapterEntity.mapToRoom(
    translationId: String,
    translationEn: String,
): ChapterRoom =
    ChapterRoom(
        id = id.orZero(),
        revelationPlace = revelationPlace.orEmpty().replaceFirstChar { it.uppercase() },
        revelationOrder = revelationOrder.orZero(),
        bismillahPre = bismillahPre ?: false,
        nameSimple = nameSimple.orEmpty(),
        nameComplex = nameComplex.orEmpty(),
        nameArabic = nameArabic.orEmpty(),
        nameTranslationId = cleanChapterNameTranslation(translationId),
        nameTranslationEn = cleanChapterNameTranslation(translationEn),
        versesCount = versesCount.orZero(),
        pages = pages?.joinToString(",").orEmpty(),
        isDownloaded = false,
    )

internal fun List<JuzsEntity.JuzEntity>.mapToRoom(): List<JuzRoom> =
    map { entity ->
        entity.mapToRoom()
    }

internal fun ChapterRoom.mapToModel(): Chapter {
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

internal fun JuzsEntity.JuzEntity.mapToRoom(): JuzRoom {
    val verseMapping = verseMapping ?: emptyMap()
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

    return JuzRoom(
        id = id.orZero(),
        juzNumber = juzNumber.orZero(),
        chapters = chaptersMapping, // "2,3"
        firstChapterNumber = fChapterNumber,
        firstVerseNumber = fVerseNumber,
        firstVerseId = firstVerseId.orZero(),
        lastChapterNumber = lChapterNumber,
        lastVerseNumber = lVerseNumber,
        lastVerseId = lastVerseId.orZero(),
        versesCount = versesCount.orZero(),
    )
}

internal fun JuzRoom.mapToModel(): Juz {
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

internal fun PageEntity.mapToRoom(): PageRoom =
    PageRoom(
        id = id.orZero(),
        pageNumber = pageNumber.orZero(),
        chapters = chapters.orEmpty(),
        firstChapterNumber = firstChapterNumber.orZero(),
        firstVerseNumber = firstVerseNumber.orZero(),
        firstVerseId = firstVerseId.orZero(),
        lastChapterNumber = lastChapterNumber.orZero(),
        lastVerseNumber = lastVerseNumber.orZero(),
        lastVerseId = lastVerseId.orZero(),
        versesCount = versesCount.orZero(),
    )

internal fun PageRoom.mapToModel(): Page {
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

internal fun VersesEntity.mapToRoom(
    indopak: List<String>,
    uthmani: List<String>,
    uthmaniTajweed: List<String>,
    transliteration: List<String>,
): List<VerseRoom> =
    verses?.mapIndexed { index, entity ->
        entity.mapToRoom(
            indopak = indopak[index],
            uthmani = uthmani[index],
            uthmaniTajweed = uthmaniTajweed[index],
            transliteration = transliteration[index],
        )
    } ?: listOf()

internal fun VersesEntity.VerseEntity.mapToRoom(
    indopak: String,
    uthmani: String,
    uthmaniTajweed: String,
    transliteration: String,
): VerseRoom {
    val translationId =
        translations
            ?.first { it.resourceId == AppSetting.Language.INDONESIAN.translationId }
            ?.text
            .orEmpty()
    val translationEn =
        translations
            ?.first { it.resourceId == AppSetting.Language.ENGLISH.translationId }
            ?.text
            .orEmpty()

    return VerseRoom(
        id = id.orZero(),
        chapterId =
            verseKey
                .orEmpty()
                .split(":")
                .first()
                .toInt(),
        verseNumber = verseNumber.orZero(),
        verseKey = verseKey.orEmpty(),
        hizbNumber = hizbNumber.orZero(),
        rubElHizbNumber = rubElHizbNumber.orZero(),
        rukuNumber = rukuNumber.orZero(),
        manzilNumber = manzilNumber.orZero(),
        sajdahNumber = sajdahNumber.orZero(),
        pageNumber = pageNumber.orZero(),
        juzNumber = juzNumber.orZero(),
        textIndopak = indopak,
        textUthmani = uthmani,
        textUthmaniTajweed = uthmaniTajweed,
        textTransliteration = transliteration,
        textTranslationId = cleanVerseTranslation(translationId),
        textTranslationEn = cleanVerseTranslation(translationEn),
    )
}

internal fun VerseRoom.mapToModel(setting: AppSetting): Verse {
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
