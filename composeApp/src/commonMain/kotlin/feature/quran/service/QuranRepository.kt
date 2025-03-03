package feature.quran.service

import core.data.ApiResponse
import core.data.DataState
import core.util.orZero
import feature.other.service.AppRepository
import feature.other.service.model.AppSetting.Language
import feature.quran.service.entity.ChapterRealm
import feature.quran.service.entity.ChaptersEntity
import feature.quran.service.entity.JuzRealm
import feature.quran.service.entity.LastReadRealm
import feature.quran.service.entity.PageEntity
import feature.quran.service.entity.PageRealm
import feature.quran.service.entity.VerseFavoriteRealm
import feature.quran.service.entity.VerseRealm
import feature.quran.service.mapper.mapToListString
import feature.quran.service.mapper.mapToModel
import feature.quran.service.mapper.mapToRealm
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
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import io.realm.kotlin.query.RealmResults
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.onStart

class QuranRepository(
    private val appRepository: AppRepository,
    private val remote: QuranRemote,
    private val realm: Realm,
) {
    fun checkIsAllDownloaded(): Boolean {
        val latestChapters = realm.query<ChapterRealm>().find().map { it.mapToModel() }

        val isAllDownloaded = latestChapters.all { chapter -> chapter.isDownloaded }

        return isAllDownloaded
    }

    fun fetchChapters() =
        flow {
            val localChapters = realm.query<ChapterRealm>().find()

            if (localChapters.size < MAX_CHAPTER) {
                realm.writeBlocking {
                    delete(ChapterRealm::class)
                }

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
                    chapters.mapToRealm(
                        translationId = translationId,
                        translationEn = translationEn,
                    )

                realm.writeBlocking {
                    finalChapters.forEach {
                        copyToRealm(it)
                    }
                }

                downloadVerses(1).collect()

                val latestChapters = realm.query<ChapterRealm>().find().map { it.mapToModel() }
                emit(DataState.Success(latestChapters))
            } else {
                val latestChapters = realm.query<ChapterRealm>().find().map { it.mapToModel() }

                emit(DataState.Success(latestChapters))
            }
        }.onStart { emit(DataState.Loading) }
            .catch { emit(DataState.Error(it.message.orEmpty())) }
            .flowOn(Dispatchers.IO)

    fun fetchJuzs() =
        flow {
            val localJuzs = realm.query<JuzRealm>().find()

            if (localJuzs.size < MAX_JUZ) {
                realm.writeBlocking {
                    delete(JuzRealm::class)
                }

                when (val result = remote.fetchJuzs()) {
                    is ApiResponse.Error -> emit(DataState.Error(result.message))
                    is ApiResponse.Success -> {
                        val remoteResult =
                            result.body.juzs
                                .orEmpty()
                                .filter { it.id.orZero() <= MAX_JUZ }
                                .distinctBy { it.juzNumber }
                                .mapToRealm()

                        realm.writeBlocking {
                            remoteResult.forEach {
                                copyToRealm(it)
                            }
                        }

                        val latestJuzs =
                            realm.query<JuzRealm>().find().map {
                                it.mapToModel()
                            }

                        emit(DataState.Success(latestJuzs))
                    }
                }
            } else {
                val latestJuzs =
                    realm.query<JuzRealm>().find().map {
                        it.mapToModel()
                    }

                emit(DataState.Success(latestJuzs))
            }
        }.onStart { emit(DataState.Loading) }
            .catch { emit(DataState.Error(it.message.orEmpty())) }
            .flowOn(Dispatchers.IO)

    fun addPages() =
        flow {
            realm.writeBlocking {
                delete(PageRealm::class)
            }

            val pageEntities = mutableListOf<PageEntity>()
            for (i in 1..MAX_PAGE) {
                try {
                    val verses =
                        realm
                            .query<VerseRealm>("pageNumber == $0", i)
                            .find()

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

            realm.writeBlocking {
                pageEntities.forEach { entity ->
                    copyToRealm(entity.mapToRealm())
                }
            }
            emit(true)
        }.flowOn(Dispatchers.IO)

    fun fetchPages() =
        flow {
            val latestPages =
                realm.query<PageRealm>().find().map {
                    it.mapToModel()
                }

            emit(latestPages)
        }.flowOn(Dispatchers.IO)

    fun getChapterById(chapterId: Int): Chapter {
        try {
            return realm
                .query<ChapterRealm>("id == $0", chapterId)
                .find()
                .first()
                .mapToModel()
        } catch (e: Exception) {
            Napier.e { "Error retrieving getChapterById chapterId $chapterId" }
            throw e
        }
    }

    fun getJuzById(juzId: Int): Juz {
        try {
            return realm
                .query<JuzRealm>("id == $0", juzId)
                .find()
                .first()
                .mapToModel()
        } catch (e: Exception) {
            Napier.e { "Error retrieving getJuzById juzId $juzId" }
            throw e
        }
    }

    fun getPageById(pageId: Int): Page {
        try {
            return realm
                .query<PageRealm>("id == $0", pageId)
                .find()
                .first()
                .mapToModel()
        } catch (e: Exception) {
            Napier.e { "Error retrieving getPageById pageId $pageId" }
            throw e
        }
    }

    fun getVerseById(verseId: Int): Verse {
        try {
            val setting = appRepository.getSetting()

            return realm
                .query<VerseRealm>("id == $0", verseId)
                .find()
                .first()
                .mapToModel(setting)
        } catch (e: Exception) {
            Napier.e { "Error retrieving getVerseById verseId $verseId" }
            throw e
        }
    }

    fun getChapterNameSimple(chapterId: Int): String {
        try {
            return realm
                .query<ChapterRealm>("id == $0", chapterId)
                .find()
                .first()
                .nameSimple
        } catch (e: Exception) {
            Napier.e { "Error retrieving getChapterName chapterId $chapterId" }
            throw e
        }
    }

    fun getRandomVerse(): Verse {
        try {
            val setting = appRepository.getSetting()

            return realm
                .query<VerseRealm>()
                .find()
                .random()
                .mapToModel(setting)
        } catch (e: Exception) {
            Napier.e { "Error retrieving getRandomVerse" }
            throw e
        }
    }

    private fun updateChapterIsDownloaded(chapterNumber: Int) {
        realm.writeBlocking {
            val chapter = this.query<ChapterRealm>("id == $0", chapterNumber).find().first()

            chapter.isDownloaded = true
        }
    }

    fun isVerseDownloaded(verseId: Int): Boolean {
        return try {
            return realm.query<VerseRealm>("id == $0", verseId).find().isNotEmpty()
        } catch (e: Exception) {
            Napier.e { "Error retrieving isVerseDownloaded verseId $verseId" }
            false
        }
    }

    fun downloadVerses(chapterNumber: Int) =
        flow {
            val localVerses =
                realm.query<VerseRealm>().find().filter { it.chapterId == chapterNumber }
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
                            realm.writeBlocking {
                                val remoteVerses =
                                    result.body.mapToRealm(
                                        indopak = indopak,
                                        uthmani = uthmani,
                                        uthmaniTajweed = uthmaniTajweed,
                                        transliteration = transliteration,
                                    )

                                remoteVerses.forEach {
                                    copyToRealm(it)
                                }
                            }

                            updateChapterIsDownloaded(chapterNumber)

                            emit(DataState.Success(true))
                        }
                    }
                }
            }
        }.flowOn(Dispatchers.IO)

    fun fetchVersesByChapter(chapterNumber: Int) =
        flow {
            val localVerses =
                realm.query<VerseRealm>().find().filter { it.chapterId == chapterNumber }
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
                            result.body.mapToRealm(
                                indopak = indopak,
                                uthmani = uthmani,
                                uthmaniTajweed = uthmaniTajweed,
                                transliteration = transliteration,
                            )

                        realm.writeBlocking {
                            remoteVerses.forEach {
                                copyToRealm(it)
                            }
                        }

                        updateChapterIsDownloaded(chapterNumber)

                        val latestVerses =
                            realm
                                .query<VerseRealm>()
                                .find()
                                .filter { it.chapterId == chapterNumber }
                                .map { it.mapToModel(setting) }
                                .sortedBy { it.verseNumber }

                        emit(DataState.Success(latestVerses))
                    }
                }
            } else {
                val latestVerses =
                    realm
                        .query<VerseRealm>()
                        .find()
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
                realm
                    .query<VerseRealm>()
                    .find()
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
                realm
                    .query<VerseRealm>()
                    .find()
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
        realm.writeBlocking {
            val verseToDelete: RealmResults<VerseRealm> =
                query<VerseRealm>("chapterId == $0", chapterNumber).find()

            delete(verseToDelete)
        }
    }

    fun resetVersesByJuz(juzNumber: Int) {
        realm.writeBlocking {
            val verseToDelete: RealmResults<VerseRealm> =
                query<VerseRealm>("juzNumber == $0", juzNumber).find()

            delete(verseToDelete)
        }
    }

    fun resetVersesByPage(pageNumber: Int) {
        realm.writeBlocking {
            val verseToDelete: RealmResults<VerseRealm> =
                query<VerseRealm>("pageNumber == $0", pageNumber).find()

            delete(verseToDelete)
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

    fun getLastRead(): LastRead {
        if (realm.query<LastReadRealm>().find().isEmpty()) {
            realm.writeBlocking {
                copyToRealm(LastReadRealm())
            }
        }

        val lastRead = realm.query<LastReadRealm>().find().first()

        return LastRead(
            chapterId = lastRead.chapterId,
            chapterNameSimple = lastRead.chapterNameSimple,
            verseId = lastRead.verseId,
            verseNumber = lastRead.verseNumber,
            progressFloat = lastRead.progressFloat,
            progressInt = lastRead.progressInt,
        )
    }

    fun updateLastRead(verseId: Int) {
        realm.writeBlocking {
            val lastRead = this.query<LastReadRealm>().find().first()
            val verse = getVerseById(verseId)
            val chapter = getChapterById(verse.chapterId)
            val progressFloat = verse.verseNumber.toFloat() / chapter.versesCount.toFloat()
            val progressInt = (progressFloat * 100).toInt()

            lastRead.chapterId = verse.chapterId
            lastRead.chapterNameSimple = chapter.nameSimple
            lastRead.verseId = verse.id
            lastRead.verseNumber = verse.verseNumber
            lastRead.progressFloat = progressFloat
            lastRead.progressInt = progressInt
        }
    }

    fun getVerseFavorites(): List<VerseFavorite> {
        val favorites = realm.query<VerseFavoriteRealm>().find()

        return favorites
            .map {
                VerseFavorite(
                    verseId = it.verseId,
                    verseNumber = it.verseNumber,
                    chapterId = it.chapterId,
                    chapterNameSimple = it.chapterNameSimple,
                )
            }.sortedBy { it.verseId }
    }

    fun isFavorite(verseId: Int): Boolean = realm.query<VerseFavoriteRealm>("verseId == $0", verseId).find().isNotEmpty()

    fun addOrRemoveVerseFavorites(verse: Verse) {
        val verseFavorites =
            realm
                .query<VerseFavoriteRealm>("verseId == $0", verse.id)
                .find()
                .firstOrNull()

        val chapter = getChapterById(verse.chapterId)

        realm.writeBlocking {
            if (verseFavorites == null) {
                copyToRealm(
                    VerseFavoriteRealm().apply {
                        verseId = verse.id
                        verseNumber = verse.verseNumber
                        chapterId = verse.chapterId
                        chapterNameSimple = chapter.nameSimple
                    },
                )
            } else {
                findLatest(verseFavorites)?.also { delete(it) }
            }
        }
    }

    fun addOrRemoveVerseFavorites(verseFavorite: VerseFavorite) {
        val verseFavorites =
            realm
                .query<VerseFavoriteRealm>("verseId == $0", verseFavorite.verseId)
                .find()
                .firstOrNull()

        realm.writeBlocking {
            if (verseFavorites == null) {
                copyToRealm(
                    VerseFavoriteRealm().apply {
                    },
                )
            } else {
                findLatest(verseFavorites)?.also { delete(it) }
            }
        }
    }
}
