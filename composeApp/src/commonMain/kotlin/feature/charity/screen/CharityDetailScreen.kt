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
import androidx.compose.runtime.currentCompositeKeyHash
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import core.ui.component.BaseIconButton
import core.ui.component.BaseImage
import core.ui.component.BaseTopAppBar
import feature.other.service.mapper.getString
import feature.other.service.model.AppString
import haqq.composeapp.generated.resources.Res
import haqq.composeapp.generated.resources.share

class CharityDetailScreen(
    private val imageUrl: String,
) : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        val openShare = remember { mutableStateOf(false) }

        Scaffold(
            topBar = {
                BaseTopAppBar(
                    title = AppString.CHARITY_TITLE.getString(),
                    onLeftButtonClick = {
                        navigator.pop()
                    },
                )
            },
        ) { paddingValues ->
            Column(modifier = Modifier.padding(paddingValues).fillMaxSize()) {
                BaseImage(
                    modifier = Modifier.weight(1f),
                    imageUrl = imageUrl,
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
                        contentDescription = AppString.SHARE.getString(),
                    )
                }
            }
        }

        if (openShare.value) {
            ShareImage(imageUrl)
            openShare.value = false
        }

        LaunchedEffect(currentCompositeKeyHash) {
        }
    }
}
