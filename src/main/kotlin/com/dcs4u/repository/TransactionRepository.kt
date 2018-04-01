package com.dcs4u.repository

import com.dcs4u.model.Transaction
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository


@Repository
interface TransactionRepository : MongoRepository<Transaction, String> {
    fun findByCurrency_id(id: String): List<Transaction>
}