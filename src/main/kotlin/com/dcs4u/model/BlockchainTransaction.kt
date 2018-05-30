package com.dcs4u.model

import com.dcs4u.json.request.TransactionRequest
import java.time.LocalDateTime

data class BlockchainTransaction(
    val id: String?,
    val currency: Currency,
    val quantity: Float,
    val date: LocalDateTime = LocalDateTime.now(),
    val additionalInformation: String?
) {
    constructor(transactionId: String, currency: Currency, request: TransactionRequest) :
        this(transactionId, currency, request.quantity, request.datetime ?: LocalDateTime.now(), request.additionalInformation)
}