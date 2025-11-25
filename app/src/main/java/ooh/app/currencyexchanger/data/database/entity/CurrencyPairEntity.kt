package ooh.app.currencyexchanger.data.database.entity


import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "currency_pair")
data class CurrencyPairEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val baseCurrency: String = "USD",
    val targetCurrency: String = "UAH"
)