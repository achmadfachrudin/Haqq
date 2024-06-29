package feature.study.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.runtime.currentCompositeKeyHash
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
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
import feature.other.service.mapper.getString
import feature.other.service.model.AppString
import feature.study.service.model.Note
import haqq.composeapp.generated.resources.Res
import haqq.composeapp.generated.resources.save
import haqq.composeapp.generated.resources.trash_2
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.compose.resources.painterResource

class NoteDetailScreen(
    private val noteId: Int = 0,
) : Screen {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val screenModel = koinScreenModel<NoteDetailScreenModel>()
        val state by screenModel.state.collectAsState()
        val navigator = LocalNavigator.currentOrThrow

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
                    title = AppString.STUDY_NOTE_NEW.getString(),
                    onLeftButtonClick = {
                        navigator.pop()
                    },
                    showOptionalButton = !state.note?.id.isNullOrZero(),
                    optionalButtonImage = painterResource(Res.drawable.trash_2),
                    onOptionalButtonClick = {
                        openConfirmationDialog.value = true
                    },
                    showRightButton = true,
                    rightButtonImage = painterResource(Res.drawable.save),
                    onRightButtonClick = {
                        state.note?.let { note ->
                            val created =
                                if (note.createdAt > 0) {
                                    note.createdAt
                                } else {
                                    Clock.System.now().toEpochMilliseconds()
                                }

                            screenModel.saveNote(
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
                            navigator.pop()
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
                        label = { BaseText(AppString.NOTE_TITLE.getString()) },
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
                        label = { BaseText(AppString.NOTE_KITAB.getString()) },
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
                        label = { BaseText(AppString.NOTE_SPEAKER.getString()) },
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
                            text = AppString.NOTE_DATE.getString(),
                            style = getHaqqTypography().bodySmall,
                            color = MaterialTheme.colorScheme.primary,
                        )
                        BaseText(
                            text = valueDate.value,
                        )
                    }

                    BaseDivider()

                    TextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = valueText.value,
                        onValueChange = { newText ->
                            valueText.value = newText
                        },
                        label = { BaseText(AppString.NOTE_TEXT.getString()) },
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
                            Text(AppString.OK.getString())
                        }
                    },
                    dismissButton = {
                        TextButton(
                            onClick = {
                                openDateDialog.value = false
                            },
                        ) {
                            Text(AppString.CANCEL.getString())
                        }
                    },
                ) {
                    DatePicker(state = datePickerState)
                }
            }

            if (openConfirmationDialog.value) {
                BaseDialog(
                    onDismissRequest = { openConfirmationDialog.value = false },
                    title = AppString.DELETE_CONFIRMATION_TITLE.getString(),
                    desc = AppString.DELETE_CONFIRMATION_DESC.getString(),
                    negativeButtonText = AppString.CANCEL.getString(),
                    positiveButtonText = AppString.DELETE.getString(),
                    onPositiveClicked = {
                        screenModel.deleteNote(noteId)
                        openConfirmationDialog.value = false
                        navigator.pop()
                    },
                )
            }
        }

        LaunchedEffect(currentCompositeKeyHash) {
            screenModel.getNoteDetail(noteId = noteId)
        }
    }
}