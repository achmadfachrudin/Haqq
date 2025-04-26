package feature.study.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import feature.study.service.StudyRepository
import feature.study.service.model.Note
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class NoteDetailScreenModel(
    private val repository: StudyRepository,
) : ViewModel() {
    val mutableState = MutableStateFlow(State())
    val state: StateFlow<State> = mutableState.asStateFlow()

    data class State(
        val note: Note? = null,
    )

    fun getNoteDetail(noteId: Int) {
        viewModelScope.launch {
            val note =
                if (noteId == 0) {
                    Note()
                } else {
                    repository.fetchNote(noteId)
                }
            mutableState.value = mutableState.value.copy(note = note)
        }
    }

    fun deleteNote(noteId: Int) {
        viewModelScope.launch {
            repository.deleteNote(noteId)
        }
    }

    fun saveNote(note: Note) {
        viewModelScope.launch {
            repository.saveNote(note)
        }
    }
}
