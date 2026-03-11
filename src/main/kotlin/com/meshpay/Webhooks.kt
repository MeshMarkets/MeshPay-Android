package com.meshpay

import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

object Webhooks {
    fun verifySignature(payload: String, signature: String, secret: String): Boolean {
        val mac = Mac.getInstance("HmacSHA256")
        mac.init(SecretKeySpec(secret.toByteArray(Charsets.UTF_8), "HmacSHA256"))
        val expected = mac.doFinal(payload.toByteArray(Charsets.UTF_8))
            .joinToString("") { "%02x".format(it) }
        if (expected.length != signature.length) return false
        return expected.zip(signature).all { (a, b) -> a == b }
    }
}
