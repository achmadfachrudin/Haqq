package feature.dhikr.screen

import AnalyticsConstant.trackScreen
import SendMail
import ShareText
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExtendedFloatingActionButton
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.util.fastMapIndexed
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import core.ui.component.ArabicCard
import core.ui.component.BaseDialog
import core.ui.component.BaseIconButton
import core.ui.component.BaseScrollableTabRow
import core.ui.component.BaseSpacerVertical
import core.ui.component.BaseText
import core.ui.component.BaseTopAppBar
import core.ui.component.ErrorState
import core.ui.component.LoadingState
import feature.dhikr.service.model.DhikrType
import feature.other.service.mapper.getString
import feature.other.service.model.AppString
import haqq.composeapp.generated.resources.Res
import haqq.composeapp.generated.resources.alert_circle
import haqq.composeapp.generated.resources.copy
import haqq.composeapp.generated.resources.more_horizontal
import haqq.composeapp.generated.resources.share
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource

class DhikrListScreen(
    private val dhikrType: DhikrType,
) : Screen {
    @Composable
    override fun Content() {
        val screenModel = koinScreenModel<DhikrListScreenModel>()
        val state by screenModel.state.collectAsState()
        val navigator = LocalNavigator.currentOrThrow
        val scope = rememberCoroutineScope()
        val snackbarHostState = remember { SnackbarHostState() }
        val clipboardManager = LocalClipboardManager.current

        val openResetDialog = remember { mutableStateOf(false) }
        val openMail = remember { mutableStateOf(false) }
        val openShare = remember { mutableStateOf(false) }
        val shareContent = remember { mutableStateOf("") }

        val title =
            when (dhikrType) {
                DhikrType.MORNING -> AppString.DHIKR_MORNING_TITLE.getString()
                DhikrType.AFTERNOON -> AppString.DHIKR_AFTERNOON_TITLE.getString()
                DhikrType.PRAY -> AppString.DHIKR_PRAY_TITLE.getString()
                DhikrType.SLEEP -> AppString.DHIKR_SLEEP_TITLE.getString()
                DhikrType.RUQYAH -> AppString.DHIKR_RUQYAH_TITLE.getString()
            }

        Scaffold(
            topBar = {
                BaseTopAppBar(
                    title = title,
                    showRightButton = state is DhikrListScreenModel.State.Content,
                    rightButtonImage = painterResource(Res.drawable.more_horizontal),
                    onLeftButtonClick = {
                        navigator.pop()
                    },
                    onRightButtonClick = {
                        openResetDialog.value = true
                    },
                )
            },
            snackbarHost = {
                SnackbarHost(hostState = snackbarHostState)
            },
        ) { paddingValues ->
            Column(modifier = Modifier.padding(paddingValues).fillMaxSize()) {
                when (val display = state) {
                    is DhikrListScreenModel.State.Loading -> {
                        LoadingState()
                    }

                    is DhikrListScreenModel.State.Content -> {
                        val tabTitles =
                            display.dhikrs.fastMapIndexed { index, dhikr ->
                                "${index + 1}. ${dhikr.title}"
                            }
                        val pagerState = rememberPagerState(pageCount = { tabTitles.size })
                        val dhikr = display.dhikrs[pagerState.currentPage]

                        BaseScrollableTabRow(
                            pagerState = pagerState,
                            tabTitles = tabTitles,
                        )

                        BaseSpacerVertical()

                        HorizontalPager(
                            state = pagerState,
                            modifier = Modifier.weight(1f),
                            verticalAlignment = Alignment.Top,
                        ) { index ->
                            ArabicCard(
                                modifier =
                                    Modifier
                                        .fillMaxSize()
                                        .verticalScroll(rememberScrollState()),
                                title = "${index + 1}. ${dhikr.title}",
                                textArabic = dhikr.textArabic,
                                textTransliteration = dhikr.textTransliteration,
                                textTranslation = dhikr.textTranslation,
                                hadith = dhikr.hadith,
                                maxCount = dhikr.maxCount,
                                showRipple = false,
                                onClick = {
                                },
                            )
                        }

                        BottomAppBar(
                            actions = {
                                val message =
                                    """
                                    $title
                                    
                                    ${dhikr.title}
                                    ${dhikr.textArabic}
                                    ${dhikr.textTransliteration}
                                    ${dhikr.textTranslation}
                                    ${dhikr.hadith}
                                    """.trimIndent()

                                BaseIconButton(
                                    iconResource = Res.drawable.alert_circle,
                                    onClick = {
                                        shareContent.value = message
                                        openMail.value = true
                                    },
                                    contentDescription = AppString.REPORT.getString(),
                                )

                                BaseIconButton(
                                    iconResource = Res.drawable.copy,
                                    onClick = {
                                        clipboardManager.setText(AnnotatedString(message))
                                        scope.launch {
                                            snackbarHostState.showSnackbar(
                                                AppString.COPIED.getString(),
                                            )
                                        }
                                    },
                                    contentDescription = AppString.COPIED.getString(),
                                )

                                BaseIconButton(
                                    iconResource = Res.drawable.share,
                                    onClick = {
                                        shareContent.value = message
                                        openShare.value = true
                                    },
                                    contentDescription = AppString.SHARE.getString(),
                                )
                            },
                            floatingActionButton = {
                                ExtendedFloatingActionButton(
                                    onClick = {
                                        screenModel.currentPage = pagerState.currentPage
                                        screenModel.dhikrClicked(dhikr)

                                        scope.launch {
                                            if (dhikr.count + 1 >= dhikr.maxCount) {
                                                pagerState.scrollToPage(pagerState.currentPage + 1)
                                            } else {
                                                pagerState.scrollToPage(0)
                                                pagerState.scrollToPage(screenModel.currentPage)
                                            }
                                        }
                                    },
                                ) {
                                    BaseText("${dhikr.count}")
                                }
                            },
                        )
                    }

                    is DhikrListScreenModel.State.Error -> {
                        ErrorState(display.message)
                    }
                }

                if (openResetDialog.value) {
                    BaseDialog(
                        onDismissRequest = { openResetDialog.value = false },
                        title = AppString.RESET_CONFIRMATION_TITLE.getString(),
                        desc = AppString.RESET_DHIKR_NOTE.getString(),
                        negativeButtonText = AppString.CANCEL.getString(),
                        positiveButtonText = AppString.RESET.getString(),
                        onPositiveClicked = {
                            screenModel.resetDhikrCount()
                            openResetDialog.value = false
                        },
                    )
                }

                if (openMail.value) {
                    SendMail(subject = "[Dhikr-Report]", message = shareContent.value)
                    openMail.value = false
                }

                if (openShare.value) {
                    ShareText(shareContent.value)
                    openShare.value = false
                }
            }
        }

        LaunchedEffect(currentCompositeKeyHash) {
            trackScreen(DhikrListScreen::class)
            screenModel.getDzikir(dhikrType)
        }
    }
}
