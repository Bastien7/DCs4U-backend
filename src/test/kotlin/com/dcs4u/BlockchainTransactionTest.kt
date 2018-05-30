package com.dcs4u

import com.dcs4u.controller.ControllerUrl
import com.dcs4u.json.request.TransactionRequest
import com.dcs4u.model.BlockchainTransaction
import com.dcs4u.model.Currency
import com.dcs4u.model.Owner
import com.dcs4u.repository.CurrencyRepository
import org.junit.After
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
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
        val instanceId: String? = restTemplate.postForObject(transactionApi, request, BlockchainTransaction::class.java)?.id
        assertNotNull(instanceId)

        Thread.sleep(2000) //Wait for the BigChainDb to make available the transaction

        instanceId?.let {
            val transaction: BlockchainTransaction = restTemplate.getForObject("$transactionApi/$instanceId", BlockchainTransaction::class.java) ?: error("The server response is null")
            val (_, currency, quantity, date, additionalInformation) = transaction

            Assert.assertEquals(testCurrency?.id, currency.id)
            Assert.assertEquals(request.quantity, quantity)
            Assert.assertEquals(request.datetime, date)
            Assert.assertEquals(request.additionalInformation, additionalInformation)
        }
    }

    /**
     * Find all transaction for a given currency.
     * It's assumed that the currency already has multiple transaction in the test server.
     */
    @Test
    fun findTransactionsByCurrency() {
        val currencyId: String = testCurrency?.id ?: error("That's not possible!")

        //Create a transaction before to search it by currency
        val request = TransactionRequest(currencyId, 12f, "This is a transaction test")
        val instanceId: String? = restTemplate.postForObject(transactionApi, request, String::class.java)
        assertNotNull(instanceId)

        Thread.sleep(2000) //Wait for the BigChainDb to make available the transaction

        val transactions: List<BlockchainTransaction> = restTemplate.getForObject("$transactionApi/currency/$currencyId", List::class.java)
            as? List<BlockchainTransaction> ?: error("Error in server response content")

        transactions.first()?.let {
            assertEquals(it.currency.id, currencyId)
            assertEquals(it.quantity, 12f)
            assertEquals(it.additionalInformation, "This is a transaction test")
        } ?: error("Problem in the transaction content")
    }


    @After
    fun cleanCurrencyRepository() {
        currencyRepository.delete(testCurrency)
    }
}