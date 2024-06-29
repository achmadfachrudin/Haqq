package feature.dhikr.screen

import AnalyticsConstant.trackScreen
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.currentCompositeKeyHash
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import core.ui.component.BaseOutlineTextField
import core.ui.component.BaseTitle
import core.ui.component.BaseTopAppBar
import core.ui.component.ErrorState
import core.ui.component.LoadingState
import core.util.searchBy
import feature.other.service.mapper.getString
import feature.other.service.model.AppString

class DuaSunnahScreen : Screen {
    @Composable
    override fun Content() {
        val screenModel = koinScreenModel<DuaSunnahScreenModel>()
        val state by screenModel.state.collectAsState()
        val navigator = LocalNavigator.currentOrThrow

        Scaffold(
            topBar = {
                BaseTopAppBar(
                    title = AppString.DUA_SUNNAH.getString(),
                    onLeftButtonClick = {
                        navigator.pop()
                    },
                )
            },
        ) { paddingValues ->
            when (val display = state) {
                is DuaSunnahScreenModel.State.Loading -> {
                    LoadingState()
                }

                is DuaSunnahScreenModel.State.Content -> {
                    val valueSearch = remember { mutableStateOf("") }
                    val query = valueSearch.value.lowercase()
                    val categoriesFiltered =
                        display.categories.filter {
                            it.title.searchBy(query)
                        }

                    LazyVerticalGrid(
                        modifier = Modifier.padding(paddingValues),
                        columns = GridCells.Fixed(2),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                    ) {
                        item(
                            span = { GridItemSpan(this.maxLineSpan) },
                            content = {
                                Surface(Modifier.fillMaxWidth()) {
                                    BaseOutlineTextField(
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
                            },
                        )

                        items(categoriesFiltered) { category ->
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                onClick = {
                                    navigator.push(
                                        DuaListScreen(
                                            category.tag,
                                            category.title,
                                        ),
                                    )
                                },
                                colors =
                                    CardDefaults.cardColors(
                                        containerColor = MaterialTheme.colorScheme.secondaryContainer,
                                        contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                                    ),
                            ) {
                                Box(
                                    modifier =
                                        Modifier
                                            .fillMaxWidth()
                                            .height(120.dp)
                                            .padding(16.dp),
                                ) {
                                    BaseTitle(
                                        modifier = Modifier.align(Alignment.BottomStart),
                                        text = category.title,
                                    )
                                }
                            }
                        }
                    }
                }

                is DuaSunnahScreenModel.State.Error -> {
                    ErrorState(display.message)
                }
            }
        }

        LaunchedEffect(currentCompositeKeyHash) {
            trackScreen(DuaSunnahScreen::class)
            screenModel.getDoaCategories()
        }
    }
}
