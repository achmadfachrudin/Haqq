package feature.other.screen

import AppConstant
import AppConstant.USERNAME_INSTAGRAM
import KottieAnimation
import SendMail
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.currentCompositeKeyHash
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import core.ui.component.BaseDialog
import core.ui.component.BaseDivider
import core.ui.component.BaseItemCard
import core.ui.component.BaseLabelValueCard
import core.ui.component.BaseTopAppBar
import feature.charity.screen.openSupport
import feature.other.service.AppRepository
import feature.other.service.mapper.getString
import feature.other.service.model.AppString
import feature.web.screen.WebScreen
import getPlatform
import haqq.composeapp.generated.resources.Res
import haqq.composeapp.generated.resources.file_text
import haqq.composeapp.generated.resources.instagram
import haqq.composeapp.generated.resources.mail
import haqq.composeapp.generated.resources.shield
import haqq.composeapp.generated.resources.trash_2
import kotlinx.coroutines.launch
import kottieComposition.KottieCompositionSpec
import kottieComposition.animateKottieCompositionAsState
import kottieComposition.rememberKottieComposition
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.koin.mp.KoinPlatform
import utils.KottieConstants

class OtherScreen : Screen {
    @OptIn(ExperimentalResourceApi::class)
    @Composable
    override fun Content() {
        val screenModel = koinScreenModel<OtherScreenModel>()
        val state by screenModel.state.collectAsState()
        val navigator = LocalNavigator.currentOrThrow
        val appRepository = KoinPlatform.getKoin().get<AppRepository>()
        val languageId = appRepository.getSetting().language.id
        val scope = rememberCoroutineScope()
        val snackbarHostState = remember { SnackbarHostState() }
        val clipboardManager = LocalClipboardManager.current

        var animation by remember { mutableStateOf("") }
        val composition =
            rememberKottieComposition(
                spec = KottieCompositionSpec.File(animation),
            )
        val animationState by animateKottieCompositionAsState(
            composition = composition,
            iterations = KottieConstants.IterateForever,
        )

        val openClearDialog = remember { mutableStateOf(false) }
        val openMail = remember { mutableStateOf(false) }

        Scaffold(
            topBar = {
                BaseTopAppBar(
                    title = AppString.OTHER_TITLE.getString(),
                    onLeftButtonClick = { navigator.pop() },
                )
            },
            snackbarHost = {
                SnackbarHost(hostState = snackbarHostState)
            },
        ) { paddingValues ->
            Column(modifier = Modifier.padding(paddingValues).fillMaxSize()) {
                BaseItemCard(
                    title = AppString.CLEAR_DATA.getString(),
                    iconResource = Res.drawable.trash_2,
                ) {
                    openClearDialog.value = true
                }

                BaseDivider()

                BaseItemCard(
                    title = AppString.FOLLOW.getString(),
                    iconResource = Res.drawable.instagram,
                ) {
                    clipboardManager.setText(AnnotatedString(USERNAME_INSTAGRAM))
                    scope.launch {
                        snackbarHostState.showSnackbar(
                            AppString.COPIED.getString(),
                        )
                    }
                }

                BaseDivider()

                BaseItemCard(
                    title = AppString.SUPPORT_TITLE.getString(),
                    iconLottie = {
                        KottieAnimation(
                            modifier = Modifier.size(24.dp),
                            composition = composition,
                            progress = { animationState.progress },
                        )
                    },
                ) {
                    openSupport(navigator)
                }

                BaseDivider()

                BaseItemCard(
                    title = AppString.FEEDBACK.getString(),
                    iconResource = Res.drawable.mail,
                ) {
                    openMail.value = true
                }

                BaseDivider()

                BaseItemCard(
                    title = AppString.PRIVACY_POLICY.getString(),
                    iconResource = Res.drawable.shield,
                ) {
                    navigator.push(
                        WebScreen(
                            url = AppConstant.getPrivacyPoilicyUrl(languageId),
                            title = AppString.PRIVACY_POLICY.getString(),
                        ),
                    )
                }

                BaseDivider()

                BaseItemCard(
                    iconResource = Res.drawable.file_text,
                    title = AppString.LICENSES.getString(),
                ) {
                    navigator.push(
                        WebScreen(
                            url = AppConstant.getLicensesUrl(languageId),
                            title = AppString.LICENSES.getString(),
                        ),
                    )
                }

                BaseDivider()

                BaseItemCard(
                    iconResource = Res.drawable.file_text,
                    title = AppString.ABOUT.getString(),
                ) {
                    navigator.push(
                        WebScreen(
                            url = AppConstant.getAboutUrl(languageId),
                            title = AppString.ABOUT.getString(),
                        ),
                    )
                }

                BaseDivider()

                BaseLabelValueCard(
                    label = AppString.CURRENT_VERSION.getString(),
                    value = getPlatform().appVersionName,
                )

                if (state is OtherScreenModel.State.Content) {
                    val setting = (state as OtherScreenModel.State.Content).setting
                    val shouldUpdate = setting.versionCode > getPlatform().appVersionCode

                    if (shouldUpdate) {
                        BaseDivider()

                        BaseLabelValueCard(
                            label = AppString.LATEST_VERSION.getString(),
                            value = setting.versionName,
                            showHighlight = true,
                        ) {
                            navigator.push(
                                WebScreen(
                                    setting.urlUpdate,
                                    "${AppString.APP_NAME.getString()} v${setting.versionName}",
                                ),
                            )
                        }
                    }
                }
            }

            if (openClearDialog.value) {
                BaseDialog(
                    onDismissRequest = { openClearDialog.value = false },
                    title = AppString.DELETE_CONFIRMATION_TITLE.getString(),
                    desc = AppString.CLEAR_DATA_NOTE.getString(),
                    shouldCustomContent = true,
                    content = {
                        OtherScreenModel.ClearType.entries.forEach { clearType ->
                            BaseItemCard(
                                title = getClearLabel(clearType),
                                titleColor = MaterialTheme.colorScheme.error,
                                onClick = {
                                    screenModel.clearData(clearType)
                                    openClearDialog.value = false
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

            if (openMail.value) {
                openMail.value = false

                SendMail(subject = "[Feedback]", message = "")
            }
        }

        LaunchedEffect(currentCompositeKeyHash) {
            animation = Res.readBytes("files/love.json").decodeToString()

            screenModel.fetchSetting()
        }
    }

    private fun getClearLabel(clearType: OtherScreenModel.ClearType): String =
        when (clearType) {
            OtherScreenModel.ClearType.DHIKR -> AppString.DHIKR.getString()
            OtherScreenModel.ClearType.DUA -> AppString.DUA.getString()
            OtherScreenModel.ClearType.QURAN -> AppString.QURAN_TITLE.getString()
            OtherScreenModel.ClearType.PRAYER_TIME -> AppString.PRAYER_TIME_TITLE.getString()
            OtherScreenModel.ClearType.STUDY_NOTE -> AppString.STUDY_NOTE_TITLE.getString()
            OtherScreenModel.ClearType.CLEAR_ALL -> AppString.CLEAR_ALL_DATA.getString()
        }
}
