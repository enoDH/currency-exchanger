package ooh.app.currencyexchanger.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ooh.app.currencyexchanger.data.database.entity.CurrencyPairEntity

@Dao
interface CurrencyPairDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCurrencyPair(pair: CurrencyPairEntity)

    @Query("SELECT * FROM currency_pair")
    suspend fun getCurrencyPairs(): List<CurrencyPairEntity>
}