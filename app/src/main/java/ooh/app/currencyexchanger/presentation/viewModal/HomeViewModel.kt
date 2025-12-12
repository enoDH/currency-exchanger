package ooh.app.currencyexchanger.presentation.viewModal

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ooh.app.currencyexchanger.domain.model.CurrencyPair
import ooh.app.currencyexchanger.domain.usecase.CalculateExchangeRateUseCase
import ooh.app.currencyexchanger.domain.usecase.GetCurrencyPairUseCase
import ooh.app.currencyexchanger.domain.usecase.GetCurrencyRatesUseCase
import ooh.app.currencyexchanger.domain.usecase.SaveCurrencyPairUseCase
import ooh.app.currencyexchanger.presentation.ui.home.HomeUiState
import javax.inject.Inject
import kotlin.math.round

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val currencyUseCase: GetCurrencyRatesUseCase,
    private val calculateExchangeRateUseCase: CalculateExchangeRateUseCase,
    private val saveCurrencyPairUseCase: SaveCurrencyPairUseCase,
    private val getCurrencyPairUseCase: GetCurrencyPairUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    private val _pairs = MutableStateFlow(emptyList<CurrencyPair>())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        fetch()
    }

    private fun fetch() {
        viewModelScope.launch {
            getPairs()

            runCatching {
                currencyUseCase.fetchCurrencyRatesByCode(_uiState.value.baseCurrency)
            }.onSuccess { currencies ->
                _uiState.update {
                    it.copy(
                        baseCodes = currencies.rates.map { (key, value) -> key },
                        targetRate = currencies.rates[_uiState.value.targetCurrency]!!.toDouble(),
                        currencyRates = currencies
                    )
                }
            }.onFailure { e ->
                Log.e("TEST_CURRENCY", e.message.toString())
            }

            changeBaseAmount(_uiState.value.baseAmount)
        }
    }

    fun changeBaseAmount(amount: Double) {
        _uiState.update {
            it.copy(
                baseAmount = round(amount * 100) / 100, targetAmount = round(
                    calculateExchangeRateUseCase.exchange(
                        amount, _uiState.value.targetRate, true
                    ) * 100
                ) / 100
            )
        }
    }

    fun changeTargetAmount(amount: Double) {
        _uiState.update {
            it.copy(
                baseAmount = round(
                    calculateExchangeRateUseCase.exchange(
                        amount, _uiState.value.targetRate, false
                    ) * 100
                ) / 100, targetAmount = round(amount * 100) / 100
            )
        }
    }

    fun changeBaseCurrency(code: String) {
        _uiState.update {
            it.copy(
                baseCurrency = code,
                showButtonSave = true
            )
        }

        fetch()
    }

    fun changeTargetCurrency(code: String) {
        _uiState.update {
            it.copy(
                targetCurrency = code,
                showButtonSave = true
            )
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

    fun savePair() {
        viewModelScope.launch {
            saveCurrencyPairUseCase.saveCurrencyPair(
                CurrencyPair(
                    baseCurrency = _uiState.value.baseCurrency,
                    targetCurrency = _uiState.value.targetCurrency
                )
            )

            _uiState.update {
                it.copy(
                    showButtonSave = false
                )
            }

            getPairs()
        }
    }

    fun displayFavoritePairs() {
        _uiState.update {
            it.copy(
                displayFavoritePairs = true
            )
        }
        getPairs()
    }

    fun displayPairs() {
        _uiState.update {
            it.copy(
                displayFavoritePairs = false
            )
        }
    }

    private fun getPairs() {
        viewModelScope.launch {
            _pairs.update {
                getCurrencyPairUseCase.getCurrencyPairs()
            }

            fetchCurrencyPairs()
        }
    }

    private fun fetchCurrencyPairs() {
        viewModelScope.launch {
            val pairs: MutableList<CurrencyPair> = mutableListOf()

            _pairs.value.forEach {
                pairs.add(
                    CurrencyPair(
                        baseCurrency = it.baseCurrency,
                        targetCurrency = it.targetCurrency,
                        targetRate = pairRate(it.baseCurrency, it.targetCurrency)
                    )
                )
            }

            _uiState.update {
                it.copy(
                    pairs = pairs.toList()
                )
            }
        }

    }

    private suspend fun pairRate(base: String, target: String): Double {
        return runCatching {
            withContext(Dispatchers.IO) {
                currencyUseCase.fetchCurrencyRatesByCode(base)
            }
        }.fold(onSuccess = { currencies ->
            currencies.rates[target] ?: 0.0
        }, onFailure = { e ->
            0.0
        })
    }

}