package feature.study.screen

import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import core.util.searchBy
import feature.study.service.StudyRepository
import feature.study.service.model.Note
import kotlinx.coroutines.launch

class NoteListScreenModel(
    private val repository: StudyRepository,
) : StateScreenModel<NoteListScreenModel.State>(State()) {
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
        screenModelScope.launch {
            val notes = repository.fetchNotes()
            mutableState.value = state.value.copy(notes = notes)
        }
    }

    fun filterApplied(filter: State.Filter) {
        screenModelScope.launch {
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
        screenModelScope.launch {
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
