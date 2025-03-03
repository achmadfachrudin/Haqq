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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import core.ui.component.BaseItemCard
import core.ui.component.BaseOutlineTextField
import core.ui.component.BaseTopAppBar
import core.ui.component.ErrorState
import core.ui.component.LoadingState
import core.util.searchBy
import feature.other.service.mapper.getString
import feature.other.service.model.AppString
import kotlinx.serialization.Serializable
import org.koin.compose.viewmodel.koinViewModel

@Serializable
data class DuaListNav(
    val duaCategoryTag: String,
    val duaCategoryTitle: String,
)

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DuaListScreen(
    nav: DuaListNav,
    onBackClick: () -> Unit,
    onDuaDetailClick: (DuaPagerNav) -> Unit,
) {
    val vm = koinViewModel<DuaListScreenModel>()
    val state by vm.state.collectAsState()

    Scaffold(
        topBar = {
            BaseTopAppBar(
                title = nav.duaCategoryTitle,
                onLeftButtonClick = {
                    onBackClick()
                },
            )
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier.padding(paddingValues).fillMaxSize(),
        ) {
            when (val display = state) {
                is DuaListScreenModel.State.Content -> {
                    val valueSearch = rememberSaveable { mutableStateOf("") }
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
                                    onDuaDetailClick(
                                        DuaPagerNav(
                                            duaCategoryTag = nav.duaCategoryTag,
                                            duaCategoryTitle = nav.duaCategoryTitle,
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

    LaunchedEffect(Unit) {
        if (vm.shouldRefresh) {
            trackScreen("DuaListScreen")
            vm.getDuaByTag(nav.duaCategoryTag)
            vm.shouldRefresh = false
        }
    }
}
