package com.dcs4u.controller

import com.dcs4u.controller.ControllerUrl.CURRENCY_API
import com.dcs4u.json.request.CurrencyCreationRequest
import com.dcs4u.service.CurrencyService
import org.springframework.web.bind.annotation.*
import javax.websocket.server.PathParam


@RestController
@RequestMapping(CURRENCY_API)
class CurrencyController(val currencyService: CurrencyService) {

    @GetMapping
    fun getCurrency(@PathParam("id") id: String) = currencyService.get(id)

    @PostMapping
    fun createCurrency(@RequestBody request: CurrencyCreationRequest) = currencyService.createCurrency(request)
}