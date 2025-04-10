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
import haqq.composeapp.generated.resources.Res
import haqq.composeapp.generated.resources.article_title
import haqq.composeapp.generated.resources.charity_title
import haqq.composeapp.generated.resources.conversation_title
import haqq.composeapp.generated.resources.kids_activity_title
import haqq.composeapp.generated.resources.learn
import haqq.composeapp.generated.resources.social
import haqq.composeapp.generated.resources.study_note_title
import haqq.composeapp.generated.resources.study_title
import org.jetbrains.compose.resources.stringResource

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
                stringResource(Res.string.learn),
                horizontalArrangement = Arrangement.Start,
            )
        }

        item {
            BaseMenuLargeCard(
                title = stringResource(Res.string.article_title),
                onItemClick = { onArticleClick() },
            )
        }

        item {
            BaseMenuLargeCard(
                title = stringResource(Res.string.study_title),
                onItemClick = { onStudyVideoClick() },
            )
        }

        item {
            BaseMenuLargeCard(
                title = stringResource(Res.string.study_note_title),
                onItemClick = { onStudyNoteClick() },
            )
        }

        item {
            BaseMenuLargeCard(
                title = stringResource(Res.string.kids_activity_title),
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
                text = stringResource(Res.string.social),
                horizontalArrangement = Arrangement.Start,
            )
        }

        item {
            BaseMenuLargeCard(
                title = stringResource(Res.string.conversation_title),
                onItemClick = { onConversationClick() },
            )
        }

        item {
            BaseMenuLargeCard(
                title = stringResource(Res.string.charity_title),
                onItemClick = { onCharityClick() },
            )
        }
    }
}
