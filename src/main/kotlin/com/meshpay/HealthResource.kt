package com.meshpay

import kotlinx.serialization.json.JsonObject

class HealthResource(private val api: ApiClient) {
    fun get(): JsonObject = api.getPublicJson("/health")
}
