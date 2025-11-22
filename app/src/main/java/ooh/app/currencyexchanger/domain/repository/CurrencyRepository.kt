package ooh.app.currencyexchanger.domain.repository

import ooh.app.currencyexchanger.domain.model.CurrencyInfo

interface CurrencyRepository {

    suspend fun fetchCurrencyInfoByCode(code: String): CurrencyInfo

}