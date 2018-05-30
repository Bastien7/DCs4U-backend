package com.dcs4u.service

import com.authenteq.api.TransactionsApi
import com.authenteq.builders.BigchainDbConfigBuilder
import com.authenteq.builders.BigchainDbTransactionBuilder
import com.authenteq.constants.Operations
import com.authenteq.model.Asset
import com.dcs4u.json.request.TransactionRequest
import com.dcs4u.model.BlockchainTransaction
import com.dcs4u.utils.toLocalDateTime
import net.i2p.crypto.eddsa.EdDSAPrivateKey
import net.i2p.crypto.eddsa.EdDSAPublicKey
import net.i2p.crypto.eddsa.KeyPairGenerator
import org.springframework.beans.factory.InitializingBean
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.security.KeyPair
import java.time.LocalDateTime


@Component
class BigChainDbService(
    @Value("\${application.id}") val id: String,
    @Value("\${application.key}") val key: String,
    val currencyService: CurrencyService
) : InitializingBean {

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

    fun createTransaction(request: TransactionRequest): BlockchainTransaction {
        val keyPair = getKeyPair()
        val currency = currencyService.get(request.currencyId) ?: error("No currency found for the id ${request.currencyId}")

        val transactionId = BigchainDbTransactionBuilder.init()
            .addAssets(request, TransactionRequest::class.java)
            .operation(Operations.CREATE)
            .buildAndSign(keyPair.public as EdDSAPublicKey, keyPair.private as EdDSAPrivateKey)
            .sendTransaction().id

        return BlockchainTransaction(transactionId, currency, request)
    }

    fun getTransaction(id: String): BlockchainTransaction {
        return TransactionsApi.getTransactionById(id).toBlockchainTransaction()
    }

    fun getTransactionsByCurrency(currencyId: String): List<BlockchainTransaction> {
        return TransactionsApi.searchTransactionByKeyword(currencyId).map { it.toBlockchainTransaction() }
    }

    private fun com.authenteq.model.Transaction.toBlockchainTransaction(): BlockchainTransaction {
        return this.asset.toBlockchainTransaction(this?.asset.id)
    }

    private fun Asset?.toBlockchainTransaction(transactionId: String? = null): BlockchainTransaction {
        val json = this?.data as? Map<*, *> ?: error("Error while parsing the transaction response")

        val currencyId: String = json[TransactionRequest::currencyId.name].toString() ?: error("CurrencyId parsing error")
        val quantity: Double = json[TransactionRequest::quantity.name] as? Double ?: error("Quantity parsing error")
        val datetime: LocalDateTime = (json[TransactionRequest::datetime.name] as? Map<String, *>)?.toLocalDateTime() ?: error("Datetime parsing error")
        val additionalInformation: String? = json[TransactionRequest::additionalInformation.name]?.toString()
        val currency = currencyService.get(currencyId) ?: error("No currency found for the id $currencyId")

        return BlockchainTransaction(transactionId, currency, quantity.toFloat(), datetime, additionalInformation)
    }
}