package feature.zakat.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import core.ui.component.BaseButton
import core.ui.component.BaseDialog
import core.ui.component.BaseOutlineTextField
import core.ui.component.BaseSpacerVertical
import core.ui.component.BaseTabRow
import core.ui.component.BaseText
import core.ui.component.BaseTopAppBar
import core.util.CurrencyTransformation
import core.util.CurrencyTransformation.Companion.MAX_DIGITS
import core.util.formatAsCurrency
import feature.prayertime.service.model.PrayerTime
import haqq.composeapp.generated.resources.Res
import haqq.composeapp.generated.resources.close
import haqq.composeapp.generated.resources.copy
import haqq.composeapp.generated.resources.zakat_calculate
import haqq.composeapp.generated.resources.zakat_calculation_result
import haqq.composeapp.generated.resources.zakat_calculator
import haqq.composeapp.generated.resources.zakat_fitrah
import haqq.composeapp.generated.resources.zakat_fitrah_gold_price_label
import haqq.composeapp.generated.resources.zakat_fitrah_result
import haqq.composeapp.generated.resources.zakat_fitrah_rice_price_label
import haqq.composeapp.generated.resources.zakat_fitrah_total_debt_label
import haqq.composeapp.generated.resources.zakat_fitrah_total_gold_label
import haqq.composeapp.generated.resources.zakat_fitrah_total_people_label
import haqq.composeapp.generated.resources.zakat_fitrah_total_saving_label
import haqq.composeapp.generated.resources.zakat_mal
import haqq.composeapp.generated.resources.zakat_mal_haul
import haqq.composeapp.generated.resources.zakat_mal_result_not_possible
import haqq.composeapp.generated.resources.zakat_mal_result_possible
import kotlinx.serialization.Serializable
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Serializable
object ZakatNav

@Composable
fun ZakatScreen(onBackClick: () -> Unit) {
    val vm = koinViewModel<ZakatScreenModel>()
    val state by vm.state.collectAsState()
    val clipboardManager = LocalClipboardManager.current

    Scaffold(
        topBar = {
            BaseTopAppBar(
                title = stringResource(Res.string.zakat_calculator),
                onLeftButtonClick = {
                    onBackClick()
                },
            )
        },
    ) { paddingValues ->
        Column(
            modifier =
                Modifier.padding(paddingValues).fillMaxSize().verticalScroll(
                    rememberScrollState(),
                ),
        ) {
            val tabTitles =
                listOf(
                    stringResource(Res.string.zakat_fitrah),
                    stringResource(Res.string.zakat_mal),
                )
            val pagerState = rememberPagerState(pageCount = { tabTitles.size })

            BaseTabRow(
                pagerState = pagerState,
                tabTitles = tabTitles,
            )

            BaseSpacerVertical()

            HorizontalPager(
                state = pagerState,
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.Top,
            ) { index ->
                when (index) {
                    0 -> {
                        ZakatFitrahContent(
                            onCalculateClick = { valuePeople, valueRicePrice ->
                                vm.calculateFitrah(valuePeople, valueRicePrice)
                            },
                        )
                    }

                    else -> {
                        ZakatMalContent(
                            lastYearTime = state.lastYearTime,
                            onCalculateClick = { valueDebt, valueSaving, valueGoldPrice, valueTotalGoldInGram ->
                                vm.calculateMal(
                                    valueDebt,
                                    valueSaving,
                                    valueGoldPrice,
                                    valueTotalGoldInGram,
                                )
                            },
                        )
                    }
                }
            }
        }

        if (state.zakatResultState != ZakatScreenModel.State.ZakatDialogState.Hide) {
            val resultMessage = getResultMessage(state.zakatResultState)

            BaseDialog(
                onDismissRequest = { vm.hideDialog() },
                title = stringResource(Res.string.zakat_calculation_result),
                desc = resultMessage,
                negativeButtonText = stringResource(Res.string.close),
                positiveButtonText = stringResource(Res.string.copy),
                onPositiveClicked = {
                    clipboardManager.setText(AnnotatedString(resultMessage))
                    vm.hideDialog()
                },
            )
        }
    }

    LaunchedEffect(Unit) {
        vm.getHijri()
    }
}

@Composable
private fun ZakatFitrahContent(onCalculateClick: (valuePeople: String, valueRicePrice: String) -> Unit) {
    val valuePeople = remember { mutableStateOf("") }
    val valueRicePrice = remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        BaseOutlineTextField(
            modifier = Modifier.fillMaxWidth(),
            value = valuePeople.value,
            onValueChange = { newText ->
                valuePeople.value = newText.filter { it.isDigit() }.take(3)
            },
            visualTransformation = CurrencyTransformation(),
            label = stringResource(Res.string.zakat_fitrah_total_people_label),
            trailingClick = { valuePeople.value = "" },
            keyboardOptions =
                KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next,
                ),
        )

        BaseSpacerVertical()

        BaseOutlineTextField(
            modifier = Modifier.fillMaxWidth(),
            value = valueRicePrice.value,
            onValueChange = { newText ->
                valueRicePrice.value = newText.filter { it.isDigit() }.take(MAX_DIGITS)
            },
            visualTransformation = CurrencyTransformation(),
            prefix = "Rp",
            label = stringResource(Res.string.zakat_fitrah_rice_price_label),
            trailingClick = { valueRicePrice.value = "" },
            keyboardOptions =
                KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done,
                ),
        )

        Spacer(Modifier.weight(1f))

        BaseButton(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(Res.string.zakat_calculate),
            onClick = {
                onCalculateClick(
                    valuePeople.value,
                    valueRicePrice.value,
                )
            },
        )
    }
}

