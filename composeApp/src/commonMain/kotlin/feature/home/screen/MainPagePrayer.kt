package feature.home.screen

import PlatformPage
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import core.ui.component.BaseDivider
import core.ui.component.BaseMenuLargeCard
import core.ui.component.BaseTitle
import feature.other.service.AppRepository
import feature.other.service.mapper.getString
import feature.other.service.model.AppString
import feature.prayertime.service.model.GuidanceType
import getPlatform
import org.koin.mp.KoinPlatform

@Composable
internal fun MainPagePrayer(
    onVideoClick: (url: String) -> Unit,
    onOpenActivity: (platformPage: PlatformPage) -> Unit,
    onTimeClick: () -> Unit,
    onCalendarClick: () -> Unit,
    onCalculateZakatClick: () -> Unit,
    onGuideClick: (guidanceType: GuidanceType) -> Unit,
) {
    val appRepository = KoinPlatform.getKoin().get<AppRepository>()

    LazyVerticalGrid(
        modifier = Modifier.fillMaxSize(),
        columns = GridCells.Fixed(3),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        item(
            span = { GridItemSpan(this.maxLineSpan) },
        ) {
            BaseTitle(
                AppString.PRAY_THAHARAH.getString(),
                horizontalArrangement = Arrangement.Start,
            )
        }

        item {
            BaseMenuLargeCard(
                title = AppString.LEARN_THAHARAH_WUDHU.getString(),
                onItemClick = { onVideoClick("5LTFZ7kT36A") },
            )
        }

        item {
            BaseMenuLargeCard(
                title = AppString.LEARN_THAHARAH_TAYAMMUM.getString(),
                onItemClick = { onVideoClick("2Vp5h4sY3l4") },
            )
        }

        item {
            BaseMenuLargeCard(
                title = AppString.LEARN_THAHARAH_JUNUB.getString(),
                onItemClick = { onVideoClick("GM43oazKk94") },
            )
        }

        item(
            span = { GridItemSpan(this.maxLineSpan) },
        ) {
            BaseDivider(0)
            BaseTitle(
                modifier = Modifier.padding(top = 16.dp),
                text = AppString.PRAY_SALAH.getString(),
                horizontalArrangement = Arrangement.Start,
            )
        }

        item {
            BaseMenuLargeCard(
                title = AppString.LEARN_SALAH.getString(),
                onItemClick = { onVideoClick("LH4Te_KiILY") },
            )
        }

        item {
            BaseMenuLargeCard(
                title = AppString.PRAYER_TIME_TITLE.getString(),
                onItemClick = { onTimeClick() },
            )
        }

        if (appRepository.getSetting().isLocationValid && getPlatform().isAndroid) {
            item {
                BaseMenuLargeCard(
                    title = AppString.QIBLA_TITLE.getString(),
                    onItemClick = {
                        onOpenActivity(PlatformPage.QIBLA)
                    },
                )
            }
        }

        item(
            span = { GridItemSpan(this.maxLineSpan) },
        ) {
            BaseDivider(0)
            BaseTitle(
                modifier = Modifier.padding(top = 16.dp),
                text = AppString.PRAY_FASTING.getString(),
                horizontalArrangement = Arrangement.Start,
            )
        }

        item {
            BaseMenuLargeCard(
                title = AppString.LEARN_FASTING.getString(),
                onItemClick = { onVideoClick("BRJDPMeevQw") },
            )
        }

        item {
            BaseMenuLargeCard(
                title = AppString.CALENDAR_FASTING.getString(),
                onItemClick = { onCalendarClick() },
            )
        }

        item(
            span = { GridItemSpan(this.maxLineSpan) },
        ) {
            BaseDivider(0)
            BaseTitle(
                modifier = Modifier.padding(top = 16.dp),
                text = AppString.PRAY_ZAKAT.getString(),
                horizontalArrangement = Arrangement.Start,
            )
        }

        item {
            BaseMenuLargeCard(
                title = AppString.LEARN_ZAKAT_FITRAH.getString(),
                onItemClick = { onVideoClick("JLbpPINCMGs") },
            )
        }

        item {
            BaseMenuLargeCard(
                title = AppString.LEARN_ZAKAT_MAL.getString(),
                onItemClick = { onVideoClick("yEsGb9VUFrg") },
            )
        }

        item {
            BaseMenuLargeCard(
                title = AppString.ZAKAT_CALCULATOR.getString(),
                onItemClick = { onCalculateZakatClick() },
            )
        }

        item(
            span = { GridItemSpan(this.maxLineSpan) },
        ) {
            BaseDivider(0)
            BaseTitle(
                modifier = Modifier.padding(top = 16.dp),
                text = AppString.PRAY_HAJJ_UMRAH.getString(),
                horizontalArrangement = Arrangement.Start,
            )
        }

        item {
            BaseMenuLargeCard(
                title = AppString.LEARN_HAJJ_UMRAH.getString(),
                onItemClick = { onGuideClick(GuidanceType.HAJJ) },
            )
        }
    }
}
