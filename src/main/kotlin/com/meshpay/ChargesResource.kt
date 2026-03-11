package com.meshpay

import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

class ChargesResource(private val api: ApiClient) {
    fun list(limit: Int? = null, cursor: String? = null, status: String? = null): JsonObject {
        val params = buildMap {
            limit?.let { put("limit", it.toString()) }
            cursor?.let { put("cursor", it) }
            status?.let { put("status", it) }
        }
        return api.getJson("/charges", if (params.isEmpty()) null else params)
    }

    fun get(chargeId: String): JsonObject = api.getJson("/charges/$chargeId")

    fun create(
        buyerId: String,
        sellerAccountId: String,
        amount: Double,
        currency: String? = "USDC",
        idempotencyKey: String? = null
    ): JsonObject = api.postJson(
        "/charges",
        buildJsonObject {
            put("buyer_id", buyerId)
            put("seller_account_id", sellerAccountId)
            put("amount", amount)
            put("currency", currency ?: "USDC")
        },
        idempotencyKey
    )

    fun fund(
        chargeId: String,
        entitySecretCiphertext: String,
        idempotencyKey: String? = null
    ): JsonObject = api.postJson(
        "/charges/$chargeId/fund",
        buildJsonObject { put("entity_secret_ciphertext", entitySecretCiphertext) },
        idempotencyKey
    )

    fun refund(
        chargeId: String,
        amount: Double? = null,
        idempotencyKey: String? = null
    ): JsonObject = api.postJson(
        "/charges/$chargeId/refund",
        if (amount != null) buildJsonObject { put("amount", amount) } else "{}",
        idempotencyKey
    )
}
