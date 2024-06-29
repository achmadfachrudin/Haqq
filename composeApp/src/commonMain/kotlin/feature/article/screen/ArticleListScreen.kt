package feature.article.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.currentCompositeKeyHash
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
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
import feature.web.screen.WebScreen

@OptIn(ExperimentalFoundationApi::class)
class ArticleListScreen : Screen {
    @Composable
    override fun Content() {
        val screenModel = koinScreenModel<ArticleListScreenModel>()
        val state by screenModel.state.collectAsState()
        val navigator = LocalNavigator.currentOrThrow

        Scaffold(
            topBar = {
                BaseTopAppBar(
                    title = AppString.ARTICLE_TITLE.getString(),
                    onLeftButtonClick = { navigator.pop() },
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
                            val mediaName = display.medias[index].name
                            val mediaArticles = display.medias[index].articles

                            LazyColumn(
                                modifier = Modifier.weight(1f),
                                contentPadding = PaddingValues(bottom = 16.dp),
                            ) {
                                items(mediaArticles) { article ->
                                    ArticleCard(
                                        title = article.title,
                                        url = article.url,
                                        date = article.date,
                                        onClick = {
                                            navigator.push(
                                                WebScreen(
                                                    article.url,
                                                    mediaName,
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

        LaunchedEffect(currentCompositeKeyHash) {
            screenModel.getMedias()
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
}
