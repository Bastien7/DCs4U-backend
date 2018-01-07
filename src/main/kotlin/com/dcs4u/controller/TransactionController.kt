package com.dcs4u.controller

import com.dcs4u.controller.ControllerUrl.TRANSACTION_API
import com.dcs4u.json.request.TransactionRequest
import com.dcs4u.service.TransactionService
import org.springframework.web.bind.annotation.*
import javax.websocket.server.PathParam

@CrossOrigin
@RestController
@RequestMapping(TRANSACTION_API)
class TransactionController(val transactionService: TransactionService) {

    @GetMapping
    fun getTransaction(@PathParam("id") id: String) = transactionService.get(id)

    @PostMapping
    fun instantiateTransaction(@RequestBody request: TransactionRequest) = transactionService.instantiateTransaction(request)
}