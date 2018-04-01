package com.dcs4u

import com.dcs4u.controller.ControllerUrl
import com.dcs4u.json.request.TransactionRequest
import com.dcs4u.model.Currency
import com.dcs4u.model.Owner
import com.dcs4u.model.Transaction
import com.dcs4u.repository.CurrencyRepository
import com.dcs4u.repository.TransactionRepository
import org.junit.*
import org.junit.Assert.assertNotNull
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.web.client.RestTemplate
import java.time.LocalDate

@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
//@Ignore
class BlockchainTransactionTest {

    val transactionApi = "http://localhost:8080${ControllerUrl.TRANSACTION_API}"
    val restTemplate: RestTemplate = RestTemplate()

    @Autowired
    lateinit var currencyRepository: CurrencyRepository

    var testCurrency: Currency? = null

    @Before
    fun createCurrency() {
        val owner = Owner("Joe", "White", "joe.white@gmail.com", LocalDate.now())
        val currency = Currency("JoeCoin", "J$", owner)
        testCurrency = currencyRepository.save(currency)
    }

    /**
     * Create a transaction, and get it by it to check that the content is like expected
     */
    @Test
    fun instantiateTransactionAndFind() {
        val request = TransactionRequest(testCurrency?.id ?: "1", 12f, "This is a transaction test")
        val instanceId: String? = restTemplate.postForObject(transactionApi, request, String::class.java)
        assertNotNull(instanceId)

        Thread.sleep(2000) //Wait for the BigChainDb to make available the transaction

        instanceId?.let {
            val transaction: TransactionRequest = restTemplate.getForObject("$transactionApi?id=$instanceId", TransactionRequest::class.java) ?: error("The server response is null")
            val (currencyId, quantity, additionalInformation) = transaction

            Assert.assertEquals(testCurrency?.id, currencyId)
            Assert.assertEquals(request.quantity, quantity)
            Assert.assertEquals(request.additionalInformation, additionalInformation)
        }
    }

    /**
     * Find all transaction for a given currency.
     * It's assumed that the currency already has multiple transaction in the test server.
     */
    @Test
    fun findTransactionsByCurrency() {
        val currencyId = testCurrency?.id
        val transaction: TransactionRequest = restTemplate.getForObject("$transactionApi/currency/$currencyId", TransactionRequest::class.java) ?: error("The server response is null")
        //val (currencyId, quantity, additionalInformation) = transaction
    }



    @After
    fun cleanCurrencyRepository() {
        currencyRepository.delete(testCurrency)
    }
}