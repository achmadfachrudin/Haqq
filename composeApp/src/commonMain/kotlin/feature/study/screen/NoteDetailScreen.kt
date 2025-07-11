package feature.study.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import core.ui.component.BaseDialog
import core.ui.component.BaseDivider
import core.ui.component.BaseText
import core.ui.component.BaseTopAppBar
import core.ui.theme.getHaqqTypography
import core.util.DateUtil
import core.util.DateUtil.formatDateTimeToLong
import core.util.DateUtil.formatDateTimeToString
import core.util.isNullOrZero
import core.util.toLocalDateTime
import feature.study.service.model.Note
import haqq.composeapp.generated.resources.Res
import haqq.composeapp.generated.resources.cancel
import haqq.composeapp.generated.resources.delete
import haqq.composeapp.generated.resources.delete_confirmation_desc
import haqq.composeapp.generated.resources.delete_confirmation_title
import haqq.composeapp.generated.resources.note_date
import haqq.composeapp.generated.resources.note_kitab
import haqq.composeapp.generated.resources.note_speaker
import haqq.composeapp.generated.resources.note_text
import haqq.composeapp.generated.resources.note_title
import haqq.composeapp.generated.resources.ok
import haqq.composeapp.generated.resources.save
import haqq.composeapp.generated.resources.study_note_new
import haqq.composeapp.generated.resources.study_note_title
import haqq.composeapp.generated.resources.trash_2
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.Serializable
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@Serializable
data class NoteDetailNav(
    val noteId: Int = 0,
)

