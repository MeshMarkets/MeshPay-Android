package com.meshpay

import kotlinx.serialization.json.JsonObject

class EscrowsResource(private val api: ApiClient) {
    fun list(limit: Int? = null, cursor: String? = null, status: String? = null): JsonObject {
        val params = buildMap {
            limit?.let { put("limit", it.toString()) }
            cursor?.let { put("cursor", it) }
            status?.let { put("status", it) }
        }
        return api.getJson("/escrows", if (params.isEmpty()) null else params)
    }

    fun get(escrowId: String): JsonObject = api.getJson("/escrows/$escrowId")

    fun release(escrowId: String, idempotencyKey: String? = null): JsonObject =
        api.postJson("/escrows/$escrowId/release", "{}", idempotencyKey)
}
