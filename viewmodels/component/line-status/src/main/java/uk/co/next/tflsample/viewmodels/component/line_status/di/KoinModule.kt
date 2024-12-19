package uk.co.next.tflsample.viewmodels.component.line_status.di

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import uk.co.next.tflsample.api.ktor.di.KtorCoreKoinModule
import uk.co.next.tflsample.viewmodels.component.line_status.LineStatusComponentViewModel
import uk.co.next.tflsample.viewmodels.component.line_status.LineStatusComponentViewModelImpl

val LineStatusComponentKoinModule = module {

    includes(KtorCoreKoinModule)

    singleOf(::LineStatusComponentViewModelImpl) bind LineStatusComponentViewModel::class
}