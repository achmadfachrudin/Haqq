package feature.article.screen

import AnalyticsConstant.trackScreen
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import core.ui.component.BaseScrollableTabRow
import core.ui.component.BaseSpacerVertical
import core.ui.component.BaseText
import core.ui.component.BaseTopAppBar
import core.ui.component.ErrorState
import core.ui.component.LoadingState
import core.ui.component.itemPadding
import core.ui.theme.getHaqqTypography
import feature.other.service.mapper.getString
import feature.other.service.model.AppString
import feature.web.screen.WebNav
import kotlinx.serialization.Serializable
import org.koin.compose.viewmodel.koinViewModel

@Serializable
object ArticleListNav

@Composable
fun ArticleListScreen(
    onBackClick: () -> Unit,
    onWebClick: (WebNav) -> Unit,
) {
    val vm = koinViewModel<ArticleListScreenModel>()
    val state by vm.state.collectAsState()

    Scaffold(
        topBar = {
            BaseTopAppBar(
                title = AppString.ARTICLE_TITLE.getString(),
                onLeftButtonClick = { onBackClick() },
            )
        },
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues).fillMaxSize()) {
            when (val display = state) {
                is ArticleListScreenModel.State.Loading -> {
                    LoadingState()
                }

                is ArticleListScreenModel.State.Content -> {
                    val tabTitles = display.medias.map { it.name }
                    val pagerState = rememberPagerState(pageCount = { tabTitles.size })

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
                        val listState = rememberSaveable(saver = LazyListState.Saver) { LazyListState() }
                        val mediaName = display.medias[index].name
                        val mediaArticles = display.medias[index].articles

                        LazyColumn(
                            modifier = Modifier.weight(1f),
                            contentPadding = PaddingValues(bottom = 16.dp),
                            state = listState,
                        ) {
                            items(mediaArticles) { article ->
                                ArticleCard(
                                    title = article.title,
                                    url = article.url,
                                    date = article.date,
                                    onClick = {
                                        onWebClick(
                                            WebNav(
                                                url = article.url,
                                                title = mediaName,
                                            ),
                                        )
                                    },
                                )
                            }
                        }
                    }
                }

                is ArticleListScreenModel.State.Error -> {
                    ErrorState(display.message)
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        if (vm.shouldRefresh) {
            trackScreen("ArticleListScreen")
            vm.getMedias()
            vm.shouldRefresh = false
        }
    }
}

@Composable
private fun ArticleCard(
    title: String,
    url: String,
    date: String,
    onClick: () -> Unit = {},
) {
    Column(
        modifier =
            Modifier
                .fillMaxWidth()
                .clickable { onClick() }
                .padding(itemPadding),
    ) {
        BaseText(
            text = title,
            style = getHaqqTypography().titleMedium,
        )
        BaseText(
            text = url,
            style = getHaqqTypography().bodySmall,
            color = MaterialTheme.colorScheme.secondary,
        )
        BaseText(text = date, style = getHaqqTypography().bodySmall)
    }
}
