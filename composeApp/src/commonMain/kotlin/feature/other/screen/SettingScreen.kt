package feature.other.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.currentCompositeKeyHash
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import core.ui.component.ArabicCard
import core.ui.component.BaseDialog
import core.ui.component.BaseDivider
import core.ui.component.BaseItemCard
import core.ui.component.BaseSpacerHorizontal
import core.ui.component.BaseText
import core.ui.component.BaseTopAppBar
import core.ui.component.itemPadding
import feature.other.service.mapper.getString
import feature.other.service.model.AppSetting
import feature.other.service.model.AppString
import haqq.composeapp.generated.resources.Res
import haqq.composeapp.generated.resources.eye
import haqq.composeapp.generated.resources.type
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource

class SettingScreen : Screen {
    @Composable
    override fun Content() {
        val screenModel = koinScreenModel<SettingScreenModel>()
        val state by screenModel.state.collectAsState()
        val navigator = LocalNavigator.currentOrThrow
        val scope = rememberCoroutineScope()
        val snackbarHostState = remember { SnackbarHostState() }

        val openArabicStyleDialog = remember { mutableStateOf(false) }
        val openThemeDialog = remember { mutableStateOf(false) }
        val openThemeColorDialog = remember { mutableStateOf(false) }
        val openLanguageDialog = remember { mutableStateOf(false) }

        Scaffold(
            topBar = {
                BaseTopAppBar(
                    title = AppString.SETTINGS.getString(),
                    onLeftButtonClick = { navigator.pop() },
                )
            },
            snackbarHost = {
                SnackbarHost(hostState = snackbarHostState)
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
                state.appSetting?.let {
                    SettingStyleCard(
                        title = AppString.THEME.getString(),
                        style = it.theme.display,
                    ) {
                        openThemeDialog.value = true
                    }

                    BaseDivider()

                    SettingStyleCard(
                        title = AppString.COLOR.getString(),
                        style = it.themeColor.display,
                    ) {
                        openThemeColorDialog.value = true
                    }

                    BaseDivider()

                    SettingStyleCard(
                        title = AppString.LANGUAGE.getString(),
                        style = it.language.display,
                    ) {
                        openLanguageDialog.value = true
                    }

                    BaseDivider()

                    SettingStyleCard(
                        title = AppString.ARABIC_STYLE.getString(),
                        style = it.arabicStyle.display,
                    ) {
                        openArabicStyleDialog.value = true
                    }

                    BaseDivider()

                    SettingVisibilityCard(
                        title = AppString.TRANSLITERATION.getString(),
                        shouldShow = it.transliterationVisibility,
                    ) { shouldShow -> screenModel.updateTransliterationVisibility(shouldShow) }

                    BaseDivider()

                    SettingVisibilityCard(
                        title = AppString.TRANSLATION.getString(),
                        shouldShow = it.translationVisibility,
                    ) { shouldShow -> screenModel.updateTranslationVisibility(shouldShow) }

                    BaseDivider()

                    SettingFontSizeCard(
                        title = AppString.ARABIC_TEXT_SIZE.getString(),
                        fontSize = it.arabicFontSize,
                    ) { fontSize ->
                        screenModel.updateArabicFontSize(fontSize)
                    }

                    BaseDivider()

                    SettingFontSizeCard(
                        title = AppString.TRANSLITERATION_TEXT_SIZE.getString(),
                        fontSize = it.transliterationFontSize,
                    ) { fontSize ->
                        screenModel.updateTransliterationFontSize(fontSize)
                    }

                    BaseDivider()

                    SettingFontSizeCard(
                        title = AppString.TRANSLATION_TEXT_SIZE.getString(),
                        fontSize = it.translationFontSize,
                    ) { fontSize ->
                        screenModel.updateTranslationFontSize(fontSize)
                    }
                }

                state.verse?.let {
                    ArabicCard(
                        modifier = Modifier.fillMaxWidth(),
                        textArabic = it.textArabic,
                        textTransliteration = it.textTransliteration,
                        textTranslation = it.textTranslation,
                        onClick = {},
                    )
                }
            }
        }

        if (openArabicStyleDialog.value) {
            BaseDialog(
                onDismissRequest = { openArabicStyleDialog.value = false },
                title = AppString.ARABIC_STYLE.getString(),
                shouldCustomContent = true,
                content = {
                    AppSetting.ArabicStyle.entries.dropLast(1).forEach { arabicStyle ->
                        BaseItemCard(
                            title = arabicStyle.display,
                            onClick = {
                                screenModel.updateArabicStyle(arabicStyle)
                                openArabicStyleDialog.value = false
                                scope.launch {
                                    snackbarHostState.showSnackbar(
                                        AppString.RESTART_PLEASE.getString(),
                                    )
                                }
                            },
                        )
                    }
                },
            )
        }

        if (openThemeDialog.value) {
            BaseDialog(
                onDismissRequest = { openThemeDialog.value = false },
                title = AppString.THEME.getString(),
                shouldCustomContent = true,
                content = {
                    AppSetting.Theme.entries.forEach { theme ->
                        BaseItemCard(
                            title = theme.display,
                            onClick = {
                                screenModel.updateTheme(theme)
                                openThemeDialog.value = false
                                scope.launch {
                                    snackbarHostState.showSnackbar(
                                        AppString.RESTART_PLEASE.getString(),
                                    )
                                }
                            },
                        )
                    }
                },
            )
        }

        if (openThemeColorDialog.value) {
            BaseDialog(
                onDismissRequest = { openThemeColorDialog.value = false },
                title = AppString.COLOR.getString(),
                shouldCustomContent = true,
                content = {
                    AppSetting.ThemeColor.entries.forEach { color ->
                        BaseItemCard(
                            title = color.display,
                            onClick = {
                                screenModel.updateThemeColor(color)
                                openThemeColorDialog.value = false
                                scope.launch {
                                    snackbarHostState.showSnackbar(
                                        AppString.RESTART_PLEASE.getString(),
                                    )
                                }
                            },
                        )
                    }
                },
            )
        }

        if (openLanguageDialog.value) {
            BaseDialog(
                onDismissRequest = { openLanguageDialog.value = false },
                title = AppString.LANGUAGE.getString(),
                shouldCustomContent = true,
                content = {
                    AppSetting.Language.entries.forEach { language ->
                        BaseItemCard(
                            title = language.display,
                            onClick = {
                                openLanguageDialog.value = false
                                scope.launch {
                                    snackbarHostState.showSnackbar(
                                        AppString.RESTART_PLEASE.getString(),
                                    )
                                }
                                screenModel.updateLanguage(language)
                            },
                        )
                    }
                },
            )
        }

        LaunchedEffect(currentCompositeKeyHash) {
            screenModel.getSetting()
        }
    }

