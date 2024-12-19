package uk.co.next.tflsample.api.json

import kotlinx.serialization.json.Json

internal fun setupJson() = Json {
    isLenient = false
    ignoreUnknownKeys = true
}