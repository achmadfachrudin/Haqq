package core.ui.component

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import core.ui.theme.getHaqqTypography
import feature.home.screen.MainScreenModel
import feature.other.service.mapper.getString
import feature.other.service.model.AppString
import haqq.composeapp.generated.resources.Res
import haqq.composeapp.generated.resources.battery_charging
import haqq.composeapp.generated.resources.book
import haqq.composeapp.generated.resources.check_square
import haqq.composeapp.generated.resources.clock
import haqq.composeapp.generated.resources.home
import org.jetbrains.compose.resources.painterResource

@Composable
fun RowScope.TabNavigationItem(
    pageState: MainScreenModel.PageState,
    pageSelected: MainScreenModel.PageState,
    onClick: () -> Unit,
) {
    val isSelected = pageState == pageSelected

    val title =
        when (pageState) {
            MainScreenModel.PageState.HOME -> AppString.HOME_TITLE.getString()
            MainScreenModel.PageState.DHIKR -> AppString.DHIKR_TITLE.getString()
            MainScreenModel.PageState.QURAN -> AppString.QURAN_TITLE.getString()
            MainScreenModel.PageState.PRAYER -> AppString.PRAYER_TITLE.getString()
            MainScreenModel.PageState.ACTIVITY -> AppString.ACTIVITY_TITLE.getString()
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
