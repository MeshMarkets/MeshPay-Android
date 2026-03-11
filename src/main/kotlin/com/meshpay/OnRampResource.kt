package com.meshpay

import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

class OnRampResource(private val api: ApiClient) {
    fun getQuote(amountUsd: Double? = null, amountUsdc: Double? = null): JsonObject {
        val body = buildJsonObject {
            amountUsd?.let { put("amount_usd", it) }
            amountUsdc?.let { put("amount_usdc", it) }
        }
        return api.postJson("/on-ramp/quote", body)
    }

    fun executeTrade(quoteId: String, idempotencyKey: String? = null): JsonObject =
        api.postJson("/on-ramp/trade", buildJsonObject { put("quote_id", quoteId) }, idempotencyKey)
}
