package feature.dhikr.screen

import AnalyticsConstant.trackScreen
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.currentCompositeKeyHash
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinNavigatorScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import core.ui.component.ArabicCard
import core.ui.component.BaseIconButton
import core.ui.component.BaseScrollableTabRow
import core.ui.component.BaseSpacerHorizontal
import core.ui.component.BaseSpacerVertical
import core.ui.component.BaseTopAppBar
import feature.other.service.mapper.getString
import feature.other.service.model.AppString
import haqq.composeapp.generated.resources.Res
import haqq.composeapp.generated.resources.alert_circle
import haqq.composeapp.generated.resources.copy
import haqq.composeapp.generated.resources.share
import kotlinx.coroutines.launch
import sendMail
import shareText

@OptIn(ExperimentalFoundationApi::class)
class DuaDetailScreen(
    private val duaCategoryTitle: String,
    val duaId: Int,
) : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val screenModel = navigator.koinNavigatorScreenModel<DuaListScreenModel>()
        val scope = rememberCoroutineScope()
        val snackbarHostState = remember { SnackbarHostState() }
        val clipboardManager = LocalClipboardManager.current
        val openMail = remember { mutableStateOf(false) }
        val openShare = remember { mutableStateOf(false) }
        val shareContent = remember { mutableStateOf("") }

        val duas = screenModel.getDuaList()
        val tabTitles = duas.map { it.title }
        val pagerState = rememberPagerState(pageCount = { tabTitles.size })
        val dua = duas[pagerState.currentPage]

        Scaffold(
            topBar = {
                BaseTopAppBar(
                    title = duaCategoryTitle,
                    onLeftButtonClick = {
                        navigator.pop()
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

                Row(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                ) {
                    val message =
                        """
                        $duaCategoryTitle

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
                }

                if (openMail.value) {
                    sendMail(shareContent.value)
                    openMail.value = false
                }

                if (openShare.value) {
                    shareText(shareContent.value)
                    openShare.value = false
                }
            }
        }

        LaunchedEffect(currentCompositeKeyHash) {
            trackScreen(DuaDetailScreen::class)
            pagerState.animateScrollToPage(duas.indexOfFirst { it.id == duaId })
        }
    }
}
