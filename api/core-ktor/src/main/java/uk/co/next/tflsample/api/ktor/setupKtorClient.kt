package uk.co.next.tflsample.api.ktor

import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.cache.HttpCache
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

internal fun setupKtorClient(json: Json) = HttpClient(OkHttp) {
    expectSuccess = true
    install(ContentNegotiation) {
        json(json)
    }
    install(HttpCache)
}