@Composable
private fun ZakatMalContent(
    lastYearTime: PrayerTime?,
    onCalculateClick: (
        valueDebt: String,
        valueSaving: String,
        valueGoldPrice: String,
        valueTotalGoldInGram: String,
    ) -> Unit,
) {
    val valueDebt = remember { mutableStateOf("") }
    val valueSaving = remember { mutableStateOf("") }
    val valueGoldPrice = remember { mutableStateOf("") }
    val valueTotalGoldInGram = remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        if (lastYearTime != null) {
            BaseText(
                stringResource(Res.string.zakat_mal_haul)
                    .replace(
                        "%1",
                        "${lastYearTime.hijri.fullDate} / ${lastYearTime.gregorian.fullDate}",
                    ),
            )
            BaseSpacerVertical()
        }

        BaseOutlineTextField(
            modifier = Modifier.fillMaxWidth(),
            value = valueDebt.value,
            onValueChange = { newText ->
                valueDebt.value = newText.filter { it.isDigit() }.take(MAX_DIGITS)
            },
            visualTransformation = CurrencyTransformation(),
            prefix = "Rp",
            label = stringResource(Res.string.zakat_fitrah_total_debt_label),
            trailingClick = { valueDebt.value = "" },
            keyboardOptions =
                KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next,
                ),
        )

        BaseSpacerVertical()

        BaseOutlineTextField(
            modifier = Modifier.fillMaxWidth(),
            value = valueSaving.value,
            onValueChange = { newText ->
                valueSaving.value = newText.filter { it.isDigit() }.take(MAX_DIGITS)
            },
            visualTransformation = CurrencyTransformation(),
            prefix = "Rp",
            label = stringResource(Res.string.zakat_fitrah_total_saving_label),
            trailingClick = { valueSaving.value = "" },
            keyboardOptions =
                KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next,
                ),
        )

        BaseSpacerVertical()

        BaseOutlineTextField(
            modifier = Modifier.fillMaxWidth(),
            value = valueTotalGoldInGram.value,
            onValueChange = { newText ->
                valueTotalGoldInGram.value = newText.filter { it.isDigit() }.take(4)
            },
            visualTransformation = CurrencyTransformation(),
            label = stringResource(Res.string.zakat_fitrah_total_gold_label),
            trailingClick = { valueTotalGoldInGram.value = "" },
            keyboardOptions =
                KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next,
                ),
        )

        BaseSpacerVertical()

        BaseOutlineTextField(
            modifier = Modifier.fillMaxWidth(),
            value = valueGoldPrice.value,
            onValueChange = { newText ->
                valueGoldPrice.value = newText.filter { it.isDigit() }.take(7)
            },
            visualTransformation = CurrencyTransformation(),
            prefix = "Rp",
            label = stringResource(Res.string.zakat_fitrah_gold_price_label),
            trailingClick = { valueGoldPrice.value = "" },
            keyboardOptions =
                KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done,
                ),
        )

        Spacer(Modifier.weight(1f))

        BaseButton(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(Res.string.zakat_calculate),
            onClick = {
                onCalculateClick(
                    valueDebt.value,
                    valueSaving.value,
                    valueGoldPrice.value,
                    valueTotalGoldInGram.value,
                )
            },
        )
    }
}

@Composable
private fun getResultMessage(dialogState: ZakatScreenModel.State.ZakatDialogState): String =
    when (dialogState) {
        ZakatScreenModel.State.ZakatDialogState.Hide -> ""
        is ZakatScreenModel.State.ZakatDialogState.Fitrah -> {
            val totalPeople = dialogState.totalPeople
            val ricePrice = dialogState.ricePrice.formatAsCurrency()
            val totalZakatInLiter = dialogState.totalZakatInLiter
            val totalZakatInMoney = dialogState.totalZakatInMoney.toInt().formatAsCurrency()

            stringResource(Res.string.zakat_fitrah_result)
                .replace("%1", "$totalPeople")
                .replace("%2", ricePrice)
                .replace("%3", "$totalZakatInLiter")
                .replace("%4", totalZakatInMoney)
        }

        is ZakatScreenModel.State.ZakatDialogState.Mal -> {
            val totalDebt = dialogState.totalDebt.formatAsCurrency()
            val totalSaving = dialogState.totalSaving.formatAsCurrency()
            val totalGoldInGram = dialogState.totalGoldInGram.formatAsCurrency(false)
            val totalAsset = dialogState.totalAsset.formatAsCurrency()
            val totalNishab = dialogState.totalNishab.formatAsCurrency()
            val totalZakat = dialogState.totalZakat.toInt().formatAsCurrency()
            val isPossibleZakat = dialogState.isPossibleZakat

            val resultString =
                if (isPossibleZakat) {
                    stringResource(Res.string.zakat_mal_result_possible)
                } else {
                    stringResource(Res.string.zakat_mal_result_not_possible)
                }

            resultString
                .replace("%1", totalDebt)
                .replace("%2", totalSaving)
                .replace("%3", totalGoldInGram)
                .replace("%4", totalAsset)
                .replace("%5", totalNishab)
                .replace("%6", totalZakat)
        }
    }
