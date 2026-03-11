package com.meshpay

import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.put

class ApiKeysResource(private val api: ApiClient) {
    fun list(): List<JsonObject> = api.get("/api-keys").let { body ->
        kotlinx.serialization.json.Json.parseToJsonElement(body).jsonArray.map { it as JsonObject }
    }

    fun create(name: String? = null): JsonObject = api.postJson(
        "/api-keys",
        if (name != null) buildJsonObject { put("name", name) } else "{}"
    )

    fun delete(id: String) {
        api.delete("/api-keys/$id")
    }
}
