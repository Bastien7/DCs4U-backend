package com.dcs4u.controller

import com.dcs4u.controller.ControllerUrl.TRANSACTION_API
import com.dcs4u.json.request.TransactionRequest
import com.dcs4u.service.TransactionService
import org.springframework.web.bind.annotation.*

@CrossOrigin
@RestController
@RequestMapping(TRANSACTION_API)
class TransactionController(val transactionService: TransactionService) {

    @GetMapping("/{id}")
    fun getTransaction(@PathVariable("id") id: String) = transactionService.getTransaction(id)

    @GetMapping("/currency/{currencyId}")
    fun getTransactionByCurrency(@PathVariable("currencyId") currencyId: String) = transactionService.getTransactionsByCurrency(currencyId)

    @PostMapping
    fun instantiateTransaction(@RequestBody request: TransactionRequest) = transactionService.instantiateTransaction(request)
}