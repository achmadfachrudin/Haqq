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
import feature.prayertime.service.model.GuidanceType
import getPlatform
import haqq.composeapp.generated.resources.Res
import haqq.composeapp.generated.resources.calendar_fasting
import haqq.composeapp.generated.resources.learn_fasting
import haqq.composeapp.generated.resources.learn_hajj_umrah
import haqq.composeapp.generated.resources.learn_salah
import haqq.composeapp.generated.resources.learn_thaharah_junub
import haqq.composeapp.generated.resources.learn_thaharah_tayammum
import haqq.composeapp.generated.resources.learn_thaharah_wudhu
import haqq.composeapp.generated.resources.learn_zakat_fitrah
import haqq.composeapp.generated.resources.learn_zakat_mal
import haqq.composeapp.generated.resources.pray_fasting
import haqq.composeapp.generated.resources.pray_hajj_umrah
import haqq.composeapp.generated.resources.pray_salah
import haqq.composeapp.generated.resources.pray_thaharah
import haqq.composeapp.generated.resources.pray_zakat
import haqq.composeapp.generated.resources.prayer_time_title
import haqq.composeapp.generated.resources.qibla_title
import haqq.composeapp.generated.resources.zakat_calculator
import org.jetbrains.compose.resources.stringResource
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
                text = stringResource(Res.string.pray_thaharah),
                horizontalArrangement = Arrangement.Start,
            )
        }

        item {
            BaseMenuLargeCard(
                title = stringResource(Res.string.learn_thaharah_wudhu),
                onItemClick = { onVideoClick("5LTFZ7kT36A") },
            )
        }

        item {
            BaseMenuLargeCard(
                title = stringResource(Res.string.learn_thaharah_tayammum),
                onItemClick = { onVideoClick("2Vp5h4sY3l4") },
            )
        }

        item {
            BaseMenuLargeCard(
                title = stringResource(Res.string.learn_thaharah_junub),
                onItemClick = { onVideoClick("GM43oazKk94") },
            )
        }

        item(
            span = { GridItemSpan(this.maxLineSpan) },
        ) {
            BaseDivider(0)
            BaseTitle(
                modifier = Modifier.padding(top = 16.dp),
                text = stringResource(Res.string.pray_salah),
                horizontalArrangement = Arrangement.Start,
            )
        }

        item {
            BaseMenuLargeCard(
                title = stringResource(Res.string.learn_salah),
                onItemClick = { onVideoClick("LH4Te_KiILY") },
            )
        }

        item {
            BaseMenuLargeCard(
                title = stringResource(Res.string.prayer_time_title),
                onItemClick = { onTimeClick() },
            )
        }

        if (appRepository.getSetting().isLocationValid && getPlatform().isAndroid) {
            item {
                BaseMenuLargeCard(
                    title = stringResource(Res.string.qibla_title),
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
                text = stringResource(Res.string.pray_fasting),
                horizontalArrangement = Arrangement.Start,
            )
        }

        item {
            BaseMenuLargeCard(
                title = stringResource(Res.string.learn_fasting),
                onItemClick = { onVideoClick("BRJDPMeevQw") },
            )
        }

        item {
            BaseMenuLargeCard(
                title = stringResource(Res.string.calendar_fasting),
                onItemClick = { onCalendarClick() },
            )
        }

        item(
            span = { GridItemSpan(this.maxLineSpan) },
        ) {
            BaseDivider(0)
            BaseTitle(
                modifier = Modifier.padding(top = 16.dp),
                text = stringResource(Res.string.pray_zakat),
                horizontalArrangement = Arrangement.Start,
            )
        }

        item {
            BaseMenuLargeCard(
                title = stringResource(Res.string.learn_zakat_fitrah),
                onItemClick = { onVideoClick("JLbpPINCMGs") },
            )
        }

        item {
            BaseMenuLargeCard(
                title = stringResource(Res.string.learn_zakat_mal),
                onItemClick = { onVideoClick("yEsGb9VUFrg") },
            )
        }

        item {
            BaseMenuLargeCard(
                title = stringResource(Res.string.zakat_calculator),
                onItemClick = { onCalculateZakatClick() },
            )
        }

        item(
            span = { GridItemSpan(this.maxLineSpan) },
        ) {
            BaseDivider(0)
            BaseTitle(
                modifier = Modifier.padding(top = 16.dp),
                text = stringResource(Res.string.pray_hajj_umrah),
                horizontalArrangement = Arrangement.Start,
            )
        }

        item {
            BaseMenuLargeCard(
                title = stringResource(Res.string.learn_hajj_umrah),
                onItemClick = { onGuideClick(GuidanceType.HAJJ) },
            )
        }
    }
}
