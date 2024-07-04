package feature.dhikr.screen

import AnalyticsConstant.trackScreen
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.currentCompositeKeyHash
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinNavigatorScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import core.ui.component.BaseItemCard
import core.ui.component.BaseOutlineTextField
import core.ui.component.BaseTopAppBar
import core.ui.component.ErrorState
import core.ui.component.LoadingState
import core.util.searchBy
import feature.other.service.mapper.getString
import feature.other.service.model.AppString

@OptIn(ExperimentalFoundationApi::class)
class DuaListScreen(
    private val duaCategoryTag: String,
    val duaCategoryTitle: String,
) : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val screenModel = navigator.koinNavigatorScreenModel<DuaListScreenModel>()
        val state by screenModel.state.collectAsState()

        Scaffold(
            topBar = {
                BaseTopAppBar(
                    title = duaCategoryTitle,
                    onLeftButtonClick = {
                        navigator.pop()
                    },
                )
            },
        ) { paddingValues ->
            Column(
                modifier = Modifier.padding(paddingValues).fillMaxSize(),
            ) {
                when (val display = state) {
                    is DuaListScreenModel.State.Content -> {
                        val valueSearch = remember { mutableStateOf("") }
                        val query = valueSearch.value.lowercase()
                        val duaFiltered =
                            display.duas.filter {
                                it.title.searchBy(query)
                            }

                        LazyColumn {
                            stickyHeader {
                                Surface(Modifier.fillMaxWidth()) {
                                    BaseOutlineTextField(
                                        modifier = Modifier.padding(16.dp),
                                        value = valueSearch.value,
                                        onValueChange = { newText ->
                                            valueSearch.value =
                                                newText
                                                    .trim()
                                                    .filter { it.isLetterOrDigit() }
                                        },
                                        label = AppString.SEARCH_DUA.getString(),
                                        trailingClick = { valueSearch.value = "" },
                                        keyboardOptions =
                                            KeyboardOptions.Default.copy(
                                                keyboardType = KeyboardType.Text,
                                                imeAction = ImeAction.Done,
                                            ),
                                    )
                                }
                            }
                            items(duaFiltered) { dua ->
                                BaseItemCard(
                                    title = dua.title,
                                    onClick = {
                                        navigator.push(
                                            DuaDetailScreen(
                                                duaCategoryTitle = duaCategoryTitle,
                                                duaId = dua.id,
                                            ),
                                        )
                                    },
                                )
                            }
                        }
                    }

                    is DuaListScreenModel.State.Error -> {
                        ErrorState(display.message)
                    }

                    DuaListScreenModel.State.Loading -> {
                        LoadingState()
                    }
                }
            }
        }

        LaunchedEffect(currentCompositeKeyHash) {
            trackScreen(DuaListScreen::class)
            screenModel.getDuaByTag(duaCategoryTag)
        }
    }
}
