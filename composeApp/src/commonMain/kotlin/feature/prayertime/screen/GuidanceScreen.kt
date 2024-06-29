package feature.prayertime.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import core.ui.component.ArabicCard
import core.ui.component.BaseIconButton
import core.ui.component.BaseImage
import core.ui.component.BaseScrollableTabRow
import core.ui.component.BaseSpacerHorizontal
import core.ui.component.BaseSpacerVertical
import core.ui.component.BaseTopAppBar
import core.ui.component.ErrorState
import core.ui.component.LoadingState
import feature.other.service.mapper.getString
import feature.other.service.model.AppString
import feature.prayertime.service.model.GuidanceType
import feature.web.screen.YoutubeScreen
import haqq.composeapp.generated.resources.Res
import haqq.composeapp.generated.resources.alert_circle
import haqq.composeapp.generated.resources.copy
import haqq.composeapp.generated.resources.share
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
class GuidanceScreen(
    private val guidanceType: GuidanceType,
) : Screen {
    @Composable
    override fun Content() {
        val screenModel = koinScreenModel<GuidanceScreenModel>()
        val state by screenModel.state.collectAsState()
        val navigator = LocalNavigator.currentOrThrow
        val scope = rememberCoroutineScope()
        val snackbarHostState = remember { SnackbarHostState() }

        val openMail = remember { mutableStateOf(false) }
        val openShare = remember { mutableStateOf(false) }
        val shareContent = remember { mutableStateOf("") }

        val title =
            when (guidanceType) {
                GuidanceType.WUDHU -> AppString.LEARN_THAHARAH_WUDHU.getString()
                GuidanceType.TAYAMMUM -> AppString.LEARN_THAHARAH_TAYAMMUM.getString()
                GuidanceType.JUNUB -> AppString.LEARN_THAHARAH_JUNUB.getString()
                GuidanceType.SALAH -> AppString.LEARN_SALAH.getString()
                GuidanceType.FASTING -> AppString.LEARN_FASTING.getString()
                GuidanceType.ZAKAT_FITRAH -> AppString.LEARN_ZAKAT_FITRAH.getString()
                GuidanceType.ZAKAT_MAL -> AppString.LEARN_ZAKAT_MAL.getString()
                GuidanceType.HAJJ -> AppString.LEARN_HAJJ_UMRAH.getString()
            }

        Scaffold(
            topBar = {
                BaseTopAppBar(
                    title = title,
                    onLeftButtonClick = { navigator.pop() },
                )
            },
        ) { paddingValues ->
            Column(modifier = Modifier.padding(paddingValues).fillMaxSize()) {
                when (val display = state) {
                    is GuidanceScreenModel.State.Loading -> {
                        LoadingState()
                    }

                    is GuidanceScreenModel.State.Content -> {
                        val clipboardManager = LocalClipboardManager.current

                        val tabTitles = display.guidances.map { it.title }
                        val pagerState = rememberPagerState(pageCount = { tabTitles.size })
                        val guidance = display.guidances[pagerState.currentPage]

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
                            val isVideo = guidance.title.lowercase() == "video"
                            Column(
                                modifier =
                                    Modifier
                                        .fillMaxWidth()
                                        .verticalScroll(rememberScrollState()),
                            ) {
                                if (guidance.image.isNotEmpty()) {
                                    BaseImage(
                                        modifier =
                                            Modifier
                                                .fillMaxWidth()
                                                .padding(16.dp)
                                                .clip(MaterialTheme.shapes.small)
                                                .clickable {
                                                    if (isVideo) {
                                                        navigator.push(YoutubeScreen(guidance.desc))
                                                    }
                                                },
                                        imageUrl = guidance.image,
                                        contentScale = ContentScale.FillWidth,
                                    )
                                }
                                BaseSpacerVertical()
                                ArabicCard(
                                    modifier = Modifier.fillMaxWidth(),
                                    title = if (isVideo) "" else "${index + 1}. ${guidance.title}",
                                    desc = if (isVideo) "" else guidance.desc,
                                    textArabic = guidance.textArabic,
                                    textTransliteration = guidance.textTransliteration,
                                    textTranslation = guidance.textTranslation,
                                    hadith = guidance.hadith,
                                    showRipple = false,
                                    onClick = {
                                    },
                                )
                            }
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth().padding(16.dp),
                        ) {
                            val message =
                                """
                                $title
                                
                                ${guidance.title}
                                ${guidance.textArabic}
                                ${guidance.textTransliteration}
                                ${guidance.textTranslation}
                                ${guidance.hadith}
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

                            Spacer(Modifier.weight(1f))
                        }
                    }

                    is GuidanceScreenModel.State.Error -> {
                        ErrorState(display.message)
                    }
                }
            }
        }

        LaunchedEffect(currentCompositeKeyHash) {
            screenModel.getGuidance(guidanceType)
        }
    }
}
