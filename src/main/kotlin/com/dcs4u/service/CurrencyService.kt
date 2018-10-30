package com.dcs4u.service

import com.dcs4u.json.request.CurrencyCreationRequest
import com.dcs4u.model.Currency
import com.dcs4u.repository.CurrencyRepository
import com.dcs4u.utils.ErrorMessages.PERSISTENCE_FAILED
import com.dcs4u.utils.Loggable
import org.springframework.stereotype.Component

/**
 * Created by bastien on 26/11/2017.
 */
@Component
class CurrencyService(val repository: CurrencyRepository) : Loggable {

    fun getAllCurrencies(): List<Currency> = repository.findAll()

    fun get(id: String): Currency? = repository.findById(id).orElse(null)

    fun createCurrency(request: CurrencyCreationRequest): Currency {
        //Destructuring the request
        val (name, owner) = request
        val symbol = name + "Coin"

        //Create and save the currency into the database
        val currency = Currency(name, symbol, owner)
        val savedCurrency = repository.save(currency) ?: throw Exception(PERSISTENCE_FAILED)

        log.info("The currency $name has been created")
        return savedCurrency
    }
}