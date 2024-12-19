package uk.co.next.tflsample.api.json.di

import kotlinx.serialization.json.Json
import org.koin.dsl.module
import uk.co.next.tflsample.api.json.setupJson

val JsonKoinModule = module {
    single<Json> { setupJson() }
}