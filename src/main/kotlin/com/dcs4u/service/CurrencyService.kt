package com.dcs4u.service

import com.dcs4u.json.request.CurrencyCreationRequest
import com.dcs4u.model.Currency
import com.dcs4u.repository.CurrencyRepository
import org.springframework.stereotype.Component

/**
 * Created by bastien on 26/11/2017.
 */
@Component
class CurrencyService(val repository: CurrencyRepository) {

    fun get(id: String): Currency? = repository.findById(id).orElse(null)

    fun createCurrency(request: CurrencyCreationRequest): String {
        val currency = Currency(name = request.name, symbol = request.symbol, owner = request.owner)
        val savedCurrency = repository.save(currency)

        return savedCurrency.id ?: throw Exception("The id should not be null after persistence in the database")
    }
}