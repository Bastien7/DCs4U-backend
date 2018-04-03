package com.dcs4u.service

import com.dcs4u.json.request.CurrencyCreationRequest
import com.dcs4u.model.Currency
import com.dcs4u.repository.CurrencyRepository
import com.dcs4u.utils.ErrorMessages.CURRENCY_SHOULD_NOT_BE_NULL
import org.springframework.stereotype.Component

/**
 * Created by bastien on 26/11/2017.
 */
@Component
class CurrencyService(val repository: CurrencyRepository) {

    fun get(id: String): Currency? = repository.findById(id).orElse(null)

    fun createCurrency(request: CurrencyCreationRequest): Currency {
        //Destructuring the request
        val (name, owner) = request
        val symbol = "SfeirC"
        //Create and save the currency into the database
        val currency = Currency(name,symbol, owner)
        val savedCurrency = repository.save(currency)

        return savedCurrency ?: throw Exception(CURRENCY_SHOULD_NOT_BE_NULL)
    }
}