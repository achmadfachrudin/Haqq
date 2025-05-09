package feature.other.screen

import AnalyticsConstant.trackScreen
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import core.ui.component.ArabicCard
import core.ui.component.BaseDialog
import core.ui.component.BaseDivider
import core.ui.component.BaseItemCard
import core.ui.component.BaseSpacerHorizontal
import core.ui.component.BaseText
import core.ui.component.BaseTopAppBar
import core.ui.component.itemPadding
import feature.other.service.model.AppSetting
import haqq.composeapp.generated.resources.Res
import haqq.composeapp.generated.resources.arabic_style
import haqq.composeapp.generated.resources.arabic_text_size
import haqq.composeapp.generated.resources.color
import haqq.composeapp.generated.resources.eye
import haqq.composeapp.generated.resources.language
import haqq.composeapp.generated.resources.restart_please
import haqq.composeapp.generated.resources.settings
import haqq.composeapp.generated.resources.theme
import haqq.composeapp.generated.resources.translation
import haqq.composeapp.generated.resources.translation_text_size
import haqq.composeapp.generated.resources.transliteration
import haqq.composeapp.generated.resources.transliteration_text_size
import haqq.composeapp.generated.resources.type
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Serializable
object SettingNav

@Composable
fun SettingScreen(onBackClick: () -> Unit) {
    val vm = koinViewModel<SettingScreenModel>()
    val state by vm.state.collectAsState()
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    val openArabicStyleDialog = remember { mutableStateOf(false) }
    val openThemeDialog = remember { mutableStateOf(false) }
    val openThemeColorDialog = remember { mutableStateOf(false) }
    val openLanguageDialog = remember { mutableStateOf(false) }

    val restartText = stringResource(Res.string.restart_please)

    Scaffold(
        topBar = {
            BaseTopAppBar(
                title = stringResource(Res.string.settings),
                onLeftButtonClick = { onBackClick() },
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
                    title = stringResource(Res.string.theme),
                    style = it.theme.display,
                ) {
                    openThemeDialog.value = true
                }

                BaseDivider()

                SettingStyleCard(
                    title = stringResource(Res.string.color),
                    style = it.themeColor.display,
                ) {
                    openThemeColorDialog.value = true
                }

                BaseDivider()

                SettingStyleCard(
                    title = stringResource(Res.string.language),
                    style = it.language.display,
                ) {
                    openLanguageDialog.value = true
                }

                BaseDivider()

                SettingStyleCard(
                    title = stringResource(Res.string.arabic_style),
                    style = it.arabicStyle.display,
                ) {
                    openArabicStyleDialog.value = true
                }

                BaseDivider()

                SettingVisibilityCard(
                    title = stringResource(Res.string.transliteration),
                    shouldShow = it.transliterationVisibility,
                ) { shouldShow -> vm.updateTransliterationVisibility(shouldShow) }

                BaseDivider()

                SettingVisibilityCard(
                    title = stringResource(Res.string.translation),
                    shouldShow = it.translationVisibility,
                ) { shouldShow -> vm.updateTranslationVisibility(shouldShow) }

                BaseDivider()

                SettingFontSizeCard(
                    title = stringResource(Res.string.arabic_text_size),
                    fontSize = it.arabicFontSize,
                ) { fontSize ->
                    vm.updateArabicFontSize(fontSize)
                }

                BaseDivider()

                SettingFontSizeCard(
                    title = stringResource(Res.string.transliteration_text_size),
                    fontSize = it.transliterationFontSize,
                ) { fontSize ->
                    vm.updateTransliterationFontSize(fontSize)
                }

                BaseDivider()

                SettingFontSizeCard(
                    title = stringResource(Res.string.translation_text_size),
                    fontSize = it.translationFontSize,
                ) { fontSize ->
                    vm.updateTranslationFontSize(fontSize)
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

    if (openThemeDialog.value) {
        BaseDialog(
            onDismissRequest = { openThemeDialog.value = false },
            title = stringResource(Res.string.theme),
            shouldCustomContent = true,
            content = {
                AppSetting.Theme.entries.forEach { theme ->
                    BaseItemCard(
                        title = theme.display,
                        titleColor = GetSelectedColor(theme == state.appSetting?.theme),
                        onClick = {
                            vm.updateTheme(theme)
                            openThemeDialog.value = false
                            scope.launch {
                                snackbarHostState.showSnackbar(restartText)
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
            title = stringResource(Res.string.color),
            shouldCustomContent = true,
            content = {
                AppSetting.ThemeColor.entries.forEach { themeColor ->
                    BaseItemCard(
                        title = themeColor.display,
                        titleColor = GetSelectedColor(themeColor == state.appSetting?.themeColor),
                        onClick = {
                            vm.updateThemeColor(themeColor)
                            openThemeColorDialog.value = false
                            scope.launch {
                                snackbarHostState.showSnackbar(restartText)
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
            title = stringResource(Res.string.language),
            shouldCustomContent = true,
            content = {
                AppSetting.Language.entries.forEach { language ->
                    BaseItemCard(
                        title = language.display,
                        titleColor = GetSelectedColor(language == state.appSetting?.language),
                        onClick = {
                            openLanguageDialog.value = false
                            scope.launch {
                                snackbarHostState.showSnackbar(restartText)
                            }
                            vm.updateLanguage(language)
                        },
                    )
                }
            },
        )
    }

    if (openArabicStyleDialog.value) {
        BaseDialog(
            onDismissRequest = { openArabicStyleDialog.value = false },
            title = stringResource(Res.string.arabic_style),
            shouldCustomContent = true,
            content = {
                AppSetting.ArabicStyle.entries.dropLast(1).forEach { arabicStyle ->
                    BaseItemCard(
                        title = arabicStyle.display,
                        titleColor = GetSelectedColor(arabicStyle == state.appSetting?.arabicStyle),
                        onClick = {
                            vm.updateArabicStyle(arabicStyle)
                            openArabicStyleDialog.value = false
                            scope.launch {
                                snackbarHostState.showSnackbar(restartText)
                            }
                        },
                    )
                }
            },
        )
    }

    LaunchedEffect(Unit) {
        trackScreen("SettingScreen")
        vm.getSetting()
    }
}

@Composable
private fun GetSelectedColor(isSelected: Boolean): Color =
    if (isSelected) {
        MaterialTheme.colorScheme.primary
    } else {
        Color.Unspecified
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
