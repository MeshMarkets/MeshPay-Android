package com.meshpay

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException

internal class ApiClient(
    private val apiKey: String,
    private val baseUrl: String
) {
    private val client = OkHttpClient()
    private val json = Json { ignoreUnknownKeys = true }

    fun get(path: String, queryParams: Map<String, String>? = null): String {
        val url = buildString {
            append(baseUrl.trimEnd('/'))
            append(path)
            if (!queryParams.isNullOrEmpty()) {
                append('?')
                append(queryParams.entries.joinToString("&") { "${it.key}=${it.value}" })
            }
        }
        val req = Request.Builder()
            .url(url)
            .addHeader("Authorization", "Bearer $apiKey")
            .addHeader("Content-Type", "application/json")
            .get()
            .build()
        val res = client.newCall(req).execute()
        val body = res.body?.string() ?: ""
        if (!res.isSuccessful) throw MeshPayApiException(res.code, body)
        return body
    }

    fun post(path: String, body: String, idempotencyKey: String? = null): String {
        val req = Request.Builder()
            .url(baseUrl.trimEnd('/') + path)
            .addHeader("Authorization", "Bearer $apiKey")
            .addHeader("Content-Type", "application/json")
            .apply { idempotencyKey?.let { addHeader("Idempotency-Key", it) } }
            .post(body.toRequestBody("application/json".toMediaType()))
            .build()
        val res = client.newCall(req).execute()
        val resBody = res.body?.string() ?: ""
        if (!res.isSuccessful) throw MeshPayApiException(res.code, resBody)
        return resBody
    }

    fun patch(path: String, body: String): String {
        val req = Request.Builder()
            .url(baseUrl.trimEnd('/') + path)
            .addHeader("Authorization", "Bearer $apiKey")
            .addHeader("Content-Type", "application/json")
            .patch(body.toRequestBody("application/json".toMediaType()))
            .build()
        val res = client.newCall(req).execute()
        val resBody = res.body?.string() ?: ""
        if (!res.isSuccessful) throw MeshPayApiException(res.code, resBody)
        return resBody
    }

    fun delete(path: String): String {
        val req = Request.Builder()
            .url(baseUrl.trimEnd('/') + path)
            .addHeader("Authorization", "Bearer $apiKey")
            .delete()
            .build()
        val res = client.newCall(req).execute()
        val resBody = res.body?.string() ?: ""
        if (!res.isSuccessful) throw MeshPayApiException(res.code, resBody)
        return resBody
    }

    inline fun <reified T> getJson(path: String, queryParams: Map<String, String>? = null): T =
        json.decodeFromString(get(path, queryParams))

    inline fun <reified T> postJson(path: String, body: Any, idempotencyKey: String? = null): T {
        val bodyStr = when (body) {
            is String -> body
            is JsonElement -> json.encodeToString(JsonElement.serializer(), body)
            else -> json.encodeToString(body)
        }
        return json.decodeFromString(post(path, bodyStr, idempotencyKey))
    }

    inline fun <reified T> patchJson(path: String, body: Any): T {
        val bodyStr = when (body) {
            is String -> body
            is JsonElement -> json.encodeToString(JsonElement.serializer(), body)
            else -> json.encodeToString(body)
        }
        return json.decodeFromString(patch(path, bodyStr))
    }
}

class MeshPayApiException(val code: Int, message: String) : IOException("Mesh Pay API error $code: $message")
