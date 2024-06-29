package feature.home.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import core.ui.component.BaseMenuLandscapeCard
import core.ui.component.BaseSpacerVertical
import feature.other.service.mapper.getString
import feature.other.service.model.AppString

@Composable
internal fun MainPageActivity(
    onArticleClick: () -> Unit,
    onStudyVideoClick: () -> Unit,
    onStudyNoteClick: () -> Unit,
    onConversationClick: () -> Unit,
    onCharityClick: () -> Unit,
) {
    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
    ) {
        BaseMenuLandscapeCard(
            title = AppString.ARTICLE_TITLE.getString(),
            onItemClick = { onArticleClick() },
        )

        BaseSpacerVertical()

        BaseMenuLandscapeCard(
            title = AppString.STUDY_TITLE.getString(),
            onItemClick = { onStudyVideoClick() },
        )

        BaseSpacerVertical()

        BaseMenuLandscapeCard(
            title = AppString.STUDY_NOTE_TITLE.getString(),
            onItemClick = { onStudyNoteClick() },
        )

        BaseSpacerVertical()

        BaseMenuLandscapeCard(
            title = AppString.CONVERSATION_TITLE.getString(),
            onItemClick = { onConversationClick() },
        )

        BaseSpacerVertical()

        BaseMenuLandscapeCard(
            title = AppString.CHARITY_TITLE.getString(),
            onItemClick = { onCharityClick() },
        )
    }
}
