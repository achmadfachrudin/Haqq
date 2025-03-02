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
import core.ui.component.ArabicCard
import core.ui.component.BaseIconButton
import core.ui.component.BaseScrollableTabRow
import core.ui.component.BaseSpacerHorizontal
import core.ui.component.BaseSpacerVertical
import core.ui.component.BaseTopAppBar
import core.ui.component.ErrorState
import core.ui.component.LoadingState
import feature.other.service.mapper.getString
import feature.other.service.model.AppString
import haqq.composeapp.generated.resources.Res
import haqq.composeapp.generated.resources.alert_circle
import haqq.composeapp.generated.resources.copy
import haqq.composeapp.generated.resources.share
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import org.koin.compose.viewmodel.koinViewModel

@Serializable
data class DuaPagerNav(
    val duaCategoryTag: String,
    val duaCategoryTitle: String,
    val duaId: Int,
)

@Composable
fun DuaPagerScreen(
    nav: DuaPagerNav,
    onBackClick: () -> Unit,
) {
    val vm = koinViewModel<DuaListScreenModel>()
    val state by vm.state.collectAsState()
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val clipboardManager = LocalClipboardManager.current
    val openMail = remember { mutableStateOf(false) }
    val openShare = remember { mutableStateOf(false) }
    val shareContent = remember { mutableStateOf("") }
    val isFirstLaunch = remember { mutableStateOf(true) }

    Scaffold(
        topBar = {
            BaseTopAppBar(
                title = nav.duaCategoryTitle,
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
                is DuaListScreenModel.State.Content -> {
                    val duas = display.duas
                    val tabTitles = duas.map { it.title }
                    val pagerState = rememberPagerState(pageCount = { tabTitles.size })
                    if (isFirstLaunch.value) {
                        isFirstLaunch.value = false
                        scope.launch {
                            pagerState.scrollToPage(duas.indexOfFirst { it.id == nav.duaId })
                        }
                    }
                    val dua = duas[pagerState.currentPage]

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
                            title = dua.title,
                            textArabic = dua.textArabic,
                            textTransliteration = dua.textTransliteration,
                            textTranslation = dua.textTranslation,
                            hadith = dua.hadith,
                            showRipple = false,
                            onClick = {
                            },
                        )
                    }

                    BottomAppBar(
                        actions = {
                            val message =
                                """
                                ${nav.duaCategoryTitle}

                                ${dua.title}
                                ${dua.textArabic}
                                ${dua.textTransliteration}
                                ${dua.textTranslation}
                                ${dua.hadith}
                                """.trimIndent()

                            BaseIconButton(
                                iconResource = Res.drawable.alert_circle,
                                onClick = {
                                    shareContent.value = message
                                    openMail.value = true
                                },
                                contentDescription = AppString.REPORT.getString(),
                            )

                            BaseSpacerHorizontal()

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

                            BaseSpacerHorizontal()

                            BaseIconButton(
                                iconResource = Res.drawable.share,
                                onClick = {
                                    shareContent.value = message
                                    openShare.value = true
                                },
                                contentDescription = AppString.SHARE.getString(),
                            )
                        },
                    )

                    if (openMail.value) {
                        SendMail(subject = "[Dua-Report]", message = shareContent.value)
                        openMail.value = false
                    }

                    if (openShare.value) {
                        ShareText(shareContent.value)
                        openShare.value = false
                    }
                }

                is DuaListScreenModel.State.Error -> {
                    ErrorState(display.message)
                }

                DuaListScreenModel.State.Loading -> {
                    LoadingState()
                }
            }
        }

        LaunchedEffect(Unit) {
            trackScreen("DuaPagerScreen-${nav.duaCategoryTag}")
            vm.getDuaByTag(nav.duaCategoryTag)
        }
    }
}
