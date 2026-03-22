package com.meshpay

import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.buildJsonObject

class WalletsResource(private val api: ApiClient) {
    fun list(): JsonObject = api.getJson("/wallets")

    fun getDetail(membershipId: String, network: String? = null): JsonObject {
        val q = network?.let { mapOf("network" to it) }
        return api.getJson("/wallets/$membershipId", q)
    }

    fun listFiatAccounts(membershipId: String): JsonObject =
        api.getJson("/wallets/fiat-accounts", mapOf("membership_id" to membershipId))

    fun linkFiatAccount(body: JsonObject, idempotencyKey: String): JsonObject =
        api.postJson("/wallets/fiat-accounts", body, idempotencyKey)

    fun unlinkFiatAccount(
        membershipId: String,
        fiatAccountId: String,
        idempotencyKey: String
    ) {
        api.delete(
            "/wallets/fiat-accounts",
            mapOf("membership_id" to membershipId, "fiat_account_id" to fiatAccountId),
            idempotencyKey
        )
    }
}
