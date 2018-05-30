package com.dcs4u

import com.dcs4u.controller.ControllerUrl.CURRENCY_API
import com.dcs4u.json.request.CurrencyCreationRequest
import com.dcs4u.model.Currency
import com.dcs4u.model.Owner
import com.dcs4u.repository.CurrencyRepository
import org.junit.Assert.assertEquals
import org.junit.Assert.fail
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.web.client.RestTemplate
import java.time.LocalDate

@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class CurrencyCreationTest {

    val currencyApi = "http://localhost:8080$CURRENCY_API"
    val restTemplate: RestTemplate = RestTemplate()

    @Autowired
    lateinit var currencyRepository: CurrencyRepository

    /**
     * Create a testCurrency, get it by it to check that the content is like expected,
     * and delete it from the repository so that the repository is clean after the test
     */
    @Test
    fun createCurrency() {
        val owner = Owner("Joe", "White", "joe.white@gmail.com", LocalDate.now())
        val request = CurrencyCreationRequest("JoeCoin", owner)
        val requestOwner = Owner("Joe", "White", "joe.white@gmail.com", LocalDate.now())
        val creationResponse: Currency? = restTemplate.postForObject(currencyApi, request, Currency::class.java)
        val creationId: String = creationResponse?.id ?: return fail("No response or response id")

        creationId.let {
            try {
                val currency: Currency? = restTemplate.getForObject("$currencyApi/$creationId", Currency::class.java)
                val (name, symbol, owner) = currency ?: throw Exception("No currency returned")

                assertEquals(creationId, currency.id)
                assertEquals(request.name, name)
                val (firstName, lastName, email, birthday) = owner
                assertEquals(requestOwner.firstName, firstName)
                assertEquals(requestOwner.lastName, lastName)
                assertEquals(requestOwner.email, email)
                assertEquals(requestOwner.birthday, birthday)
            } finally {
                currencyRepository.deleteById(creationId)
            }
        }
    }
}