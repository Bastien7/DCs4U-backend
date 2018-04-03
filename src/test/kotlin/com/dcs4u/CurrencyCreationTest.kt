package com.dcs4u

import com.dcs4u.controller.ControllerUrl.CURRENCY_API
import com.dcs4u.json.request.CurrencyCreationRequest
import com.dcs4u.model.Currency
import com.dcs4u.model.Owner
import com.dcs4u.repository.CurrencyRepository
import org.junit.Assert.assertEquals
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

        val instanceId: String? = restTemplate.postForObject(currencyApi, request, String::class.java)

        instanceId?.let {
            try {
                val currency: Currency? = restTemplate.getForObject("$currencyApi?id=$instanceId", Currency::class.java)

                assertEquals(instanceId, currency?.id)
                assertEquals(request.name, currency?.name)

                val expectedOwner = request.owner
                assertEquals(expectedOwner.firstName, currency?.owner?.firstName)
                assertEquals(expectedOwner.lastName, currency?.owner?.lastName)
                assertEquals(expectedOwner.email, currency?.owner?.email)
                assertEquals(expectedOwner.birthday, currency?.owner?.birthday)
            } finally {
                currencyRepository.deleteById(instanceId)
            }
        }
    }
}