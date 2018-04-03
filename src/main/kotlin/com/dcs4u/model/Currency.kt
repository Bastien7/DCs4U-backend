package com.dcs4u.model

import org.springframework.data.annotation.Id
import java.time.LocalDateTime

/**
 * Created by bastien on 26/11/2017.
 */
data class Currency(val name: String, val symbol: String, val owner: Owner) {

    @Id
    val id: String? = null //This field is out of the constructor, so that the only way to assign it is reflection used by Spring-Data
    val creationDate: LocalDateTime = LocalDateTime.now()
}