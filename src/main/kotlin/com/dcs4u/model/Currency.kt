package com.dcs4u.model

import org.springframework.data.annotation.Id
import java.time.LocalDateTime

/**
 * Created by bastien on 26/11/2017.
 */
data class Currency(
    @Id val id: String? = null,
    val name: String,
    val symbol: String,
    val owner: Owner,
    val creationDate: LocalDateTime = LocalDateTime.now()
)