    @Composable
    private fun SettingStyleCard(
        title: String,
        style: String,
        onClick: () -> Unit,
    ) {
        Row(
            modifier =
                Modifier
                    .clickable { onClick() }
                    .fillMaxWidth()
                    .padding(itemPadding),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                painter = painterResource(Res.drawable.type),
                contentDescription = title,
            )
            BaseSpacerHorizontal()
            BaseText(text = title)
            Spacer(modifier = Modifier.weight(1f))
            BaseText(text = style)
        }
    }

    @Composable
    private fun SettingVisibilityCard(
        title: String,
        shouldShow: Boolean,
        onChecked: (shouldShow: Boolean) -> Unit,
    ) {
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(itemPadding),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                painter = painterResource(Res.drawable.eye),
                contentDescription = title,
            )
            BaseSpacerHorizontal()
            BaseText(text = title)
            Spacer(modifier = Modifier.weight(1f))
            Switch(checked = shouldShow, onCheckedChange = { onChecked(it) })
        }
    }

    @Composable
    private fun SettingFontSizeCard(
        title: String,
        fontSize: Int,
        onChanged: (fontSize: Int) -> Unit,
    ) {
        var sliderPosition by remember {
            mutableFloatStateOf(
                fontSize.toFloat(),
            )
        }

        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(itemPadding),
        ) {
            val fontSizeList = listOf(14, 16, 18, 20, 22, 24, 26, 28, 30, 32)

            Icon(
                painter = painterResource(Res.drawable.type),
                contentDescription = title,
            )
            BaseSpacerHorizontal()
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    BaseText(text = title)
                    BaseText(text = "${sliderPosition.toInt()}")
                }
                Slider(
                    value = sliderPosition,
                    onValueChange = {
                        sliderPosition = it
                    },
                    steps = fontSizeList.size - 2,
                    valueRange =
                        fontSizeList.first().toFloat()..fontSizeList.last().toFloat(),
                    onValueChangeFinished = {
                        onChanged(sliderPosition.toInt())
                    },
                )
            }
        }
    }
}
