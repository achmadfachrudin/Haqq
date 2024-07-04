package feature.study.screen

import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import feature.study.service.StudyRepository
import feature.study.service.model.Note
import kotlinx.coroutines.launch

class NoteDetailScreenModel(
    private val repository: StudyRepository,
) : StateScreenModel<NoteDetailScreenModel.State>(State()) {
    data class State(
        val note: Note? = null,
    )

    fun getNoteDetail(noteId: Int) {
        screenModelScope.launch {
            val note = repository.fetchNote(noteId) ?: Note()
            mutableState.value = mutableState.value.copy(note = note)
        }
    }

    fun deleteNote(noteId: Int) {
        screenModelScope.launch {
            repository.deleteNote(noteId)
        }
    }

    fun saveNote(note: Note) {
        screenModelScope.launch {
            repository.saveNote(note)
        }
    }
}
