package feature.charity.screen

import AppConstant.URL_SUPPORT
import androidx.navigation.NavHostController
import feature.other.service.mapper.getString
import feature.other.service.model.AppString
import feature.web.screen.WebNav
import getPlatform
import openExternalLink

fun openSupport2(navHostController: NavHostController) {
    if (getPlatform().isIOS) {
        openExternalLink(URL_SUPPORT)
    } else {
        navHostController.navigate(
            WebNav(
                url = URL_SUPPORT,
                title = AppString.SUPPORT_TITLE.getString(),
            ),
        )
    }
}
