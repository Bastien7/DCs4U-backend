package com.dcs4u.json.request

import java.time.LocalDateTime


data class TransactionRequest(
    val currencyId: String,
    val quantity: Float,
    val additionalInformation: String?,
    val datetime: LocalDateTime? = LocalDateTime.now()
)