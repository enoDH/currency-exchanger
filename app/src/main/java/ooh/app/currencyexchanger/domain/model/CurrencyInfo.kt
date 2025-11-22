package ooh.app.currencyexchanger.domain.model

data class CurrencyInfo(
    val result: String = "",
    val provider: String = "",
    val documentation: String = "",
    val terms_of_use: String = "",
    val time_last_update_unix: Long = 0,
    val time_last_update_utc: String = "",
    val time_next_update_unix: Long = 0,
    val time_next_update_utc: String = "",
    val time_eol_unix: Int = 0,
    val base_code: String = "",
    val rates: Map<String, Double> = emptyMap<String, Double>()
)
