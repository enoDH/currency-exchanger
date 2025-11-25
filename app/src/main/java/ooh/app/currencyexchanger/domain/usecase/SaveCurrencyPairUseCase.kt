package ooh.app.currencyexchanger.domain.usecase

import ooh.app.currencyexchanger.domain.model.CurrencyPair
import ooh.app.currencyexchanger.domain.repository.CurrencyRepository
import javax.inject.Inject

class SaveCurrencyPairUseCase @Inject constructor(private val repo: CurrencyRepository) {
    suspend fun saveCurrencyPair(pair: CurrencyPair){
        repo.insertCurrencyPair(pair)
    }
}