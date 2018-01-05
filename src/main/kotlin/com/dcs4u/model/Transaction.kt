package com.dcs4u.model

import org.springframework.data.annotation.Id
import java.time.LocalDateTime

/**
 * Created by bastien on 05/01/2018.
 */
data class Transaction(val currency: Currency, val quantity: Float, val additionalInformation: String?) {

    @Id
    val id: String? = null //This field is out of the constructor, so that the only way to assign it is reflection used by Spring-Data
    val date: LocalDateTime = LocalDateTime.now()
}