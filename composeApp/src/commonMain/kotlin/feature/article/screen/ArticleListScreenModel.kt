package feature.article.screen

import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import core.data.DataState
import feature.article.service.ArticleRepository
import feature.article.service.model.ArticleMedia
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ArticleListScreenModel(
    private val repository: ArticleRepository,
) : StateScreenModel<ArticleListScreenModel.State>(State.Loading) {
    sealed class State {
        object Loading : State()

        data class Error(
            val message: String,
        ) : State()

        data class Content(
            val medias: List<ArticleMedia>,
        ) : State()
    }

    fun getMedias() {
        screenModelScope.launch {
            repository.fetchMedias().collectLatest {
                mutableState.value =
                    when (it) {
                        is DataState.Error -> State.Error(it.message)
                        DataState.Loading -> State.Loading
                        is DataState.Success -> State.Content(it.data)
                    }
            }
        }
    }
}
