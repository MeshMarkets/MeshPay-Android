package com.meshpay

/**
 * Mesh Pay API client (OpenAPI v1). Default base URL uses YOUR_PROJECT_REF placeholder.
 */
class MeshPay(
    apiKey: String,
    baseUrl: String = "https://YOUR_PROJECT_REF.supabase.co/functions/v1/api",
    useXApiKeyHeader: Boolean = false
) {
    private val api = ApiClient(apiKey, baseUrl.trimEnd('/'), useXApiKeyHeader)

    val health get() = HealthResource(api)
    val accounts get() = AccountsResource(api)
    val wallets get() = WalletsResource(api)
    val charges get() = ChargesResource(api)
    val escrows get() = EscrowsResource(api)
    val webhookEndpoints get() = WebhookEndpointsResource(api)
    val onRamp get() = OnRampResource(api)
    val offRamp get() = OffRampResource(api)
    val webhooks get() = Webhooks
}
