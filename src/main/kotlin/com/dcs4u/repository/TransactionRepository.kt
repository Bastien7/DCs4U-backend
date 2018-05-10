package com.dcs4u.repository

import com.dcs4u.model.Transaction
import org.springframework.data.mongodb.repository.MongoRepository

interface TransactionRepository : MongoRepository<Transaction, String> {
    fun findByCurrencyId(currencyId: String): List<Transaction>
}