package feature.prayertime.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.currentCompositeKeyHash
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinNavigatorScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import core.ui.component.BaseText
import core.ui.component.BaseTopAppBar
import core.ui.theme.getHaqqTypography
import feature.other.service.AppRepository
import feature.other.service.mapper.getString
import feature.other.service.model.AppString
import feature.prayertime.service.model.PrayerTime
import feature.prayertime.service.model.Salah
import org.koin.mp.KoinPlatform

class MonthlyPrayerTimeScreen : Screen {
    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val screenModel = navigator.koinNavigatorScreenModel<MonthlyScreenModel>()
        val appRepository = KoinPlatform.getKoin().get<AppRepository>()
        val scope = rememberCoroutineScope()
        val snackbarHostState = remember { SnackbarHostState() }

        val haqqCalendar = screenModel.state.value.haqqCalendar

        Scaffold(
            topBar = {
                BaseTopAppBar(
                    title = "${haqqCalendar.haqqMonth.monthName} ${haqqCalendar.haqqYear}",
                    onLeftButtonClick = {
                        navigator.pop()
                    },
                )
            },
            snackbarHost = {
                SnackbarHost(hostState = snackbarHostState)
            },
        ) { paddingValues ->
            LazyColumn(modifier = Modifier.padding(paddingValues).fillMaxSize()) {
                item {
                    val locationName = appRepository.getSetting().location.name
                    BaseText(
                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                        text = "$locationName (${haqqCalendar.timeZone})",
                    )
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

        LaunchedEffect(currentCompositeKeyHash) {
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
                text = AppString.PRAYER_FAJR.getString(),
                horizontalArrangement = Arrangement.Center,
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
}
