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
import core.ui.component.ArabicCard
import core.ui.component.BaseOutlineTextField
import core.ui.component.BaseTopAppBar
import core.ui.component.ErrorState
import core.ui.component.LoadingState
import core.util.searchBy
import haqq.composeapp.generated.resources.Res
import haqq.composeapp.generated.resources.asmaul_husna_title
import haqq.composeapp.generated.resources.copied
import haqq.composeapp.generated.resources.search_asmaul_husna
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Serializable
object AsmaulHusnaNav

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AsmaulHusnaScreen(onBackClick: () -> Unit) {
    val vm = koinViewModel<AsmaulHusnaScreenModel>()
    val state by vm.state.collectAsState()
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val clipboardManager = LocalClipboardManager.current

    Scaffold(
        topBar = {
            BaseTopAppBar(
                title = stringResource(Res.string.asmaul_husna_title),
                onLeftButtonClick = {
                    onBackClick()
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
                                    label = stringResource(Res.string.search_asmaul_husna),
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
                            val copiedText = stringResource(Res.string.copied)
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
                                        snackbarHostState.showSnackbar(copiedText)
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

    LaunchedEffect(Unit) {
        trackScreen("AsmaulHusnaScreen")
        vm.getDoa()
    }
}
