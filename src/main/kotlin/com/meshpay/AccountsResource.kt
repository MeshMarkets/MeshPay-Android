package com.meshpay

import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

class AccountsResource(private val api: ApiClient) {
    fun list(): JsonObject = api.getJson("/accounts")

    fun create(email: String): JsonObject =
        api.postJson("/accounts", buildJsonObject { put("email", email) })

    fun deleteMembership(membershipId: String) {
        api.delete("/accounts/$membershipId")
    }
}
