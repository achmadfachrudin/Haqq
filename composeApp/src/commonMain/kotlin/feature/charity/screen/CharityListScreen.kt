package feature.charity.screen

import AnalyticsConstant.trackScreen
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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import core.ui.component.BaseButton
import core.ui.component.BaseImage
import core.ui.component.BaseTopAppBar
import core.ui.component.ErrorState
import core.ui.component.LoadingState
import core.ui.theme.ExtraColor
import core.ui.theme.ExtraColor.PINK
import feature.other.service.AppRepository
import feature.web.screen.WebNav
import haqq.composeapp.generated.resources.Res
import haqq.composeapp.generated.resources.charity_title
import haqq.composeapp.generated.resources.support_title
import kotlinx.serialization.Serializable
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.mp.KoinPlatform

@Serializable
object CharityListNav

@Composable
fun CharityListScreen(
    onBackClick: () -> Unit,
    onCharityDetailClick: (CharityDetailNav) -> Unit,
    onSupportClick: (WebNav) -> Unit,
) {
    val vm = koinViewModel<CharityListScreenModel>()
    val state by vm.state.collectAsState()
    val appRepository = KoinPlatform.getKoin().get<AppRepository>()
    val languageId = appRepository.getSetting().language.id

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
                                            onCharityDetailClick(CharityDetailNav(it))
                                        },
                                imageUrl = it,
                            )
                        }
                    }

                    val supportTitle = stringResource(Res.string.support_title)
                    BaseButton(
                        modifier = Modifier.padding(16.dp).fillMaxWidth(),
                        colors =
                            ButtonDefaults.buttonColors(
                                containerColor = ExtraColor.getPairColor(PINK).first,
                                contentColor = ExtraColor.getPairColor(PINK).second,
                            ),
                        text = supportTitle,
                        onClick = {
                            onSupportClick(
                                WebNav(
                                    url = AppConstant.getSupportUrl(languageId),
                                    title = supportTitle,
                                    openExternalIOS = true,
                                ),
                            )
                        },
                    )
                }

                is CharityListScreenModel.State.Error -> {
                    ErrorState(display.message)
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        trackScreen("CharityListScreen")
        vm.getCharity()
    }
}
