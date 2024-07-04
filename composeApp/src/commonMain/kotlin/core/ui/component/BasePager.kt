package core.ui.component

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BaseIndicator(
    pagerState: PagerState,
    modifier: Modifier = Modifier,
) {
    Row(modifier = modifier) {
        // pageCount start from 0
        repeat(pagerState.pageCount) { iteration ->
            val shape = CircleShape
            val activeColor = MaterialTheme.colorScheme.primary
            val color =
                if (pagerState.currentPage == iteration) activeColor else MaterialTheme.colorScheme.secondaryContainer
            Box(
                modifier =
                    Modifier
                        .padding(2.dp)
                        .clip(shape)
                        .background(color)
                        .border(1.dp, activeColor, shape)
                        .size(10.dp),
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BaseTabRow(
    pagerState: PagerState,
    tabTitles: List<String>,
) {
    val scope = rememberCoroutineScope()

    TabRow(
        selectedTabIndex = pagerState.currentPage,
    ) {
        tabTitles.forEachIndexed { index, title ->
            Tab(
                selected = pagerState.currentPage == index,
                onClick = {
                    scope.launch {
                        pagerState.animateScrollToPage(index)
                    }
                },
                text = {
                    BaseText(
                        text = title,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                },
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BaseScrollableTabRow(
    pagerState: PagerState,
    tabTitles: List<String>,
) {
    val scope = rememberCoroutineScope()

    ScrollableTabRow(
        selectedTabIndex = pagerState.currentPage,
    ) {
        tabTitles.forEachIndexed { index, title ->
            Tab(
                selected = pagerState.currentPage == index,
                onClick = {
                    scope.launch {
                        pagerState.animateScrollToPage(index)
                    }
                },
                text = {
                    BaseText(
                        text = title,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                },
            )
        }
    }
}
