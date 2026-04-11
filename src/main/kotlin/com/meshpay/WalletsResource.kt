package com.meshpay

import kotlinx.serialization.json.JsonObject

class WalletsResource(private val api: ApiClient) {
    fun list(): JsonObject = api.getJson("/wallets")

    fun getDetail(membershipId: String, network: String? = null): JsonObject {
        val q = network?.let { mapOf("network" to it) }
        return api.getJson("/wallets/$membershipId", q)
    }
}
