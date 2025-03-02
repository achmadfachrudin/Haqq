package feature.prayertime.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import core.ui.component.BaseText
import core.ui.component.BaseTitle
import core.ui.theme.getHaqqTypography
import feature.other.service.AppRepository
import feature.other.service.mapper.getString
import feature.other.service.model.AppString
import feature.prayertime.service.model.HaqqCalendar
import feature.prayertime.service.model.PrayerTime
import feature.prayertime.service.model.Salah
import org.koin.mp.KoinPlatform

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PrayerTimeMonthlyBottomSheet(haqqCalendar: HaqqCalendar) {
    val appRepository = KoinPlatform.getKoin().get<AppRepository>()
    val locationName = appRepository.getSetting().location.name

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        item {
            Column(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                BaseTitle(
                    text = "${haqqCalendar.haqqMonth.monthName} ${haqqCalendar.haqqYear}",
                )
                BaseText(
                    text = "$locationName (${haqqCalendar.timeZone})",
                )
            }
        }

        stickyHeader {
            Surface {
                LabelCard()
            }
        }

        itemsIndexed(haqqCalendar.haqqDays.filterIsInstance<PrayerTime>()) { index, day ->
            PrayerTimeCard(index, day)
        }
    }
}

@Composable
private fun LabelCard() {
    val contentColor = MaterialTheme.colorScheme.primary
    val textStyle = getHaqqTypography().bodyMedium

    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
    ) {
        BaseText(
            modifier = Modifier.weight(0.4f),
            text = " ",
            color = contentColor,
            style = textStyle,
        )
        BaseText(
            modifier = Modifier.weight(1f),
            text = AppString.PRAYER_IMSAK.getString(),
            horizontalArrangement = Arrangement.Center,
            color = contentColor,
            style = textStyle,
        )
        BaseText(
            modifier = Modifier.weight(1f),
            text = AppString.PRAYER_FAJR.getString(),
            horizontalArrangement = Arrangement.Center,
            color = contentColor,
            style = textStyle,
        )
        BaseText(
            modifier = Modifier.weight(1f),
            text = AppString.PRAYER_SUNRISE.getString(),
            horizontalArrangement = Arrangement.Center,
            color = contentColor,
            style = textStyle,
        )
        BaseText(
            modifier = Modifier.weight(1f),
            text = AppString.PRAYER_DHUHR.getString(),
            horizontalArrangement = Arrangement.Center,
            color = contentColor,
            style = textStyle,
        )
        BaseText(
            modifier = Modifier.weight(1f),
            text = AppString.PRAYER_ASR.getString(),
            horizontalArrangement = Arrangement.Center,
            color = contentColor,
            style = textStyle,
        )
        BaseText(
            modifier = Modifier.weight(1.1f),
            text = AppString.PRAYER_MAGHRIB.getString(),
            horizontalArrangement = Arrangement.Center,
            color = contentColor,
            style = textStyle,
        )
        BaseText(
            modifier = Modifier.weight(1f),
            text = AppString.PRAYER_ISHA.getString(),
            horizontalArrangement = Arrangement.Center,
            color = contentColor,
            style = textStyle,
        )
    }
}

@Composable
private fun PrayerTimeCard(
    index: Int,
    day: PrayerTime,
) {
    val backgroundColor =
        if (index % 2 == 0) {
            MaterialTheme.colorScheme.secondary
        } else {
            MaterialTheme.colorScheme.surface
        }

    val contentColor =
        if (index % 2 == 0) {
            MaterialTheme.colorScheme.onSecondary
        } else {
            MaterialTheme.colorScheme.onSurface
        }

    val textStyle = getHaqqTypography().bodyMedium

    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .background(backgroundColor)
                .padding(vertical = 8.dp),
    ) {
        BaseText(
            modifier = Modifier.weight(0.4f),
            text = "${day.hijri.date}",
            horizontalArrangement = Arrangement.Center,
            color = contentColor,
            style = textStyle,
        )

        day.mapStringTime.forEach {
            val weight = if (it.key == Salah.MAGHRIB) 1.1f else 1f
            BaseText(
                modifier = Modifier.weight(weight),
                text = it.value.cleanTime(),
                horizontalArrangement = Arrangement.Center,
                color = contentColor,
                style = textStyle,
            )
        }
    }
}

private fun String.cleanTime(): String {
    val regex = "[^\\d:]".toRegex()

    return this.replace(regex, "")
}
