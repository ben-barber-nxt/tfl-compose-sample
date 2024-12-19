package uk.co.next.tflsample.viewmodels.components.di

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import uk.co.next.repositories.stoppoint.search.di.StopPointSearchKoinModule
import uk.co.next.tflsample.viewmodels.components.SearchComponentViewModel
import uk.co.next.tflsample.viewmodels.components.SearchComponentViewModelImpl

val SearchComponentViewModelKoinModule = module {
    includes(StopPointSearchKoinModule)

    singleOf(::SearchComponentViewModelImpl) bind SearchComponentViewModel::class
}