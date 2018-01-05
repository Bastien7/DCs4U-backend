package com.dcs4u.service

import com.dcs4u.json.request.TransactionRequest
import com.dcs4u.model.Transaction
import com.dcs4u.repository.TransactionRepository
import com.dcs4u.utils.ErrorMessages.ID_SHOULD_NOT_BE_NULL
import org.springframework.stereotype.Component

/**
 * Created by bastien on 03/01/2018.
 */
@Component
class TransactionService(val transactionRepository: TransactionRepository, val currencyService: CurrencyService) {

    fun get(id: String): Transaction? = transactionRepository.findById(id).orElse(null)

    fun instantiateTransaction(request: TransactionRequest): String {
        //Destructuring the request
        val (currencyId, quantity, additionalInformation) = request

        //Get the related currency from the database
        val currency = currencyService.get(currencyId) ?: throw Exception("No currency is corresponding to the id: $currencyId")

        //Create and save the transaction into the database
        val transaction = Transaction(currency, quantity, additionalInformation)
        val savedTransaction = transactionRepository.save(transaction)

        return savedTransaction.id ?: throw Exception(ID_SHOULD_NOT_BE_NULL)
    }
}