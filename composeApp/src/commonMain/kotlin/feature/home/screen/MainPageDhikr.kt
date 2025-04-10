package feature.home.screen

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
import feature.dhikr.service.model.DhikrType
import haqq.composeapp.generated.resources.Res
import haqq.composeapp.generated.resources.asmaul_husna_title
import haqq.composeapp.generated.resources.dhikr_afternoon_title
import haqq.composeapp.generated.resources.dhikr_morning_title
import haqq.composeapp.generated.resources.dhikr_pray_title
import haqq.composeapp.generated.resources.dhikr_ruqyah_title
import haqq.composeapp.generated.resources.dhikr_sleep_title
import haqq.composeapp.generated.resources.dhikr_title
import haqq.composeapp.generated.resources.dua
import haqq.composeapp.generated.resources.dua_quran
import haqq.composeapp.generated.resources.dua_sunnah
import haqq.composeapp.generated.resources.other_title
import org.jetbrains.compose.resources.stringResource

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
                text = stringResource(Res.string.dhikr_title),
                horizontalArrangement = Arrangement.Start,
            )
        }

        item {
            BaseMenuLargeCard(
                title = stringResource(Res.string.dhikr_morning_title),
                onItemClick = { onDzikirClick(DhikrType.MORNING) },
            )
        }

        item {
            BaseMenuLargeCard(
                title = stringResource(Res.string.dhikr_afternoon_title),
                onItemClick = { onDzikirClick(DhikrType.AFTERNOON) },
            )
        }

        item {
            BaseMenuLargeCard(
                title = stringResource(Res.string.dhikr_pray_title),
                onItemClick = { onDzikirClick(DhikrType.PRAY) },
            )
        }

        item {
            BaseMenuLargeCard(
                title = stringResource(Res.string.dhikr_sleep_title),
                onItemClick = { onDzikirClick(DhikrType.SLEEP) },
            )
        }

        item {
            BaseMenuLargeCard(
                title = stringResource(Res.string.dhikr_ruqyah_title),
                onItemClick = { onDzikirClick(DhikrType.RUQYAH) },
            )
        }

        item(
            span = { GridItemSpan(this.maxLineSpan) },
        ) {
            BaseDivider(0)
            BaseTitle(
                modifier = Modifier.padding(top = 16.dp),
                text = stringResource(Res.string.dua),
                horizontalArrangement = Arrangement.Start,
            )
        }

        item {
            BaseMenuLargeCard(
                title = stringResource(Res.string.dua_quran),
                onItemClick = { onDuaQuranClick() },
            )
        }

        item {
            BaseMenuLargeCard(
                title = stringResource(Res.string.dua_sunnah),
                onItemClick = { onDuaSunnahClick() },
            )
        }

        item(
            span = { GridItemSpan(this.maxLineSpan) },
        ) {
            BaseDivider(0)
            BaseTitle(
                modifier = Modifier.padding(top = 16.dp),
                text = stringResource(Res.string.other_title),
                horizontalArrangement = Arrangement.Start,
            )
        }

        item {
            BaseMenuLargeCard(
                title = stringResource(Res.string.asmaul_husna_title),
                onItemClick = { onAsmaClick() },
            )
        }
    }
}
