package feature.study.service

import core.data.ApiResponse
import core.data.DataState
import feature.study.service.entity.NoteRealm
import feature.study.service.mapper.mapToModel
import feature.study.service.mapper.mapToNote
import feature.study.service.mapper.mapToVideos
import feature.study.service.model.Note
import feature.study.service.source.remote.StudyRemote
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart

class StudyRepository(
    private val remote: StudyRemote,
    private val realm: Realm,
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

    fun fetchNotes(): List<Note> = realm.query<NoteRealm>().find().map { it.mapToNote() }

    fun fetchNote(id: Int): Note? =
        realm
            .query<NoteRealm>("id == $0", id)
            .find()
            .firstOrNull()
            ?.mapToNote()

    fun deleteNote(id: Int) {
        realm.writeBlocking {
            val localNote = this.query<NoteRealm>("id == $0", id).find().first()

            delete(localNote)
        }
    }

    fun saveNote(note: Note) {
        realm.writeBlocking {
            val localNote = this.query<NoteRealm>("id == $0", note.id).find().firstOrNull()

            val maxNoteId =
                this
                    .query<NoteRealm>()
                    .find()
                    .maxByOrNull { it.id }
                    ?.id ?: 0
            val nextNoteId = maxNoteId + 1

            if (localNote == null) {
                // add
                copyToRealm(
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
