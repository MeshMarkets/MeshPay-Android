package com.meshpay

import kotlinx.serialization.json.JsonObject

class OffRampResource(private val api: ApiClient) {
    fun createSession(body: JsonObject): JsonObject =
        api.postJson("/off-ramp/sessions", body)
}
