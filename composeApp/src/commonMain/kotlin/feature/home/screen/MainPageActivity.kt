package feature.home.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import core.ui.component.BaseDivider
import core.ui.component.BaseMenuLargeCard
import core.ui.component.BaseTitle
import feature.other.service.mapper.getString
import feature.other.service.model.AppString

@Composable
internal fun MainPageActivity(
    onArticleClick: () -> Unit,
    onStudyVideoClick: () -> Unit,
    onStudyNoteClick: () -> Unit,
    onConversationClick: () -> Unit,
    onCharityClick: () -> Unit,
    onContentClick: () -> Unit,
    onKidsActivityClick: () -> Unit,
) {
    LazyVerticalGrid(
        modifier = Modifier.fillMaxSize(),
        columns = GridCells.Fixed(3),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        // Learn
        item(
            span = { GridItemSpan(this.maxLineSpan) },
        ) {
            BaseTitle(
                AppString.LEARN.getString(),
                horizontalArrangement = Arrangement.Start,
            )
        }

        item {
            BaseMenuLargeCard(
                title = AppString.ARTICLE_TITLE.getString(),
                onItemClick = { onArticleClick() },
            )
        }

        item {
            BaseMenuLargeCard(
                title = AppString.STUDY_TITLE.getString(),
                onItemClick = { onStudyVideoClick() },
            )
        }

        item {
            BaseMenuLargeCard(
                title = AppString.STUDY_NOTE_TITLE.getString(),
                onItemClick = { onStudyNoteClick() },
            )
        }

        item {
            BaseMenuLargeCard(
                title = AppString.KIDS_ACTIVITY_TITLE.getString(),
                onItemClick = { onKidsActivityClick() },
            )
        }

        // Social
        item(
            span = { GridItemSpan(this.maxLineSpan) },
        ) {
            BaseDivider(0)
            BaseTitle(
                modifier = Modifier.padding(top = 16.dp),
                text = AppString.SOCIAL.getString(),
                horizontalArrangement = Arrangement.Start,
            )
        }

        item {
            BaseMenuLargeCard(
                title = AppString.CONVERSATION_TITLE.getString(),
                onItemClick = { onConversationClick() },
            )
        }

        item {
            BaseMenuLargeCard(
                title = AppString.CHARITY_TITLE.getString(),
                onItemClick = { onCharityClick() },
            )
        }
    }
}
