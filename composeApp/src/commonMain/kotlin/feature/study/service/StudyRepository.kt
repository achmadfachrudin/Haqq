package feature.study.service

import core.data.ApiResponse
import core.data.DataState
import data.AppDatabase
import feature.study.service.entity.NoteRealm
import feature.study.service.mapper.mapToModel
import feature.study.service.mapper.mapToNote
import feature.study.service.mapper.mapToVideos
import feature.study.service.model.Note
import feature.study.service.source.remote.StudyRemote
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class StudyRepository(
    private val remote: StudyRemote,
    private val database: AppDatabase,
) {
    fun fetchYoutubeAPI() =
        flow {
            when (val result = remote.fetchYoutubeAPI()) {
                is ApiResponse.Error -> emit(listOf())
                is ApiResponse.Success -> {
                    val remoteResult = result.body.map { it.api.orEmpty() }

                    emit(remoteResult)
                }
            }
        }.flowOn(Dispatchers.IO)

    fun fetchChannels() =
        flow {
            when (val result = remote.fetchChannels()) {
                is ApiResponse.Error -> emit(DataState.Error(result.message))
                is ApiResponse.Success -> {
                    val remoteResult = result.body.map { it.mapToModel() }.sortedBy { it.name }

                    emit(DataState.Success(remoteResult))
                }
            }
        }.onStart { emit(DataState.Loading) }
            .catch { emit(DataState.Error(it.message.orEmpty())) }
            .flowOn(Dispatchers.IO)

    fun fetchLiveVideo(
        apiKey: String,
        query: String,
    ) = flow {
        when (
            val result =
                remote.fetchLiveVideo(
                    key = apiKey,
                    query = query,
                )
        ) {
            is ApiResponse.Error -> emit(DataState.Error(result.message))
            is ApiResponse.Success -> {
                val remoteResult = result.body

                emit(DataState.Success(remoteResult))
            }
        }
    }.onStart { emit(DataState.Loading) }
        .catch { emit(DataState.Error(it.message.orEmpty())) }
        .flowOn(Dispatchers.IO)

    fun fetchVideoList(
        apiKey: String,
        channelId: String,
    ) = flow {
        when (
            val result =
                remote.fetchVideoList(
                    key = apiKey,
                    channelId = channelId,
                )
        ) {
            is ApiResponse.Error -> emit(DataState.Error(result.message))
            is ApiResponse.Success -> {
                val remoteResult = result.body.mapToVideos()

                emit(DataState.Success(remoteResult))
            }
        }
    }.onStart { emit(DataState.Loading) }
        .catch { emit(DataState.Error(it.message.orEmpty())) }
        .flowOn(Dispatchers.IO)

    fun fetchNotes(): List<Note> {
        return runBlocking {
            withContext(Dispatchers.IO) {
                return@withContext database.noteDao().getAll().map { it.mapToNote() }
            }
        }
    }

    fun fetchNote(id: Int): Note {
        return runBlocking {
            withContext(Dispatchers.IO) {
                return@withContext database
                    .noteDao()
                    .loadAllById(listOf(id))
                    .first()
                    .mapToNote()
            }
        }
    }

    fun deleteNote(id: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            database
                .noteDao()
                .loadAllById(listOf(id))
                .first()
                .let { note ->
                    database.noteDao().delete(note)
                }
        }
    }

    fun saveNote(note: Note) {
        CoroutineScope(Dispatchers.IO).launch {
            val localNote = database.noteDao().loadAllById(listOf(note.id)).firstOrNull()

            val maxNoteId =
                database
                    .noteDao()
                    .getAll()
                    .maxByOrNull { it.id }
                    ?.id ?: 0
            val nextNoteId = maxNoteId + 1

            if (localNote == null) {
                // add
                database.noteDao().insert(
                    NoteRealm().apply {
                        id = nextNoteId
                        title = note.title
                        text = note.text
                        speaker = note.speaker
                        kitab = note.kitab
                        createdAt = note.createdAt
                        studyAt = note.studyAt
                    },
                )
            } else {
                // update
                localNote.title = note.title
                localNote.text = note.text
                localNote.speaker = note.speaker
                localNote.kitab = note.kitab
                localNote.createdAt = note.createdAt
                localNote.studyAt = note.studyAt
            }
        }
    }
}
