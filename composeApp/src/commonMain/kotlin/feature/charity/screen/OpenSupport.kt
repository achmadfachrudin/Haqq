package feature.charity.screen

import AppConstant.URL_SUPPORT
import cafe.adriel.voyager.navigator.Navigator
import feature.other.service.mapper.getString
import feature.other.service.model.AppString
import feature.web.screen.WebScreen
import getPlatform
import openExternalLink

fun openSupport(navigator: Navigator) {
    if (getPlatform().isIOS) {
        openExternalLink(URL_SUPPORT)
    } else {
        navigator.push(
            WebScreen(
                url = URL_SUPPORT,
                title = AppString.SUPPORT_TITLE.getString(),
            ),
        )
    }
}
