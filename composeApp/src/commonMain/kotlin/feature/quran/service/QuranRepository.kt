package feature.quran.service

import core.data.ApiResponse
import core.data.DataState
import core.util.orZero
import data.AppDatabase
import feature.other.service.AppRepository
import feature.other.service.model.AppSetting.Language
import feature.quran.service.entity.ChaptersEntity
import feature.quran.service.entity.LastReadRoom
import feature.quran.service.entity.PageEntity
import feature.quran.service.entity.VerseFavoriteRoom
import feature.quran.service.mapper.mapToListString
import feature.quran.service.mapper.mapToModel
import feature.quran.service.mapper.mapToRoom
import feature.quran.service.model.Chapter
import feature.quran.service.model.Juz
import feature.quran.service.model.LastRead
import feature.quran.service.model.Page
import feature.quran.service.model.QuranConstant.MAX_CHAPTER
import feature.quran.service.model.QuranConstant.MAX_JUZ
import feature.quran.service.model.QuranConstant.MAX_PAGE
import feature.quran.service.model.Verse
import feature.quran.service.model.VerseFavorite
import feature.quran.service.source.local.QuranArchiveSource
import feature.quran.service.source.remote.QuranRemote
import io.github.aakira.napier.Napier
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class QuranRepository(
    private val appRepository: AppRepository,
    private val remote: QuranRemote,
    private val database: AppDatabase,
) {
    fun checkIsAllDownloaded(): Boolean =
        runBlocking {
            withContext(Dispatchers.IO) {
                val latestChapters =
                    database
                        .chapterDao()
                        .getAll()
                        .map { it.mapToModel() }

                latestChapters.all { chapter -> chapter.isDownloaded }
            }
        }

    fun fetchChapters() =
        flow {
            val localChapters = database.chapterDao().getAll()

            if (localChapters.size < MAX_CHAPTER) {
                database.chapterDao().deleteAll()

                val languages = Language.entries.toTypedArray()
                val chapters = mutableListOf<ChaptersEntity.ChapterEntity>()
                val translationId = mutableListOf<String>()
                val translationEn = mutableListOf<String>()
                languages.forEachIndexed { index, language ->
                    when (val result = remote.fetchChapters(language)) {
                        is ApiResponse.Error -> emit(DataState.Error(result.message))
                        is ApiResponse.Success -> {
                            val remoteChapters = result.body.chapters.orEmpty()

                            when (language) {
                                Language.ENGLISH -> {
                                    translationEn.addAll(remoteChapters.map { it.translatedName?.name.orEmpty() })
                                }

                                Language.INDONESIAN -> {
                                    translationId.addAll(remoteChapters.map { it.translatedName?.name.orEmpty() })
                                }
                            }

                            if (index == languages.lastIndex) {
                                chapters.addAll(remoteChapters.toMutableList())
                            }
                        }
                    }
                }

                val finalChapters =
                    chapters.mapToRoom(
                        translationId = translationId,
                        translationEn = translationEn,
                    )

                database.chapterDao().insert(finalChapters)

                downloadVerses(1).collect()
            }

            val latestChapters = database.chapterDao().getAll().map { it.mapToModel() }
            emit(DataState.Success(latestChapters))
        }.onStart { emit(DataState.Loading) }
            .catch { emit(DataState.Error(it.message.orEmpty())) }
            .flowOn(Dispatchers.IO)

    fun fetchJuzs() =
        flow {
            val localJuzs = database.juzDao().getAll()

            if (localJuzs.size < MAX_JUZ) {
                database.juzDao().deleteAll()

                when (val result = remote.fetchJuzs()) {
                    is ApiResponse.Error -> emit(DataState.Error(result.message))
                    is ApiResponse.Success -> {
                        val remoteResult =
                            result.body.juzs
                                .orEmpty()
                                .filter { it.id.orZero() <= MAX_JUZ }
                                .distinctBy { it.juzNumber }
                                .mapToRoom()

                        database.juzDao().insert(remoteResult)

                        val latestJuzs = database.juzDao().getAll().map { it.mapToModel() }

                        emit(DataState.Success(latestJuzs))
                    }
                }
            } else {
                val latestJuzs = database.juzDao().getAll().map { it.mapToModel() }

                emit(DataState.Success(latestJuzs))
            }
        }.onStart { emit(DataState.Loading) }
            .catch { emit(DataState.Error(it.message.orEmpty())) }
            .flowOn(Dispatchers.IO)

    fun addPages() =
        flow {
            database.pageDao().deleteAll()

            val pageEntities = mutableListOf<PageEntity>()
            for (i in 1..MAX_PAGE) {
                try {
                    val localVerse = database.verseDao().getAll()
                    val verses = localVerse.filter { it.pageNumber == i }

                    val firstVerse = verses.minBy { it.id }

                    pageEntities.add(
                        PageEntity(
                            id = i,
                            pageNumber = i,
                            chapters = "0",
                            firstChapterNumber = firstVerse.chapterId,
                            firstVerseNumber = firstVerse.verseNumber,
                            firstVerseId = firstVerse.id,
                            lastChapterNumber = 0,
                            lastVerseNumber = 0,
                            lastVerseId = 0,
                            versesCount = verses.size,
                        ),
                    )
                } catch (e: Exception) {
                    Napier.e { "Error retrieving firstVerse" }
                    throw e
                }
            }

            database.pageDao().insert(pageEntities.map { it.mapToRoom() })
            emit(true)
        }.flowOn(Dispatchers.IO)

    fun fetchPages() =
        flow {
            val localPages = database.pageDao().getAll()

            if (localPages.isEmpty() && checkIsAllDownloaded()) {
                addPages().last()
            }

            val latestPages = database.pageDao().getAll().map { it.mapToModel() }

            emit(latestPages)
        }.flowOn(Dispatchers.IO)

    fun getChapterById(chapterId: Int): Chapter =
        runBlocking {
            withContext(Dispatchers.IO) {
                try {
                    database
                        .chapterDao()
                        .loadAllById(listOf(chapterId))
                        .first()
                        .mapToModel()
                } catch (e: Exception) {
                    Napier.e { "Error retrieving getChapterById chapterId $chapterId" }
                    throw e
                }
            }
        }

    fun getJuzById(juzId: Int): Juz =
        runBlocking {
            withContext(Dispatchers.IO) {
                try {
                    database
                        .juzDao()
                        .loadAllById(listOf(juzId))
                        .first()
                        .mapToModel()
                } catch (e: Exception) {
                    Napier.e { "Error retrieving getJuzById juzId $juzId" }
                    throw e
                }
            }
        }

    fun getPageById(pageId: Int): Page =
        runBlocking {
            withContext(Dispatchers.IO) {
                try {
                    database
                        .pageDao()
                        .loadAllById(listOf(pageId))
                        .first()
                        .mapToModel()
                } catch (e: Exception) {
                    Napier.e { "Error retrieving getPageById pageId $pageId" }
                    throw e
                }
            }
        }

    fun getVerseById(verseId: Int): Verse =
        runBlocking {
            withContext(Dispatchers.IO) {
                try {
                    val setting = appRepository.getSetting()

                    database
                        .verseDao()
                        .loadAllById(listOf(verseId))
                        .first()
                        .mapToModel(setting)
                } catch (e: Exception) {
                    Napier.e { "Error retrieving getVerseById verseId $verseId" }
                    throw e
                }
            }
        }

    fun getChapterNameSimple(chapterId: Int): String =
        runBlocking {
            withContext(Dispatchers.IO) {
                try {
                    database
                        .chapterDao()
                        .loadAllById(listOf(chapterId))
                        .first()
                        .nameSimple
                } catch (e: Exception) {
                    Napier.e { "Error retrieving getChapterName chapterId $chapterId" }
                    throw e
                }
            }
        }

    fun getRandomVerse(): Verse =
        runBlocking {
            withContext(Dispatchers.IO) {
                try {
                    val setting = appRepository.getSetting()

                    database
                        .verseDao()
                        .getAll()
                        .random()
                        .mapToModel(setting)
                } catch (e: Exception) {
                    Napier.e { "Error retrieving getRandomVerse" }
                    throw e
                }
            }
        }

    private fun updateChapterIsDownloaded(chapterNumber: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            val current = database.chapterDao().loadAllById(listOf(chapterNumber)).first()
            val updated =
                current.copy(
                    isDownloaded = true,
                )

            database.chapterDao().update(updated)
        }
    }

    fun isChapterDownloaded(chapterId: Int): Boolean =
        runBlocking {
            withContext(Dispatchers.IO) {
                try {
                    database.chapterDao().loadAllById(listOf(chapterId)).isNotEmpty()
                } catch (e: Exception) {
                    Napier.e { "Error retrieving isChapterDownloaded chapterId $chapterId" }
                    false
                }
            }
        }

    fun isVerseDownloaded(verseId: Int): Boolean =
        runBlocking {
            withContext(Dispatchers.IO) {
                try {
                    database.verseDao().loadAllById(listOf(verseId)).isNotEmpty()
                } catch (e: Exception) {
                    Napier.e { "Error retrieving isVerseDownloaded verseId $verseId" }
                    false
                }
            }
        }

    fun downloadVerses(chapterNumber: Int) =
        flow {
            val localVerses = database.verseDao().getAll().filter { it.chapterId == chapterNumber }
            val translations = Language.entries.map { it.translationId }.joinToString(",")

            if (localVerses.isEmpty()) {
                when (
                    val result =
                        remote.fetchVersesByChapter(
                            chapterNumber = chapterNumber,
                            translations = translations,
                        )
                ) {
                    is ApiResponse.Error -> emit(DataState.Error(result.message))
                    is ApiResponse.Success -> {
                        val indopak = fetchIndopakByChapter(chapterNumber).last()
                        val uthmani = fetchUthmaniByChapter(chapterNumber).last()
                        val uthmaniTajweed = fetchUthmaniTajweedByChapter(chapterNumber).last()
                        val transliteration = QuranArchiveSource.latins[chapterNumber - 1]

                        val indopakStatus = indopak.isNotEmpty()
                        val uthmaniStatus = uthmani.isNotEmpty()
                        val uthmaniTajweedStatus = uthmaniTajweed.isNotEmpty()

                        if (!indopakStatus || !uthmaniStatus || !uthmaniTajweedStatus) {
                            emit(DataState.Error("empty"))
                            return@flow
                        } else {
                            val remoteVerses =
                                result.body.mapToRoom(
                                    indopak = indopak,
                                    uthmani = uthmani,
                                    uthmaniTajweed = uthmaniTajweed,
                                    transliteration = transliteration,
                                )

                            database.verseDao().insert(remoteVerses)

                            updateChapterIsDownloaded(chapterNumber)

                            emit(DataState.Success(true))
                        }
                    }
                }
            }
        }.flowOn(Dispatchers.IO)

    fun fetchVersesByChapter(chapterNumber: Int) =
        flow {
            val localVerses = database.verseDao().getAll().filter { it.chapterId == chapterNumber }
            val setting = appRepository.getSetting()
            val translations = Language.entries.map { it.translationId }.joinToString(",")

            if (localVerses.isEmpty()) {
                when (
                    val result =
                        remote.fetchVersesByChapter(
                            chapterNumber = chapterNumber,
                            translations = translations,
                        )
                ) {
                    is ApiResponse.Error -> emit(DataState.Error(result.message))
                    is ApiResponse.Success -> {
                        val indopak = fetchIndopakByChapter(chapterNumber).last()
                        val uthmani = fetchUthmaniByChapter(chapterNumber).last()
                        val uthmaniTajweed = fetchUthmaniTajweedByChapter(chapterNumber).last()
                        val transliteration = QuranArchiveSource.latins[chapterNumber - 1]

                        val remoteVerses =
                            result.body.mapToRoom(
                                indopak = indopak,
                                uthmani = uthmani,
                                uthmaniTajweed = uthmaniTajweed,
                                transliteration = transliteration,
                            )

                        database.verseDao().insert(remoteVerses)

                        updateChapterIsDownloaded(chapterNumber)

                        val latestVerses =
                            database
                                .verseDao()
                                .getAll()
                                .filter { it.chapterId == chapterNumber }
                                .map { it.mapToModel(setting) }
                                .sortedBy { it.verseNumber }

                        emit(DataState.Success(latestVerses))
                    }
                }
            } else {
                val latestVerses =
                    database
                        .verseDao()
                        .getAll()
                        .filter { it.chapterId == chapterNumber }
                        .map { it.mapToModel(setting) }
                        .sortedBy { it.verseNumber }

                emit(DataState.Success(latestVerses))
            }
        }.onStart { emit(DataState.Loading) }
            .catch { emit(DataState.Error(it.message.orEmpty())) }
            .flowOn(Dispatchers.IO)

    fun fetchVersesByJuz(juzNumber: Int) =
        flow {
            val juz = getJuzById(juzNumber)

            // download
            juz.chapterIds.forEach { id ->
                downloadVerses(id).collect()
            }

            val setting = appRepository.getSetting()

            val latestVerses =
                database
                    .verseDao()
                    .getAll()
                    .filter { it.juzNumber == juzNumber }
                    .map { it.mapToModel(setting) }
                    .sortedBy { it.verseNumber }

            val verseGroup = latestVerses.groupBy { it.chapterId }

            val verses = verseGroup.entries.sortedBy { it.key }.flatMap { it.value }

            if (verses.isEmpty()) {
                emit(DataState.Error("empty"))
            } else {
                emit(DataState.Success(verses))
            }
        }.onStart { emit(DataState.Loading) }
            .catch { emit(DataState.Error(it.message.orEmpty())) }
            .flowOn(Dispatchers.IO)

    fun fetchVersesByPage(pageNumber: Int) =
        flow {
            val page = getPageById(pageNumber)

            // download
            page.chapterIds.forEach { id ->
                downloadVerses(id).collect()
            }

            val setting = appRepository.getSetting()

            val latestVerses =
                database
                    .verseDao()
                    .getAll()
                    .filter { it.pageNumber == pageNumber }
                    .map { it.mapToModel(setting) }
                    .sortedBy { it.verseNumber }

            val verseGroup = latestVerses.groupBy { it.chapterId }

            val verses = verseGroup.entries.sortedBy { it.key }.flatMap { it.value }

            if (verses.isEmpty()) {
                emit(DataState.Error("empty"))
            } else {
                emit(DataState.Success(verses))
            }
        }.onStart { emit(DataState.Loading) }
            .catch { emit(DataState.Error(it.message.orEmpty())) }
            .flowOn(Dispatchers.IO)

    fun resetVersesByChapter(chapterNumber: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            database
                .verseDao()
                .getAll()
                .filter { it.chapterId == chapterNumber }
                .let { verses ->
                    database.verseDao().delete(verses)
                }
        }
    }

    fun resetVersesByJuz(juzNumber: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            database
                .verseDao()
                .getAll()
                .filter { it.juzNumber == juzNumber }
                .let { verses ->
                    database.verseDao().delete(verses)
                }
        }
    }

    fun resetVersesByPage(pageNumber: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            database
                .verseDao()
                .getAll()
                .filter { it.pageNumber == pageNumber }
                .let { verses ->
                    database.verseDao().delete(verses)
                }
        }
    }

    fun fetchIndopakByChapter(chapterNumber: Int) =
        flow {
            when (val result = remote.fetchIndopak(chapterNumber)) {
                is ApiResponse.Error -> emit(listOf())
                is ApiResponse.Success -> emit(result.body.mapToListString())
            }
        }.flowOn(Dispatchers.IO)

    fun fetchUthmaniByChapter(chapterNumber: Int) =
        flow {
            when (val result = remote.fetchUthmani(chapterNumber)) {
                is ApiResponse.Error -> emit(listOf())
                is ApiResponse.Success -> emit(result.body.mapToListString())
            }
        }.flowOn(Dispatchers.IO)

    fun fetchUthmaniTajweedByChapter(chapterNumber: Int) =
        flow {
            when (val result = remote.fetchUthmaniTajweed(chapterNumber)) {
                is ApiResponse.Error -> emit(listOf())
                is ApiResponse.Success -> emit(result.body.mapToListString())
            }
        }.flowOn(Dispatchers.IO)

    fun fetchLatinByChapter(chapterNumber: Int) =
        flow {
            when (val result = remote.fetchLatin(chapterNumber)) {
                is ApiResponse.Error -> emit(listOf())
                is ApiResponse.Success -> emit(result.body.mapToListString())
            }
        }.flowOn(Dispatchers.IO)

    fun fetchTranslationsByChapter(
        language: Language,
        chapterNumber: Int,
    ) = flow {
        when (
            val result =
                remote.fetchTranslations(
                    language = language,
                    chapterNumber = chapterNumber,
                )
        ) {
            is ApiResponse.Error -> emit(listOf())
            is ApiResponse.Success -> emit(result.body.mapToListString())
        }
    }.flowOn(Dispatchers.IO)

    fun getLastRead(): LastRead =
        runBlocking {
            withContext(Dispatchers.IO) {
                if (database.lastReadDao().getAll().isEmpty()) {
                    database.lastReadDao().insert(LastReadRoom())
                }

                val lastRead = database.lastReadDao().getAll().first()

                LastRead(
                    chapterId = lastRead.chapterId,
                    chapterNameSimple = lastRead.chapterNameSimple,
                    verseId = lastRead.verseId,
                    verseNumber = lastRead.verseNumber,
                    progressFloat = lastRead.progressFloat,
                    progressInt = lastRead.progressInt,
                )
            }
        }

    fun updateLastRead(verseId: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            val lastRead = database.lastReadDao().getAll().first()
            val verse = getVerseById(verseId)
            val chapter = getChapterById(verse.chapterId)
            val progressFloat = verse.verseNumber.toFloat() / chapter.versesCount.toFloat()
            val progressInt = (progressFloat * 100).toInt()

            val updated =
                lastRead.copy(
                    chapterId = verse.chapterId,
                    chapterNameSimple = chapter.nameSimple,
                    verseId = verse.id,
                    verseNumber = verse.verseNumber,
                    progressFloat = progressFloat,
                    progressInt = progressInt,
                )

            database.lastReadDao().update(updated)
        }
    }

    fun getVerseFavorites(): List<VerseFavorite> =
        runBlocking {
            withContext(Dispatchers.IO) {
                val favorites = database.verseFavoriteDao().getAll()

                favorites
                    .map {
                        VerseFavorite(
                            verseId = it.verseId,
                            verseNumber = it.verseNumber,
                            chapterId = it.chapterId,
                            chapterNameSimple = it.chapterNameSimple,
                        )
                    }.sortedBy { it.verseId }
            }
        }

    fun isFavorite(verseId: Int): Boolean =
        runBlocking {
            withContext(Dispatchers.IO) {
                database.verseFavoriteDao().getAll().any { it.verseId == verseId }
            }
        }

    fun addOrRemoveVerseFavorites(verse: Verse) {
        CoroutineScope(Dispatchers.IO).launch {
            val verseFavorites =
                database
                    .verseFavoriteDao()
                    .getAll()
                    .firstOrNull { it.verseId == verse.id }

            if (verseFavorites == null) {
                val chapter = getChapterById(verse.chapterId)

                database.verseFavoriteDao().insert(
                    VerseFavoriteRoom(
                        verseId = verse.id,
                        verseNumber = verse.verseNumber,
                        chapterId = verse.chapterId,
                        chapterNameSimple = chapter.nameSimple,
                    ),
                )
            } else {
                database.verseFavoriteDao().delete(verseFavorites)
            }
        }
    }

    fun addOrRemoveVerseFavorites(verseFavorite: VerseFavorite) {
        CoroutineScope(Dispatchers.IO).launch {
            val verseFavorites =
                database
                    .verseFavoriteDao()
                    .getAll()
                    .firstOrNull { it.verseId == verseFavorite.verseId }

            if (verseFavorites == null) {
                database.verseFavoriteDao().insert(
                    VerseFavoriteRoom(
                        verseId = verseFavorite.verseId,
                        verseNumber = verseFavorite.verseNumber,
                        chapterId = verseFavorite.chapterId,
                        chapterNameSimple = verseFavorite.chapterNameSimple,
                    ),
                )
            } else {
                database.verseFavoriteDao().delete(verseFavorites)
            }
        }
    }
}
