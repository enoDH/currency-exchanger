package ooh.app.currencyexchanger.domain.usecase

import ooh.app.currencyexchanger.domain.model.CurrencyPair
import ooh.app.currencyexchanger.domain.repository.CurrencyRepository
import javax.inject.Inject

class GetCurrencyPairUseCase @Inject constructor(private val currencyRep: CurrencyRepository) {
    suspend fun getCurrencyPairs(): List<CurrencyPair>{
        return currencyRep.getCurrencyPairs()
    }
}