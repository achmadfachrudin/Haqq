package feature.charity.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.currentCompositeKeyHash
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import core.ui.component.BaseButton
import core.ui.component.BaseImage
import core.ui.component.BaseTopAppBar
import core.ui.component.ErrorState
import core.ui.component.LoadingState
import core.ui.theme.ExtraColor
import core.ui.theme.ExtraColor.PINK
import feature.other.service.mapper.getString
import feature.other.service.model.AppString

class CharityListScreen : Screen {
    @Composable
    override fun Content() {
        val screenModel = koinScreenModel<CharityListScreenModel>()
        val state by screenModel.state.collectAsState()
        val navigator = LocalNavigator.currentOrThrow

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
                when (val display = state) {
                    is CharityListScreenModel.State.Loading -> {
                        LoadingState()
                    }

                    is CharityListScreenModel.State.Content -> {
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(3),
                            modifier = Modifier.weight(1f),
                        ) {
                            items(display.images) {
                                BaseImage(
                                    modifier =
                                        Modifier
                                            .aspectRatio(1f)
                                            .clickable {
                                                navigator.push(
                                                    CharityDetailScreen(it),
                                                )
                                            },
                                    imageUrl = it,
                                )
                            }
                        }

                        BaseButton(
                            modifier = Modifier.padding(16.dp).fillMaxWidth(),
                            colors =
                                ButtonDefaults.buttonColors(
                                    containerColor = ExtraColor.getPairColor(PINK).first,
                                    contentColor = ExtraColor.getPairColor(PINK).second,
                                ),
                            text = AppString.SUPPORT_TITLE.getString(),
                            onClick = {
                                openSupport(navigator)
                            },
                        )
                    }

                    is CharityListScreenModel.State.Error -> {
                        ErrorState(display.message)
                    }
                }
            }
        }

        LaunchedEffect(currentCompositeKeyHash) {
            screenModel.getCharity()
        }
    }
}
