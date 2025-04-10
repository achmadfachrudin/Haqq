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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.util.fastMapIndexed
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
import haqq.composeapp.generated.resources.Res
import haqq.composeapp.generated.resources.alert_circle
import haqq.composeapp.generated.resources.cancel
import haqq.composeapp.generated.resources.copied
import haqq.composeapp.generated.resources.copy
import haqq.composeapp.generated.resources.dhikr
import haqq.composeapp.generated.resources.dhikr_afternoon_title
import haqq.composeapp.generated.resources.dhikr_morning_title
import haqq.composeapp.generated.resources.dhikr_pray_title
import haqq.composeapp.generated.resources.dhikr_ruqyah_title
import haqq.composeapp.generated.resources.dhikr_sleep_title
import haqq.composeapp.generated.resources.more_horizontal
import haqq.composeapp.generated.resources.report
import haqq.composeapp.generated.resources.reset
import haqq.composeapp.generated.resources.reset_confirmation_title
import haqq.composeapp.generated.resources.reset_dhikr_note
import haqq.composeapp.generated.resources.share
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Serializable
data class DhikrPagerNav(
    val dhikrTypeName: String,
)

@Composable
fun DhikrPagerScreen(
    nav: DhikrPagerNav,
    onBackClick: () -> Unit,
) {
    val vm = koinViewModel<DhikrListScreenModel>()
    val state by vm.state.collectAsState()
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val clipboardManager = LocalClipboardManager.current

    val openResetDialog = remember { mutableStateOf(false) }
    val openMail = remember { mutableStateOf(false) }
    val openShare = remember { mutableStateOf(false) }
    val shareContent = remember { mutableStateOf("") }

    val dhikrType = enumValueOf<DhikrType>(nav.dhikrTypeName)

    val title =
        when (dhikrType) {
            DhikrType.MORNING -> stringResource(Res.string.dhikr_morning_title)
            DhikrType.AFTERNOON -> stringResource(Res.string.dhikr_afternoon_title)
            DhikrType.PRAY -> stringResource(Res.string.dhikr_pray_title)
            DhikrType.SLEEP -> stringResource(Res.string.dhikr_sleep_title)
            DhikrType.RUQYAH -> stringResource(Res.string.dhikr_ruqyah_title)
        }

    Scaffold(
        topBar = {
            BaseTopAppBar(
                title = title,
                showRightButton = state is DhikrListScreenModel.State.Content,
                rightButtonImage = painterResource(Res.drawable.more_horizontal),
                onLeftButtonClick = {
                    onBackClick()
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
                                contentDescription = stringResource(Res.string.report),
                            )

                            val copiedText = stringResource(Res.string.copied)
                            BaseIconButton(
                                iconResource = Res.drawable.copy,
                                onClick = {
                                    clipboardManager.setText(AnnotatedString(message))
                                    scope.launch {
                                        snackbarHostState.showSnackbar(copiedText)
                                    }
                                },
                                contentDescription = stringResource(Res.string.copied),
                            )

                            BaseIconButton(
                                iconResource = Res.drawable.share,
                                onClick = {
                                    shareContent.value = message
                                    openShare.value = true
                                },
                                contentDescription = stringResource(Res.string.share),
                            )
                        },
                        floatingActionButton = {
                            ExtendedFloatingActionButton(
                                onClick = {
                                    vm.currentPage = pagerState.currentPage
                                    vm.dhikrClicked(dhikr)

                                    scope.launch {
                                        if (dhikr.count + 1 >= dhikr.maxCount) {
                                            pagerState.scrollToPage(pagerState.currentPage + 1)
                                        } else {
                                            pagerState.scrollToPage(0)
                                            pagerState.scrollToPage(vm.currentPage)
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
                    title = stringResource(Res.string.reset_confirmation_title),
                    desc = stringResource(Res.string.reset_dhikr_note),
                    negativeButtonText = stringResource(Res.string.cancel),
                    positiveButtonText = stringResource(Res.string.reset),
                    onPositiveClicked = {
                        vm.resetDhikrCount()
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

    LaunchedEffect(Unit) {
        trackScreen("DhikrPagerScreen-$dhikrType")
        vm.getDzikir(dhikrType)
    }
}
