package com.dcs4u

import com.dcs4u.controller.ControllerUrl.TRANSACTION_API
import com.dcs4u.json.request.TransactionRequest
import com.dcs4u.model.Currency
import com.dcs4u.model.Owner
import com.dcs4u.model.Transaction
import com.dcs4u.repository.CurrencyRepository
import com.dcs4u.repository.TransactionRepository
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.web.client.RestTemplate
import java.time.LocalDate

@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class TransactionInstantiationTest {

    val transactionApi = "http://localhost:8080$TRANSACTION_API"
    val restTemplate: RestTemplate = RestTemplate()

    @Autowired
    lateinit var currencyRepository: CurrencyRepository
    @Autowired
    lateinit var transactionRepository: TransactionRepository

    var testCurrency: Currency? = null

    @Before
    fun createCurrency() {
        val owner = Owner("Joe", "White", "joe.white@gmail.com", LocalDate.now())
        val currency = Currency("JoeCoin", owner)
        testCurrency = currencyRepository.save(currency)
    }

    /**
     * Create a transaction, get it by it to check that the content is like expected,
     * and delete it from the repository so that the repository is clean after the test
     */
    @Test
    fun instantiateTransaction() {
        val request = TransactionRequest(testCurrency?.id ?: "1", 12f, "This is a transaction test")
        val instanceId: String? = restTemplate.postForObject(transactionApi, request, String::class.java)

        instanceId?.let {
            try {
                val transaction: Transaction? = restTemplate.getForObject("$transactionApi?id=$instanceId", Transaction::class.java)
                val (currency, quantity, additionalInformation) = transaction ?: throw Exception("No transaction returned")

                assertEquals(instanceId, transaction.id)
                assertEquals(currency, testCurrency)
                assertEquals(request.quantity, quantity)
                assertEquals(request.additionalInformation, additionalInformation)
            } finally {
                transactionRepository.deleteById(instanceId)
            }
        }
    }

    @After
    fun cleanCurrencyRepository() {
        currencyRepository.delete(testCurrency)
    }
}