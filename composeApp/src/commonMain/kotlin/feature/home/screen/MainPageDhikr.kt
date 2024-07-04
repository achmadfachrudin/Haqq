package feature.home.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import core.ui.component.BaseDivider
import core.ui.component.BaseMenuLargeCard
import core.ui.component.BaseTitle
import feature.dhikr.service.model.DhikrType
import feature.other.service.mapper.getString
import feature.other.service.model.AppString

@Composable
internal fun MainPageDhikr(
    onDzikirClick: (dhikrType: DhikrType) -> Unit,
    onDuaQuranClick: () -> Unit,
    onDuaSunnahClick: () -> Unit,
    onAsmaClick: () -> Unit,
) {
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
                AppString.DHIKR_TITLE.getString(),
                horizontalArrangement = Arrangement.Start,
            )
        }

        item {
            BaseMenuLargeCard(
                title = AppString.DHIKR_MORNING_TITLE.getString(),
                onItemClick = { onDzikirClick(DhikrType.MORNING) },
            )
        }

        item {
            BaseMenuLargeCard(
                title = AppString.DHIKR_AFTERNOON_TITLE.getString(),
                onItemClick = { onDzikirClick(DhikrType.AFTERNOON) },
            )
        }

        item {
            BaseMenuLargeCard(
                title = AppString.DHIKR_PRAY_TITLE.getString(),
                onItemClick = { onDzikirClick(DhikrType.PRAY) },
            )
        }

        item {
            BaseMenuLargeCard(
                title = AppString.DHIKR_SLEEP_TITLE.getString(),
                onItemClick = { onDzikirClick(DhikrType.SLEEP) },
            )
        }

        item {
            BaseMenuLargeCard(
                title = AppString.DHIKR_RUQYAH_TITLE.getString(),
                onItemClick = { onDzikirClick(DhikrType.RUQYAH) },
            )
        }

        item(
            span = { GridItemSpan(this.maxLineSpan) },
        ) {
            BaseDivider(0)
            BaseTitle(
                AppString.DUA.getString(),
                horizontalArrangement = Arrangement.Start,
            )
        }

        item {
            BaseMenuLargeCard(
                title = AppString.DUA_QURAN.getString(),
                onItemClick = { onDuaQuranClick() },
            )
        }

        item {
            BaseMenuLargeCard(
                title = AppString.DUA_SUNNAH.getString(),
                onItemClick = { onDuaSunnahClick() },
            )
        }

        item(
            span = { GridItemSpan(this.maxLineSpan) },
        ) {
            BaseDivider(0)
            BaseTitle(
                AppString.OTHER_TITLE.getString(),
                horizontalArrangement = Arrangement.Start,
            )
        }

        item {
            BaseMenuLargeCard(
                title = AppString.ASMAUL_HUSNA_TITLE.getString(),
                onItemClick = { onAsmaClick() },
            )
        }
    }
}
