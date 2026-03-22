package com.meshpay

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException

internal class ApiClient(
    private val apiKey: String,
    private val baseUrl: String,
    private val useXApiKeyHeader: Boolean = false
) {
    private val client = OkHttpClient()
    private val json = Json { ignoreUnknownKeys = true }

    private fun authHeader(): Pair<String, String> =
        if (useXApiKeyHeader) "X-Api-Key" to apiKey else "Authorization" to "Bearer $apiKey"

    fun get(path: String, queryParams: Map<String, String>? = null): String {
        val url = buildString {
            append(baseUrl.trimEnd('/'))
            append(path)
            if (!queryParams.isNullOrEmpty()) {
                append('?')
                append(queryParams.entries.joinToString("&") { "${it.key}=${it.value}" })
            }
        }
        val (h, v) = authHeader()
        val req = Request.Builder()
            .url(url)
            .addHeader(h, v)
            .addHeader("Accept", "application/json")
            .get()
            .build()
        return execute(req)
    }

    /** GET without API key (e.g. /health). */
    fun getPublic(path: String, queryParams: Map<String, String>? = null): String {
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
            .addHeader("Accept", "application/json")
            .get()
            .build()
        return execute(req)
    }

    fun post(path: String, body: String, idempotencyKey: String? = null): String {
        val (h, v) = authHeader()
        val b = Request.Builder()
            .url(baseUrl.trimEnd('/') + path)
            .addHeader(h, v)
            .addHeader("Content-Type", "application/json")
            .apply { idempotencyKey?.let { addHeader("Idempotency-Key", it) } }
            .post(body.toRequestBody("application/json".toMediaType()))
            .build()
        return execute(b)
    }

    fun patch(path: String, body: String): String {
        val (h, v) = authHeader()
        val req = Request.Builder()
            .url(baseUrl.trimEnd('/') + path)
            .addHeader(h, v)
            .addHeader("Content-Type", "application/json")
            .patch(body.toRequestBody("application/json".toMediaType()))
            .build()
        return execute(req)
    }

    fun delete(path: String, queryParams: Map<String, String>? = null, idempotencyKey: String? = null) {
        val url = buildString {
            append(baseUrl.trimEnd('/'))
            append(path)
            if (!queryParams.isNullOrEmpty()) {
                append('?')
                append(queryParams.entries.joinToString("&") { "${it.key}=${it.value}" })
            }
        }
        val (h, v) = authHeader()
        val req = Request.Builder()
            .url(url)
            .addHeader(h, v)
            .addHeader("Accept", "application/json")
            .apply { idempotencyKey?.let { addHeader("Idempotency-Key", it) } }
            .delete()
            .build()
        executeAllowEmpty(req)
    }

    private fun execute(req: Request): String {
        val res = client.newCall(req).execute()
        val body = res.body?.string() ?: ""
        if (!res.isSuccessful) throw MeshPayApiException(res.code, body)
        if (res.code == 204 || body.isEmpty()) return "{}"
        return body
    }

    private fun executeAllowEmpty(req: Request) {
        val res = client.newCall(req).execute()
        res.body?.close()
        if (!res.isSuccessful) {
            val msg = res.body?.string() ?: ""
            throw MeshPayApiException(res.code, msg)
        }
    }

    inline fun <reified T> getJson(path: String, queryParams: Map<String, String>? = null): T =
        json.decodeFromString(get(path, queryParams))

    inline fun <reified T> getPublicJson(path: String, queryParams: Map<String, String>? = null): T =
        json.decodeFromString(getPublic(path, queryParams))

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
