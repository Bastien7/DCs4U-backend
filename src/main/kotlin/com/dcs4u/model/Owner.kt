package com.dcs4u.model

import java.time.LocalDate

/**
 * Created by bastien on 26/11/2017.
 */
data class Owner(
    val firstName: String,
    val lastName: String,
    val email: String,
    val birthday: LocalDate
)