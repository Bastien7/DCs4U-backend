package com.dcs4u.repository

import com.dcs4u.model.Currency
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

/**
 * Created by bastien on 26/11/2017.
 */
@Repository
interface CurrencyRepository : MongoRepository<Currency, String>