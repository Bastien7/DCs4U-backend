package com.dcs4u

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class CurrencyManagerApplication

fun main(args: Array<String>) {
    runApplication<CurrencyManagerApplication>(*args)
}
