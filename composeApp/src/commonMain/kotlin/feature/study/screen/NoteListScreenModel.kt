package feature.study.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import core.util.searchBy
import feature.study.service.StudyRepository
import feature.study.service.model.Note
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class NoteListScreenModel(
    private val repository: StudyRepository,
) : ViewModel() {
    val mutableState = MutableStateFlow(State())
    val state: StateFlow<State> = mutableState.asStateFlow()

    data class State(
        val notes: List<Note> = listOf(),
        val searchQuery: String = "",
        val filter: Filter = Filter(),
        val sortType: SortType = SortType.STUDY_AT_NEWEST,
    ) {
        data class Filter(
            val title: String = "",
            val kitab: String = "",
            val speaker: String = "",
        )

        enum class SortType {
            TITLE_ASC,
            TITLE_DESC,
            KITAB_ASC,
            KITAB_DESC,
            SPEAKER_ASC,
            SPEAKER_DESC,
            STUDY_AT_OLDEST,
            STUDY_AT_NEWEST,
        }
    }

    fun getNotes() {
        viewModelScope.launch {
            val notes = repository.fetchNotes()
            mutableState.value = state.value.copy(notes = notes)
        }
    }

    fun filterApplied(filter: State.Filter) {
        viewModelScope.launch {
            val notes =
                state.value.notes
                    .filter {
                        it.title.searchBy(filter.title) ||
                            it.kitab.searchBy(filter.kitab) ||
                            it.speaker.searchBy(filter.speaker)
                    }
            mutableState.value = state.value.copy(notes = notes)
        }
    }

    fun sortApplied(sortType: State.SortType) {
        viewModelScope.launch {
            val notes = state.value.notes

            when (sortType) {
                State.SortType.TITLE_ASC -> notes.sortedBy { it.title }
                State.SortType.TITLE_DESC -> notes.sortedByDescending { it.title }
                State.SortType.KITAB_ASC -> notes.sortedBy { it.kitab }
                State.SortType.KITAB_DESC -> notes.sortedByDescending { it.kitab }
                State.SortType.SPEAKER_ASC -> notes.sortedBy { it.speaker }
                State.SortType.SPEAKER_DESC -> notes.sortedByDescending { it.speaker }
                State.SortType.STUDY_AT_OLDEST -> notes.sortedBy { it.studyAt }
                State.SortType.STUDY_AT_NEWEST -> notes.sortedByDescending { it.studyAt }
            }
            mutableState.value = state.value.copy(notes = notes)
        }
    }
}
