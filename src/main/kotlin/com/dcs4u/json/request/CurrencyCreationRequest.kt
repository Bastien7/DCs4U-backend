package com.dcs4u.json.request

import com.dcs4u.model.Owner

/**
 * Created by bastien on 26/11/2017.
 */
class CurrencyCreationRequest(
    val name: String,
    val symbol: String,
    val owner: Owner
)