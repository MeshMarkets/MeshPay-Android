package com.meshpay

import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

class WalletsResource(private val api: ApiClient) {
    fun create(accountId: String): JsonObject =
        api.postJson("/wallets", buildJsonObject { put("account_id", accountId) })

    fun getByAccountId(accountId: String): JsonObject =
        api.getJson("/wallets", mapOf("account_id" to accountId))
}
