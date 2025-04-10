package feature.prayertime.screen

import AnalyticsConstant.trackScreen
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
import core.ui.component.ArabicCard
import core.ui.component.BaseIconButton
import core.ui.component.BaseImage
import core.ui.component.BaseScrollableTabRow
import core.ui.component.BaseSpacerHorizontal
import core.ui.component.BaseSpacerVertical
import core.ui.component.BaseTopAppBar
import core.ui.component.ErrorState
import core.ui.component.LoadingState
import feature.prayertime.service.model.GuidanceType
import feature.web.screen.YoutubeNav
import haqq.composeapp.generated.resources.Res
import haqq.composeapp.generated.resources.alert_circle
import haqq.composeapp.generated.resources.copied
import haqq.composeapp.generated.resources.copy
import haqq.composeapp.generated.resources.learn_fasting
import haqq.composeapp.generated.resources.learn_hajj_umrah
import haqq.composeapp.generated.resources.learn_salah
import haqq.composeapp.generated.resources.learn_thaharah_junub
import haqq.composeapp.generated.resources.learn_thaharah_tayammum
import haqq.composeapp.generated.resources.learn_thaharah_wudhu
import haqq.composeapp.generated.resources.learn_zakat_fitrah
import haqq.composeapp.generated.resources.learn_zakat_mal
import haqq.composeapp.generated.resources.report
import haqq.composeapp.generated.resources.share
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Serializable
data class GuidanceNav(
    val guidanceTypeName: String,
)

@Composable
fun GuidanceScreen(
    nav: GuidanceNav,
    onBackClick: () -> Unit,
    onYoutubeClick: (YoutubeNav) -> Unit,
) {
    val vm = koinViewModel<GuidanceScreenModel>()
    val state by vm.state.collectAsState()
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    val openMail = remember { mutableStateOf(false) }
    val openShare = remember { mutableStateOf(false) }
    val shareContent = remember { mutableStateOf("") }

    val guidanceType = enumValueOf<GuidanceType>(nav.guidanceTypeName)

    val title =
        when (guidanceType) {
            GuidanceType.WUDHU -> stringResource(Res.string.learn_thaharah_wudhu)
            GuidanceType.TAYAMMUM -> stringResource(Res.string.learn_thaharah_tayammum)
            GuidanceType.JUNUB -> stringResource(Res.string.learn_thaharah_junub)
            GuidanceType.SALAH -> stringResource(Res.string.learn_salah)
            GuidanceType.FASTING -> stringResource(Res.string.learn_fasting)
            GuidanceType.ZAKAT_FITRAH -> stringResource(Res.string.learn_zakat_fitrah)
            GuidanceType.ZAKAT_MAL -> stringResource(Res.string.learn_zakat_mal)
            GuidanceType.HAJJ -> stringResource(Res.string.learn_hajj_umrah)
        }

    Scaffold(
        topBar = {
            BaseTopAppBar(
                title = title,
                onLeftButtonClick = { onBackClick() },
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
                                                    onYoutubeClick(YoutubeNav(guidance.desc))
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
                            contentDescription = stringResource(Res.string.report),
                        )

                        BaseSpacerHorizontal()

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

                        BaseSpacerHorizontal()

                        BaseIconButton(
                            iconResource = Res.drawable.share,
                            onClick = {
                                shareContent.value = message
                                openShare.value = true
                            },
                            contentDescription = stringResource(Res.string.share),
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

    LaunchedEffect(Unit) {
        trackScreen("GuidanceScreen-$guidanceType")
        vm.getGuidance(guidanceType)
    }
}
