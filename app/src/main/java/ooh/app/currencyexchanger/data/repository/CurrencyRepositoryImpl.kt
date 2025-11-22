package ooh.app.currencyexchanger.data.repository

import ooh.app.currencyexchanger.data.api.ApiService
import ooh.app.currencyexchanger.domain.model.CurrencyInfo
import ooh.app.currencyexchanger.domain.repository.CurrencyRepository
import javax.inject.Inject

class CurrencyRepositoryImpl @Inject constructor(private val api: ApiService) : CurrencyRepository {

    override suspend fun fetchCurrencyInfoByCode(code: String): CurrencyInfo {
        return api.getCurrencyInfoByCode(code)
    }
}