package com.meshpay

import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.buildJsonObject

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

    fun create(body: JsonObject, idempotencyKey: String): JsonObject =
        api.postJson("/charges", body, idempotencyKey)

    fun createPooledCharge(body: JsonObject, idempotencyKey: String): JsonObject =
        api.postJson("/pooled-charges", body, idempotencyKey)

    fun fund(chargeId: String, body: JsonObject, idempotencyKey: String): JsonObject =
        api.postJson("/charges/$chargeId/fund", body, idempotencyKey)

    fun cancel(chargeId: String, idempotencyKey: String): JsonObject =
        api.postJson("/charges/$chargeId/cancel", buildJsonObject { }, idempotencyKey)

    fun refund(chargeId: String, body: JsonObject, idempotencyKey: String): JsonObject =
        api.postJson("/charges/$chargeId/refund", body, idempotencyKey)
}
