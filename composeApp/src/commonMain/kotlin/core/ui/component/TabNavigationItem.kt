package core.ui.component

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import core.ui.theme.getHaqqTypography
import feature.home.screen.MainScreenModel
import haqq.composeapp.generated.resources.Res
import haqq.composeapp.generated.resources.activity_title
import haqq.composeapp.generated.resources.battery_charging
import haqq.composeapp.generated.resources.book
import haqq.composeapp.generated.resources.check_square
import haqq.composeapp.generated.resources.clock
import haqq.composeapp.generated.resources.dhikr_title
import haqq.composeapp.generated.resources.home
import haqq.composeapp.generated.resources.home_title
import haqq.composeapp.generated.resources.prayer_title
import haqq.composeapp.generated.resources.quran_title
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun RowScope.TabNavigationItem(
    mainPageState: MainScreenModel.MainPageState,
    pageSelected: MainScreenModel.MainPageState,
    onClick: () -> Unit,
) {
    val isSelected = mainPageState == pageSelected

    val title =
        when (mainPageState) {
            MainScreenModel.MainPageState.HOME -> stringResource(Res.string.home_title)
            MainScreenModel.MainPageState.DHIKR -> stringResource(Res.string.dhikr_title)
            MainScreenModel.MainPageState.QURAN -> stringResource(Res.string.quran_title)
            MainScreenModel.MainPageState.PRAYER -> stringResource(Res.string.prayer_title)
            MainScreenModel.MainPageState.ACTIVITY -> stringResource(Res.string.activity_title)
        }

    val icon =
        when (mainPageState) {
            MainScreenModel.MainPageState.HOME -> Res.drawable.home
            MainScreenModel.MainPageState.DHIKR -> Res.drawable.battery_charging
            MainScreenModel.MainPageState.QURAN -> Res.drawable.book
            MainScreenModel.MainPageState.PRAYER -> Res.drawable.clock
            MainScreenModel.MainPageState.ACTIVITY -> Res.drawable.check_square
        }

    NavigationBarItem(
        selected = isSelected,
        onClick = {
            onClick()
        },
        label = {
            BaseText(title, style = getHaqqTypography().labelMedium)
        },
        icon = {
            Icon(
                painter = painterResource(icon),
                contentDescription = title,
            )
        },
    )
}
