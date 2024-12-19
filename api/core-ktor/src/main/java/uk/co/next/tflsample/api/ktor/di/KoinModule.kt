package uk.co.next.tflsample.api.ktor.di

import io.ktor.client.HttpClient
import org.koin.dsl.module
import uk.co.next.tflsample.api.json.di.JsonKoinModule
import uk.co.next.tflsample.api.ktor.setupKtorClient

val KtorCoreKoinModule = module {
    includes(JsonKoinModule)

    single<HttpClient> { setupKtorClient(get()) }
}