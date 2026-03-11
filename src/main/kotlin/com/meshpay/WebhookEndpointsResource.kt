package com.meshpay

import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.put

class WebhookEndpointsResource(private val api: ApiClient) {
    fun list(): List<JsonObject> = api.get("/webhook-endpoints").let { body ->
        kotlinx.serialization.json.Json.parseToJsonElement(body).jsonArray.map { it as JsonObject }
    }

    fun get(id: String): JsonObject = api.getJson("/webhook-endpoints/$id")

    fun create(url: String, events: List<String>, secret: String? = null): JsonObject {
        val body = buildJsonObject {
            put("url", url)
            put("events", events.map { JsonPrimitive(it) })
            secret?.let { put("secret", it) }
        }
        return api.postJson("/webhook-endpoints", body)
    }

    fun update(id: String, active: Boolean? = null, events: List<String>? = null): JsonObject {
        val body = buildJsonObject {
            active?.let { put("active", it) }
            events?.let { put("events", it.map { JsonPrimitive(it) }) }
        }
        return api.patchJson("/webhook-endpoints/$id", body)
    }

    fun delete(id: String) {
        api.delete("/webhook-endpoints/$id")
    }
}
