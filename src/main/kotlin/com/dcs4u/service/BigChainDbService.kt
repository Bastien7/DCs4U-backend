package com.dcs4u.service

import com.authenteq.api.TransactionsApi
import com.authenteq.builders.BigchainDbConfigBuilder
import com.authenteq.builders.BigchainDbTransactionBuilder
import com.authenteq.constants.Operations
import com.authenteq.model.Asset
import com.authenteq.model.Transaction
import com.dcs4u.json.request.TransactionRequest
import net.i2p.crypto.eddsa.EdDSAPrivateKey
import net.i2p.crypto.eddsa.EdDSAPublicKey
import net.i2p.crypto.eddsa.KeyPairGenerator
import org.springframework.beans.factory.InitializingBean
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.security.KeyPair


@Component
class BigChainDbService(@Value("\${application.id}") val id: String, @Value("\${application.key}") val key: String) : InitializingBean {

    override fun afterPropertiesSet() {
        BigchainDbConfigBuilder
            .baseUrl("https://test.bigchaindb.com")
            .addToken("app_id", id)
            .addToken("app_key", key).setup()
    }

    private fun getKeyPair(): KeyPair {
        val edDsaKpg = KeyPairGenerator()
        return edDsaKpg.generateKeyPair()
    }

    fun createTransaction(request: TransactionRequest): String {
        val keyPair = getKeyPair()

        return BigchainDbTransactionBuilder.init()
            .addAssets(request, TransactionRequest::class.java)
            .operation(Operations.CREATE)
            .buildAndSign(keyPair.public as EdDSAPublicKey, keyPair.private as EdDSAPrivateKey)
            .sendTransaction().id
    }

    fun getTransaction(id: String): TransactionRequest {
        val transaction: Transaction? = TransactionsApi.getTransactionById(id)
        val json = transaction?.asset?.data as? Map<*, *> ?: error("Error while parsing the transaction response")

        val currencyId: String = json[TransactionRequest::currencyId.name]?.toString() ?: error("CurrencyId parsing error")
        val quantity: Double = json[TransactionRequest::quantity.name] as? Double ?: error("Quantity parsing error")
        val additionalInformation: String? = json[TransactionRequest::additionalInformation.name]?.toString()

        return TransactionRequest(currencyId, quantity.toFloat(), additionalInformation)
    }

    fun getTransactionsByCurrency(currencyId: String): List<Asset> = TransactionsApi.searchTransactionByKeyword(currencyId)
}