package com.dcs4u.controller

import com.dcs4u.json.request.CurrencyCreationRequest
import com.dcs4u.service.CurrencyService
import org.springframework.web.bind.annotation.*
import javax.websocket.server.PathParam

/**
 * Created by bastien on 26/11/2017.
 */
@RestController
@RequestMapping("/api/currency")
class CurrencyController(val currencyService: CurrencyService) {

    @GetMapping
    fun getCurrency(@PathParam("id") id: String) = currencyService.get(id)

    @PostMapping
    fun createCurrency(@RequestBody request: CurrencyCreationRequest) = currencyService.createCurrency(request)
}