package ooh.app.currencyexchanger.domain.usecase

import jakarta.inject.Inject

class CalculateExchangeRateUseCase @Inject constructor() {
    fun exchange(amount: Double, rate: Double, direct: Boolean): Double {
        if (amount < 0) throw IllegalArgumentException("Amount cannot be negative.")
        return if(direct) amount * rate else amount / rate
    }
}