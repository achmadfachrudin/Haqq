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
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.currentCompositeKeyHash
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import core.ui.component.ArabicCard
import core.ui.component.BaseOutlineTextField
import core.ui.component.BaseTopAppBar
import core.ui.component.ErrorState
import core.ui.component.LoadingState
import core.util.searchBy
import feature.other.service.mapper.getString
import feature.other.service.model.AppString
import kotlinx.coroutines.launch

class AsmaulHusnaScreen : Screen {
    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    override fun Content() {
        val screenModel = koinScreenModel<AsmaulHusnaScreenModel>()
        val state by screenModel.state.collectAsState()
        val navigator = LocalNavigator.currentOrThrow
        val scope = rememberCoroutineScope()
        val snackbarHostState = remember { SnackbarHostState() }
        val clipboardManager = LocalClipboardManager.current

        Scaffold(
            topBar = {
                BaseTopAppBar(
                    title = AppString.ASMAUL_HUSNA_TITLE.getString(),
                    onLeftButtonClick = {
                        navigator.pop()
                    },
                )
            },
            snackbarHost = {
                SnackbarHost(hostState = snackbarHostState)
            },
        ) { paddingValues ->
            Column(
                modifier = Modifier.padding(paddingValues).fillMaxSize(),
            ) {
                when (val display = state) {
                    is AsmaulHusnaScreenModel.State.Loading -> {
                        LoadingState()
                    }

                    is AsmaulHusnaScreenModel.State.Content -> {
                        val valueSearch = remember { mutableStateOf("") }
                        val query = valueSearch.value.lowercase()
                        val namesFiltered =
                            display.names.filter {
                                it.textTranslation.searchBy(query) ||
                                    it.textTransliteration.searchBy(query)
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
                                        label = AppString.SEARCH_ASMAUL_HUSNA.getString(),
                                        trailingClick = { valueSearch.value = "" },
                                        keyboardOptions =
                                            KeyboardOptions.Default.copy(
                                                keyboardType = KeyboardType.Text,
                                                imeAction = ImeAction.Done,
                                            ),
                                    )
                                }
                            }

                            items(namesFiltered) { asma ->
                                ArabicCard(
                                    modifier = Modifier.fillMaxWidth(),
                                    textArabic = asma.textArabic,
                                    textTransliteration = asma.textTransliteration,
                                    textTranslation = asma.textTranslation,
                                    onClick = {
                                        clipboardManager.setText(
                                            AnnotatedString(
                                                """
                                                ${asma.textArabic}
                                                ${asma.textTransliteration}
                                                ${asma.textTranslation}
                                                """.trimIndent(),
                                            ),
                                        )
                                        scope.launch {
                                            snackbarHostState.showSnackbar(
                                                AppString.COPIED.getString(),
                                            )
                                        }
                                    },
                                )
                            }
                        }
                    }

                    is AsmaulHusnaScreenModel.State.Error -> {
                        ErrorState(display.message)
                    }
                }
            }
        }

        LaunchedEffect(currentCompositeKeyHash) {
            trackScreen(AsmaulHusnaScreen::class)
            screenModel.getDoa()
        }
    }
}
