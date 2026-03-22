package com.meshpay

import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

class EscrowsResource(private val api: ApiClient) {
    fun list(limit: Int? = null, status: String? = null): JsonObject {
        val params = buildMap {
            limit?.let { put("limit", it.toString()) }
            status?.let { put("status", it) }
        }
        return api.getJson("/escrows", if (params.isEmpty()) null else params)
    }

    fun get(escrowId: String): JsonObject = api.getJson("/escrows/$escrowId")

    fun release(escrowId: String, idempotencyKey: String): JsonObject =
        api.postJson("/escrows/$escrowId/release", buildJsonObject { }, idempotencyKey)

    fun openDispute(escrowId: String, txHash: String): JsonObject =
        api.postJson(
            "/escrows/$escrowId/open-dispute",
            buildJsonObject { put("tx_hash", txHash) }
        )

    fun resolveDispute(
        escrowId: String,
        releaseToSeller: Boolean,
        idempotencyKey: String
    ): JsonObject = api.postJson(
        "/escrows/$escrowId/resolve-dispute",
        buildJsonObject { put("release_to_seller", releaseToSeller) },
        idempotencyKey
    )
}
