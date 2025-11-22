package ooh.app.currencyexchanger.presentation.ui.home

data class HomeUiState(
    val baseCurrency: String = "USD",
    val targetCurrency: String = "EUR",
    val baseCurrencyExpand: Boolean = false,
    val targetCurrencyExpand: Boolean = false,
    val targetRate: Double = 0.0,
    val baseAmount: Double = 0.0,
    val targetAmount: Double = 0.0,
    val baseCodes: List<String> = emptyList()
)
