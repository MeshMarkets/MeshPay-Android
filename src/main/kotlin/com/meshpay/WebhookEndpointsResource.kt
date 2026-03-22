package com.meshpay

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.put

class WebhookEndpointsResource(private val api: ApiClient) {
    private val jsonParser = Json { ignoreUnknownKeys = true }

    fun list(): List<JsonObject> {
        val body = api.get("/webhook-endpoints")
        return parseListBody(body)
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

    fun listDeliveries(id: String, limit: Int? = null): List<JsonObject> {
        val q = limit?.let { mapOf("limit" to it.toString()) }
        val path = "/webhook-endpoints/$id/deliveries"
        val body = if (q == null) api.get(path) else api.get(path, q)
        return parseListBody(body)
    }

    private fun parseListBody(body: String): List<JsonObject> {
        val el = jsonParser.parseToJsonElement(body)
        return when {
            el is JsonArray -> el.map { it as JsonObject }
            el is JsonObject && el["data"] is JsonArray ->
                (el["data"] as JsonArray).map { it as JsonObject }
            else -> emptyList()
        }
    }
}
