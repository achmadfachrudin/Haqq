package feature.charity.screen

import ShareImage
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import core.ui.component.BaseIconButton
import core.ui.component.BaseImage
import core.ui.component.BaseTopAppBar
import haqq.composeapp.generated.resources.Res
import haqq.composeapp.generated.resources.charity_title
import haqq.composeapp.generated.resources.share
import kotlinx.serialization.Serializable
import org.jetbrains.compose.resources.stringResource

@Serializable
data class CharityDetailNav(
    val imageUrl: String,
)

@Composable
fun CharityDetailScreen(
    nav: CharityDetailNav,
    onBackClick: () -> Unit,
) {
    val openShare = remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            BaseTopAppBar(
                title = stringResource(Res.string.charity_title),
                onLeftButtonClick = {
                    onBackClick()
                },
            )
        },
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues).fillMaxSize()) {
            BaseImage(
                modifier = Modifier.weight(1f),
                imageUrl = nav.imageUrl,
                contentScale = ContentScale.FillWidth,
            )

            Row(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                horizontalArrangement = Arrangement.Center,
            ) {
                BaseIconButton(
                    iconResource = Res.drawable.share,
                    onClick = {
                        openShare.value = true
                    },
                    contentDescription = stringResource(Res.string.share),
                )
            }
        }
    }

    if (openShare.value) {
        ShareImage(nav.imageUrl)
        openShare.value = false
    }

    LaunchedEffect(Unit) {
    }
}
