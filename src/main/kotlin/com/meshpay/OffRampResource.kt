package com.meshpay

import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

class OffRampResource(private val api: ApiClient) {
    fun getQuote(amountUsdc: Double? = null, amountUsd: Double? = null): JsonObject {
        val body = buildJsonObject {
            amountUsdc?.let { put("amount_usdc", it) }
            amountUsd?.let { put("amount_usd", it) }
        }
        return api.postJson("/off-ramp/quote", body)
    }

    fun executeTrade(quoteId: String, idempotencyKey: String? = null): JsonObject =
        api.postJson("/off-ramp/trade", buildJsonObject { put("quote_id", quoteId) }, idempotencyKey)
}
