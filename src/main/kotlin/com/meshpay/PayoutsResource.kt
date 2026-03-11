package com.meshpay

import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

class PayoutsResource(private val api: ApiClient) {
    fun list(limit: Int? = null, cursor: String? = null, status: String? = null): JsonObject {
        val params = buildMap {
            limit?.let { put("limit", it.toString()) }
            cursor?.let { put("cursor", it) }
            status?.let { put("status", it) }
        }
        return api.getJson("/payouts", if (params.isEmpty()) null else params)
    }

    fun get(payoutId: String): JsonObject = api.getJson("/payouts/$payoutId")

    fun create(
        accountId: String,
        amount: Double,
        idempotencyKey: String? = null
    ): JsonObject = api.postJson(
        "/payouts",
        buildJsonObject {
            put("account_id", accountId)
            put("amount", amount)
        },
        idempotencyKey
    )
}
