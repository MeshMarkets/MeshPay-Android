package com.meshpay

import kotlinx.serialization.json.JsonObject

class OnRampResource(private val api: ApiClient) {
    fun createSession(body: JsonObject): JsonObject =
        api.postJson("/on-ramp/sessions", body)
}
