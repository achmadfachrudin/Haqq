package feature.other.screen

import AnalyticsConstant.trackScreen
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import core.ui.component.BaseDialog
import core.ui.component.BaseDivider
import core.ui.component.BaseItemCard
import core.ui.component.BaseLabelValueCard
import core.ui.component.BaseTopAppBar
import feature.other.service.AppRepository
import feature.web.screen.WebNav
import getPlatform
import haqq.composeapp.generated.resources.Res
import haqq.composeapp.generated.resources.about
import haqq.composeapp.generated.resources.app_name
import haqq.composeapp.generated.resources.clear_data
import haqq.composeapp.generated.resources.clear_data_note
import haqq.composeapp.generated.resources.copied
import haqq.composeapp.generated.resources.current_version
import haqq.composeapp.generated.resources.delete_confirmation_title
import haqq.composeapp.generated.resources.feedback
import haqq.composeapp.generated.resources.file_text
import haqq.composeapp.generated.resources.follow
import haqq.composeapp.generated.resources.instagram
import haqq.composeapp.generated.resources.latest_version
import haqq.composeapp.generated.resources.licenses
import haqq.composeapp.generated.resources.mail
import haqq.composeapp.generated.resources.other_title
import haqq.composeapp.generated.resources.privacy_policy
import haqq.composeapp.generated.resources.restart_please
import haqq.composeapp.generated.resources.shield
import haqq.composeapp.generated.resources.support_title
import haqq.composeapp.generated.resources.trash_2
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import kottieComposition.KottieCompositionSpec
import kottieComposition.animateKottieCompositionAsState
import kottieComposition.rememberKottieComposition
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.mp.KoinPlatform
import utils.KottieConstants

@Serializable
object OtherNav

@OptIn(ExperimentalResourceApi::class)
@Composable
fun OtherScreen(
    onBackClick: () -> Unit,
    onWebClick: (WebNav) -> Unit,
) {
    val vm = koinViewModel<OtherScreenModel>()
    val state by vm.state.collectAsState()
    val appRepository = KoinPlatform.getKoin().get<AppRepository>()
    val languageId = appRepository.getSetting().language.id
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val clipboardManager = LocalClipboardManager.current

    var animation by remember { mutableStateOf("") }
    scope.launch {
        animation = Res.readBytes("files/love.json").decodeToString()
    }
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
                title = stringResource(Res.string.other_title),
                onLeftButtonClick = { onBackClick() },
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues).fillMaxSize()) {
            BaseItemCard(
                title = stringResource(Res.string.clear_data),
                iconResource = Res.drawable.trash_2,
            ) {
                openClearDialog.value = true
            }

            BaseDivider()

            val copiedText = stringResource(Res.string.copied)
            BaseItemCard(
                title = stringResource(Res.string.follow),
                iconResource = Res.drawable.instagram,
            ) {
                clipboardManager.setText(AnnotatedString(USERNAME_INSTAGRAM))
                scope.launch {
                    snackbarHostState.showSnackbar(copiedText)
                }
            }

            BaseDivider()

            val supportTitle = stringResource(Res.string.support_title)
            BaseItemCard(
                title = supportTitle,
                iconLottie = {
                    KottieAnimation(
                        modifier = Modifier.size(24.dp),
                        composition = composition,
                        progress = { animationState.progress },
                    )
                },
            ) {
                onWebClick(
                    WebNav(
                        url = AppConstant.getSupportUrl(languageId),
                        title = supportTitle,
                        openExternalIOS = true,
                    ),
                )
            }

            BaseDivider()

            BaseItemCard(
                title = stringResource(Res.string.feedback),
                iconResource = Res.drawable.mail,
            ) {
                openMail.value = true
            }

            BaseDivider()

            val privacyTitle = stringResource(Res.string.privacy_policy)
            BaseItemCard(
                title = privacyTitle,
                iconResource = Res.drawable.shield,
            ) {
                onWebClick(
                    WebNav(
                        url = AppConstant.getPrivacyPolicyUrl(languageId),
                        title = privacyTitle,
                    ),
                )
            }

            BaseDivider()

            val licensesTitle = stringResource(Res.string.licenses)
            BaseItemCard(
                iconResource = Res.drawable.file_text,
                title = licensesTitle,
            ) {
                onWebClick(
                    WebNav(
                        url = AppConstant.getLicensesUrl(languageId),
                        title = licensesTitle,
                    ),
                )
            }

            BaseDivider()

            val aboutTitle = stringResource(Res.string.about)
            BaseItemCard(
                iconResource = Res.drawable.file_text,
                title = aboutTitle,
            ) {
                onWebClick(
                    WebNav(
                        url = AppConstant.getAboutUrl(languageId),
                        title = aboutTitle,
                    ),
                )
            }

            BaseDivider()

            BaseLabelValueCard(
                label = stringResource(Res.string.current_version),
                value = getPlatform().appVersionName,
            )

            if (state is OtherScreenModel.State.Content) {
                val setting = (state as OtherScreenModel.State.Content).setting
                val shouldUpdate = setting.versionCode > getPlatform().appVersionCode

                if (shouldUpdate) {
                    BaseDivider()

                    val urlTitle = "${stringResource(Res.string.app_name)} v${setting.versionName}"
                    BaseLabelValueCard(
                        label = stringResource(Res.string.latest_version),
                        value = setting.versionName,
                        showHighlight = true,
                    ) {
                        onWebClick(
                            WebNav(
                                url = setting.urlUpdate,
                                title = urlTitle,
                            ),
                        )
                    }
                }
            }
        }

        if (openClearDialog.value) {
            BaseDialog(
                onDismissRequest = { openClearDialog.value = false },
                title = stringResource(Res.string.delete_confirmation_title),
                desc = stringResource(Res.string.clear_data_note),
                shouldCustomContent = true,
                content = {
                    OtherScreenModel.ClearType.entries.forEach { clearType ->
                        val restartText = stringResource(Res.string.restart_please)
                        BaseItemCard(
                            title = stringResource(clearType.label),
                            titleColor = MaterialTheme.colorScheme.error,
                            onClick = {
                                vm.clearData(clearType)
                                openClearDialog.value = false
                                scope.launch {
                                    snackbarHostState.showSnackbar(restartText)
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

    LaunchedEffect(Unit) {
        trackScreen("SettingScreen")
        vm.fetchSetting()
    }
}
