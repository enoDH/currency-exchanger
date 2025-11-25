package ooh.app.currencyexchanger.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import ooh.app.currencyexchanger.data.database.dao.CurrencyPairDao
import ooh.app.currencyexchanger.data.database.entity.CurrencyPairEntity


@Database(
    entities = [CurrencyPairEntity::class],
    version = 1,
    exportSchema = false
)
abstract class ExchangerDatabase : RoomDatabase() {
    abstract fun currencyPairDao(): CurrencyPairDao
}