package com.meshpay

/**
 * Mesh Pay API client with full API and separation of concerns.
 */
class MeshPay(
    apiKey: String,
    baseUrl: String = "http://localhost:3000"
) {
    private val api = ApiClient(apiKey, baseUrl.trimEnd('/'))

    val health get() = HealthResource(api)
    val accounts get() = AccountsResource(api)
    val wallets get() = WalletsResource(api)
    val charges get() = ChargesResource(api)
    val escrows get() = EscrowsResource(api)
    val payouts get() = PayoutsResource(api)
    val apiKeys get() = ApiKeysResource(api)
    val webhookEndpoints get() = WebhookEndpointsResource(api)
    val onRamp get() = OnRampResource(api)
    val offRamp get() = OffRampResource(api)
    val webhooks get() = Webhooks
}
