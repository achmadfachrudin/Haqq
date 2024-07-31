package core.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import feature.other.service.mapper.getString
import feature.other.service.model.AppString

@Composable
fun LoadingState() {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = CenterHorizontally,
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun ErrorState(
    message: String,
    showRetryButton: Boolean = false,
    onRetryButtonClick: () -> Unit = {},
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = CenterHorizontally,
    ) {
        BaseText(text = message)

        if (showRetryButton) {
            BaseSpacerVertical()

            BaseButton(
                text = AppString.TRY_AGAIN.getString(),
                onClick = onRetryButtonClick,
            )
        }
    }
}
