package ooh.app.currencyexchanger.domain.usecase

import ooh.app.currencyexchanger.domain.model.CurrencyInfo
import ooh.app.currencyexchanger.domain.repository.CurrencyRepository
import javax.inject.Inject

class GetCurrencyRatesUseCase @Inject constructor(private val currencyRep: CurrencyRepository) {

    suspend fun fetchCurrencyRatesByCode(code: String): CurrencyInfo{
        return currencyRep.fetchCurrencyInfoByCode(code)
    }
}