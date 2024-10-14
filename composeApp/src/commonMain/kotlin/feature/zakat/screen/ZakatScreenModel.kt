package feature.zakat.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import core.data.DataState
import core.util.toLongDefault
import feature.prayertime.service.PrayerRepository
import feature.prayertime.service.model.PrayerTime
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ZakatScreenModel(
    private val repository: PrayerRepository,
) : ViewModel() {
    val mutableState = MutableStateFlow(State())
    val state: StateFlow<State> = mutableState.asStateFlow()

    data class State(
        var zakatResultState: ZakatDialogState = ZakatDialogState.Hide,
        var currentTime: PrayerTime? = null,
        var lastYearTime: PrayerTime? = null,
    ) {
        sealed class ZakatDialogState {
            object Hide : ZakatDialogState()

            data class Fitrah(
                var totalPeople: Long,
                var ricePrice: Long,
                var totalZakatInLiter: Double,
                var totalZakatInMoney: Double,
            ) : ZakatDialogState()

            data class Mal(
                var totalDebt: Long,
                var totalSaving: Long,
                var goldPrice: Long,
                var totalGoldInGram: Long,
                var totalGoldInMoney: Long,
                var totalAsset: Long,
                var totalNishab: Long,
                var totalZakat: Double,
                var isPossibleZakat: Boolean,
            ) : ZakatDialogState()
        }
    }

    fun getHijri() {
        viewModelScope.launch {
            repository.fetchTodayTomorrowPrayerTimes().collectLatest {
                if (it.data.isNotEmpty()) {
                    val currentTime = it.data.first()
                    val currentHijri = currentTime.hijri

                    repository
                        .fetchHijriMonthTimes(
                            month = currentHijri.month.monthNumber,
                            year = currentHijri.year - 1,
                        ).collectLatest { lastYear ->
                            when (lastYear) {
                                is DataState.Error -> {
                                }

                                DataState.Loading -> {}
                                is DataState.Success -> {
                                    val lastYearTime =
                                        lastYear.data.haqqDays
                                            .filterIsInstance<PrayerTime>()
                                            .first { time -> time.hijri.date == currentTime.hijri.date }
                                    mutableState.value =
                                        state.value.copy(
                                            currentTime = currentTime,
                                            lastYearTime = lastYearTime,
                                        )
                                }
                            }
                        }
                }
            }
        }
    }

    fun hideDialog() {
        viewModelScope.launch {
            mutableState.value =
                state.value.copy(
                    zakatResultState = State.ZakatDialogState.Hide,
                )
        }
    }

    fun calculateFitrah(
        valueTotalPeople: String,
        valueRicePrice: String,
    ) {
        viewModelScope.launch {
            val totalPeople = valueTotalPeople.toLongDefault()
            val ricePrice = valueRicePrice.toLongDefault()
            val totalZakatInLiter = (3.5 * totalPeople)
            val totalZakatInMoney = (totalZakatInLiter * ricePrice)

            mutableState.value =
                state.value.copy(
                    zakatResultState =
                        State.ZakatDialogState.Fitrah(
                            totalPeople = totalPeople,
                            ricePrice = ricePrice,
                            totalZakatInLiter = totalZakatInLiter,
                            totalZakatInMoney = totalZakatInMoney,
                        ),
                )
        }
    }

    fun calculateMal(
        valueTotalDebt: String,
        valueTotalSaving: String,
        valueGoldPrice: String,
        valueTotalGoldInGram: String,
    ) {
        viewModelScope.launch {
            val totalDebt = valueTotalDebt.toLongDefault()
            val totalSaving = valueTotalSaving.toLongDefault()
            val goldPrice = valueGoldPrice.toLongDefault()
            val totalGoldInGram = valueTotalGoldInGram.toLongDefault()
            val totalGoldInMoney = (totalGoldInGram * goldPrice)
            val totalAsset = (totalSaving + totalGoldInMoney - totalDebt)
            val totalNishab = (goldPrice * 85)
            val totalZakat = (totalAsset * 0.025)
            val isPossibleZakat = (totalAsset >= totalNishab)

            mutableState.value =
                state.value.copy(
                    zakatResultState =
                        State.ZakatDialogState.Mal(
                            totalDebt = totalDebt,
                            totalSaving = totalSaving,
                            goldPrice = goldPrice,
                            totalGoldInGram = totalGoldInGram,
                            totalGoldInMoney = totalGoldInMoney,
                            totalAsset = totalAsset,
                            totalNishab = totalNishab,
                            totalZakat = totalZakat,
                            isPossibleZakat = isPossibleZakat,
                        ),
                )
        }
    }
}
