package ooh.app.currencyexchanger.data.repository

import ooh.app.currencyexchanger.data.api.ApiService
import ooh.app.currencyexchanger.data.database.dao.CurrencyPairDao
import ooh.app.currencyexchanger.data.database.entity.CurrencyPairEntity
import ooh.app.currencyexchanger.domain.model.CurrencyInfo
import ooh.app.currencyexchanger.domain.model.CurrencyPair
import ooh.app.currencyexchanger.domain.repository.CurrencyRepository
import javax.inject.Inject

class CurrencyRepositoryImpl @Inject constructor(
    private val api: ApiService,
    private val currencyPairDao: CurrencyPairDao
) : CurrencyRepository {

    override suspend fun fetchCurrencyInfoByCode(code: String): CurrencyInfo {
        return api.getCurrencyInfoByCode(code)
    }

    override suspend fun insertCurrencyPair(pair: CurrencyPair) {
        val entity = CurrencyPairEntity(
            baseCurrency = pair.baseCurrency,
            targetCurrency = pair.targetCurrency
        )

        currencyPairDao.insertCurrencyPair(entity)
    }

    override suspend fun getCurrencyPairs(): List<CurrencyPair> {
        val pair = currencyPairDao.getCurrencyPairs().map {
            CurrencyPair(
                baseCurrency = it.baseCurrency,
                targetCurrency = it.targetCurrency
            )
        }

        return pair
    }
}