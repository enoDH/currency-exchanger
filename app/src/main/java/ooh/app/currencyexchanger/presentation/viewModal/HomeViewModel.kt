package ooh.app.currencyexchanger.presentation.viewModal

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ooh.app.currencyexchanger.domain.model.CurrencyInfo
import ooh.app.currencyexchanger.domain.usecase.CalculateExchangeRateUseCase
import ooh.app.currencyexchanger.domain.usecase.GetCurrencyRatesUseCase
import ooh.app.currencyexchanger.presentation.ui.home.HomeUiState
import javax.inject.Inject
import kotlin.math.round

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val currencyUseCase: GetCurrencyRatesUseCase,
    private val calculateExchangeRateUseCase: CalculateExchangeRateUseCase
) :
    ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    private var _currencyInfo = MutableStateFlow(CurrencyInfo())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        fetch()
    }

    private fun fetch() {
        viewModelScope.launch {

            runCatching {
                currencyUseCase.fetchCurrencyRatesByCode(_uiState.value.baseCurrency)
            }.onSuccess { currencies ->
                _currencyInfo.update {
                    currencies
                }
                _uiState.update {
                    it.copy(
                        baseCodes = currencies.rates.map { (key, value) -> key },
                        targetRate = currencies.rates[_uiState.value.targetCurrency]!!.toDouble()
                    )
                }
            }.onFailure { e ->
                Log.e("TEST_CURRENCY", e.message.toString())
            }


            _currencyInfo.update {
                it.copy(
                    currencyUseCase.fetchCurrencyRatesByCode(_uiState.value.baseCurrency).toString()
                )
            }

            Log.e(
                "TEST_CURRENCY",
                currencyUseCase.fetchCurrencyRatesByCode(_uiState.value.baseCurrency).toString()
            )
            changeBaseAmount(_uiState.value.baseAmount)
        }
    }

    @SuppressLint("DefaultLocale")
    fun changeBaseAmount(amount: Double) {
        _uiState.update {
            it.copy(
                baseAmount = round(amount * 100) / 100,
                targetAmount = round(
                    calculateExchangeRateUseCase.exchange(
                        amount,
                        _uiState.value.targetRate,
                        true
                    ) * 100
                ) / 100
            )
        }
    }

    @SuppressLint("DefaultLocale")
    fun changeTargetAmount(amount: Double) {
        _uiState.update {
            it.copy(
                baseAmount = round(
                    calculateExchangeRateUseCase.exchange(
                        amount,
                        _uiState.value.targetRate,
                        false
                    ) * 100
                ) / 100,
                targetAmount = round(amount * 100) / 100
            )
        }
    }

    fun changeBaseCurrency(code: String) {
        _uiState.update {
            it.copy(baseCurrency = code)
        }

        fetch()
    }

    fun changeTargetCurrency(code: String) {
        _uiState.update {
            it.copy(targetCurrency = code)
        }

        fetch()
    }

    fun changeBaseExpand() {
        _uiState.update {
            it.copy(baseCurrencyExpand = !_uiState.value.baseCurrencyExpand)
        }
    }

    fun changeTargetExpand() {
        _uiState.update {
            it.copy(targetCurrencyExpand = !_uiState.value.targetCurrencyExpand)
        }
    }
}