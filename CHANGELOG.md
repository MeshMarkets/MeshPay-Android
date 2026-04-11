# Changelog

## 3.1.0

- **Added** `ChargesResource.createPooledCharge` and `EscrowsResource.createContribution`, `setPayee`, `cancelPooledEscrow` for pooled escrow flows.

## 3.0.0

- **Removed** fiat-account methods from `WalletsResource`. Use `onRamp` / `offRamp` `createSession` instead.

## 2.0.0

Breaking — OpenAPI v1 alignment. Removed payout and API key resources. `MeshPay` supports optional `useXApiKeyHeader`. See MeshPay-JS CHANGELOG for route and method changes.
