package ooh.app.currencyexchanger.data.api

import ooh.app.currencyexchanger.domain.model.CurrencyInfo
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {

    @GET("{code}")
    suspend fun getCurrencyInfoByCode(@Path("code") code: String): CurrencyInfo
}