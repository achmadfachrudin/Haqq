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
    pageState: MainScreenModel.PageState,
    pageSelected: MainScreenModel.PageState,
    onClick: () -> Unit,
) {
    val isSelected = pageState == pageSelected

    val title =
        when (pageState) {
            MainScreenModel.PageState.HOME -> stringResource(Res.string.home_title)
            MainScreenModel.PageState.DHIKR -> stringResource(Res.string.dhikr_title)
            MainScreenModel.PageState.QURAN -> stringResource(Res.string.quran_title)
            MainScreenModel.PageState.PRAYER -> stringResource(Res.string.prayer_title)
            MainScreenModel.PageState.ACTIVITY -> stringResource(Res.string.activity_title)
        }

    val icon =
        when (pageState) {
            MainScreenModel.PageState.HOME -> Res.drawable.home
            MainScreenModel.PageState.DHIKR -> Res.drawable.battery_charging
            MainScreenModel.PageState.QURAN -> Res.drawable.book
            MainScreenModel.PageState.PRAYER -> Res.drawable.clock
            MainScreenModel.PageState.ACTIVITY -> Res.drawable.check_square
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