@OptIn(ExperimentalMaterial3Api::class, ExperimentalTime::class)
@Composable
fun NoteDetailScreen(
    nav: NoteDetailNav,
    onBackClick: () -> Unit,
) {
    val vm = koinViewModel<NoteDetailScreenModel>()
    val state by vm.state.collectAsState()

    val datePickerState = rememberDatePickerState()
    val valueTitle = remember { mutableStateOf("") }
    val valueKitab = remember { mutableStateOf("") }
    val valueSpeaker = remember { mutableStateOf("") }
    val valueDate = remember { mutableStateOf("") }
    val valueText = remember { mutableStateOf("") }
    val openDateDialog = remember { mutableStateOf(false) }
    val openConfirmationDialog = remember { mutableStateOf(false) }
    val initialUpdate = remember { mutableStateOf(true) }

    Scaffold(
        topBar = {
            BaseTopAppBar(
                title =
                    if (state.note?.id.isNullOrZero()) {
                        stringResource(Res.string.study_note_new)
                    } else {
                        stringResource(Res.string.study_note_title)
                    },
                showOptionalButton = !state.note?.id.isNullOrZero(),
                showRightButton = true,
                optionalButtonImage = painterResource(Res.drawable.trash_2),
                rightButtonImage = painterResource(Res.drawable.save),
                onLeftButtonClick = {
                    onBackClick()
                },
                onOptionalButtonClick = {
                    openConfirmationDialog.value = true
                },
                onRightButtonClick = {
                    state.note?.let { note ->
                        val created =
                            if (note.createdAt > 0) {
                                note.createdAt
                            } else {
                                Clock.System.now().toEpochMilliseconds()
                            }

                        vm.saveNote(
                            Note(
                                id = note.id,
                                title = valueTitle.value,
                                text = valueText.value,
                                kitab = valueKitab.value,
                                speaker = valueSpeaker.value,
                                createdAt = created,
                                studyAt =
                                    formatDateTimeToLong(
                                        valueDate.value,
                                        DateUtil.dd_MM_yyyy,
                                    ),
                            ),
                        )
                        onBackClick()
                    }
                },
            )
        },
    ) { paddingValues ->
        Column(
            modifier =
                Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(vertical = 16.dp),
        ) {
            state.note?.let { note ->
                if (initialUpdate.value) {
                    initialUpdate.value = false

                    val studyAtLocalDateTime =
                        if (note.studyAt > 0) {
                            note.studyAt.toLocalDateTime()
                        } else {
                            Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
                        }
                    val studyAtMillis =
                        studyAtLocalDateTime
                            .toInstant(TimeZone.currentSystemDefault())
                            .toEpochMilliseconds()
                    val studyAtString =
                        formatDateTimeToString(studyAtLocalDateTime, DateUtil.dd_MM_yyyy)

                    datePickerState.selectedDateMillis = studyAtMillis
                    datePickerState.displayedMonthMillis = studyAtMillis
                    valueTitle.value = note.title
                    valueKitab.value = note.kitab
                    valueSpeaker.value = note.speaker
                    valueDate.value = studyAtString
                    valueText.value = note.text
                }

                val textFieldColor =
                    TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        disabledContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent,
                    )

                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = valueTitle.value,
                    onValueChange = { newText ->
                        valueTitle.value = newText.take(40)
                    },
                    label = { BaseText(stringResource(Res.string.note_title)) },
                    keyboardOptions =
                        KeyboardOptions.Default.copy(
                            imeAction = ImeAction.Next,
                        ),
                    maxLines = 2,
                    colors = textFieldColor,
                )

                BaseDivider()

                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = valueKitab.value,
                    onValueChange = { newText ->
                        valueKitab.value = newText.take(40)
                    },
                    label = { BaseText(stringResource(Res.string.note_kitab)) },
                    keyboardOptions =
                        KeyboardOptions.Default.copy(
                            imeAction = ImeAction.Next,
                        ),
                    maxLines = 2,
                    colors = textFieldColor,
                )

                BaseDivider()

                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = valueSpeaker.value,
                    onValueChange = { newText ->
                        valueSpeaker.value = newText.take(40)
                    },
                    label = { BaseText(stringResource(Res.string.note_speaker)) },
                    keyboardOptions =
                        KeyboardOptions.Default.copy(
                            imeAction = ImeAction.Next,
                        ),
                    maxLines = 2,
                    colors = textFieldColor,
                )

                BaseDivider()

                Column(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .height(TextFieldDefaults.MinHeight)
                            .clickable { openDateDialog.value = true }
                            .padding(TextFieldDefaults.contentPaddingWithLabel()),
                ) {
                    BaseText(
                        text = stringResource(Res.string.note_date),
                        style = getHaqqTypography().bodySmall,
                        color = MaterialTheme.colorScheme.primary,
                    )
                    BaseText(
                        text = valueDate.value,
                    )
                }

                BaseDivider()

                TextField(
                    modifier = Modifier.fillMaxWidth().imePadding(),
                    value = valueText.value,
                    onValueChange = { newText ->
                        valueText.value = newText
                    },
                    label = { BaseText(stringResource(Res.string.note_text)) },
                    singleLine = false,
                    colors = textFieldColor,
                )
            }
        }

        if (openDateDialog.value) {
            val confirmEnabled =
                remember {
                    derivedStateOf { datePickerState.selectedDateMillis != null }
                }

            DatePickerDialog(
                onDismissRequest = {
                    openDateDialog.value = false
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            openDateDialog.value = false
                            datePickerState.selectedDateMillis?.let {
                                valueDate.value =
                                    formatDateTimeToString(
                                        it.toLocalDateTime(),
                                        DateUtil.dd_MM_yyyy,
                                    )
                            }
                        },
                        enabled = confirmEnabled.value,
                    ) {
                        Text(stringResource(Res.string.ok))
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            openDateDialog.value = false
                        },
                    ) {
                        Text(stringResource(Res.string.cancel))
                    }
                },
            ) {
                DatePicker(state = datePickerState)
            }
        }

        if (openConfirmationDialog.value) {
            BaseDialog(
                onDismissRequest = { openConfirmationDialog.value = false },
                title = stringResource(Res.string.delete_confirmation_title),
                desc = stringResource(Res.string.delete_confirmation_desc),
                negativeButtonText = stringResource(Res.string.cancel),
                positiveButtonText = stringResource(Res.string.delete),
                onPositiveClicked = {
                    vm.deleteNote(nav.noteId)
                    openConfirmationDialog.value = false
                    onBackClick()
                },
            )
        }
    }

    LaunchedEffect(Unit) {
        vm.getNoteDetail(noteId = nav.noteId)
    }
}
