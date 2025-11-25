package ooh.app.currencyexchanger.domain.repository

import ooh.app.currencyexchanger.domain.model.CurrencyInfo
import ooh.app.currencyexchanger.domain.model.CurrencyPair

interface CurrencyRepository {

    suspend fun fetchCurrencyInfoByCode(code: String): CurrencyInfo
    suspend fun insertCurrencyPair(pair: CurrencyPair)
    suspend fun getCurrencyPairs(): List<CurrencyPair>

}