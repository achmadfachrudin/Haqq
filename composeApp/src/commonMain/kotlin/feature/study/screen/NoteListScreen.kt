package feature.study.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import core.ui.component.BaseOutlineTextField
import core.ui.component.BaseText
import core.ui.component.BaseTopAppBar
import core.ui.component.ErrorState
import core.ui.theme.getHaqqTypography
import core.util.DateUtil
import core.util.searchBy
import core.util.toLocalDateTime
import feature.other.service.mapper.getString
import feature.other.service.model.AppString
import feature.study.service.model.Note
import haqq.composeapp.generated.resources.Res
import haqq.composeapp.generated.resources.plus
import org.jetbrains.compose.resources.painterResource

class NoteListScreen : Screen {
    @Composable
    override fun Content() {
        val screenModel = koinScreenModel<NoteListScreenModel>()
        val state by screenModel.state.collectAsState()
        val navigator = LocalNavigator.currentOrThrow

        Scaffold(
            topBar = {
                BaseTopAppBar(
                    title = AppString.STUDY_NOTE_TITLE.getString(),
                    onLeftButtonClick = {
                        navigator.pop()
                    },
                )
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { navigator.push(NoteDetailScreen()) },
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.plus),
                        contentDescription = null,
                    )
                }
            },
        ) { paddingValues ->
            Column(modifier = Modifier.padding(paddingValues).fillMaxSize()) {
                val valueSearch = remember { mutableStateOf("") }
                val query = valueSearch.value.lowercase()
                val channelFiltered =
                    state.notes.filter {
                        it.title.searchBy(query) ||
                            it.speaker.searchBy(query) ||
                            it.kitab.searchBy(query)
                    }

                BaseOutlineTextField(
                    modifier = Modifier.padding(16.dp),
                    value = valueSearch.value,
                    onValueChange = { newText ->
                        valueSearch.value =
                            newText
                                .trim()
                                .filter { it.isLetterOrDigit() }
                    },
                    label = AppString.SEARCH_STUDY_NOTE.getString(),
                    trailingClick = { valueSearch.value = "" },
                    keyboardOptions =
                        KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Done,
                        ),
                )

                if (channelFiltered.isEmpty()) {
                    ErrorState(AppString.STUDY_NOTE_EMPTY.getString())
                } else {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        contentPadding = PaddingValues(16.dp),
                    ) {
                        items(channelFiltered) { note ->
                            NoteCard(
                                note = note,
                                onItemClick = {
                                    navigator.push(NoteDetailScreen(note.id))
                                },
                            )
                        }
                    }
                }
            }
        }

        LaunchedEffect(currentCompositeKeyHash) {
            screenModel.getNotes()
        }
    }

    @Composable
    private fun NoteCard(
        note: Note,
        onItemClick: () -> Unit,
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            onClick = { onItemClick() },
            colors =
                CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                ),
        ) {
            Column(
                modifier = Modifier.fillMaxSize().padding(16.dp),
            ) {
                BaseText(
                    text = note.title,
                    style = getHaqqTypography().titleMedium,
                    maxLines = 1,
                )
                BaseText(
                    text = note.kitab,
                    style = getHaqqTypography().titleSmall,
                    maxLines = 1,
                )
                BaseText(
                    text = note.speaker,
                    style = getHaqqTypography().labelMedium,
                    maxLines = 1,
                )
                BaseText(
                    text =
                        DateUtil.formatDateTimeToString(
                            localDateTime = note.studyAt.toLocalDateTime(),
                            pattern = DateUtil.dd_MM_yyyy,
                        ),
                    style = getHaqqTypography().bodySmall,
                    maxLines = 1,
                )
            }
        }
    }
}
