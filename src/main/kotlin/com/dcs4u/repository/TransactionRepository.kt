package com.dcs4u.repository

import com.dcs4u.model.Transaction
import org.springframework.data.mongodb.repository.MongoRepository

interface TransactionRepository : MongoRepository<Transaction, String> {
    //fun findByCurrency_Id(currencyId: String): List<Transaction> //TODO not working, need to know how spring data can manage this
}