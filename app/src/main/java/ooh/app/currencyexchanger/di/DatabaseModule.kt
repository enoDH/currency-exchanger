package ooh.app.currencyexchanger.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton
import ooh.app.currencyexchanger.data.database.ExchangerDatabase
import ooh.app.currencyexchanger.data.database.dao.CurrencyPairDao

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): ExchangerDatabase{
        return Room.databaseBuilder(
            context.applicationContext,
            ExchangerDatabase::class.java,
            "exchanger.db"
        ).build()
    }

    @Provides
    fun provideCurrencyPairDao(database: ExchangerDatabase): CurrencyPairDao {
        return database.currencyPairDao()
    }
}