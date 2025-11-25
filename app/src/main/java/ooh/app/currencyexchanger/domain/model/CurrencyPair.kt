package ooh.app.currencyexchanger.domain.model

data class CurrencyPair(
    val baseCurrency: String = "USD",
    val targetCurrency: String = "UAH",
    val targetRate: Double = 0.0
)
