package ooh.app.currencyexchanger.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import ooh.app.currencyexchanger.data.api.ApiService
import ooh.app.currencyexchanger.data.repository.CurrencyRepositoryImpl
import ooh.app.currencyexchanger.domain.repository.CurrencyRepository
import ooh.app.currencyexchanger.domain.usecase.GetCurrencyRatesUseCase
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    private const val BASE_URL = "https://open.er-api.com/v6/latest/"

    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(loggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideCurrencyRepository(implementation: CurrencyRepositoryImpl): CurrencyRepository {
        return implementation
    }

    @Provides
    fun provideGetCurrencyRatesUseCase(repo: CurrencyRepository): GetCurrencyRatesUseCase {
        return GetCurrencyRatesUseCase(repo)
    }
}