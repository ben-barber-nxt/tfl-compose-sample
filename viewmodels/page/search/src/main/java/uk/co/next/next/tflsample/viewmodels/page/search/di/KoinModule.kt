package uk.co.next.next.tflsample.viewmodels.page.search.di

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import uk.co.next.next.tflsample.viewmodels.page.search.SearchPageViewModel
import uk.co.next.tflsample.viewmodels.components.di.SearchComponentViewModelKoinModule

val SearchPageViewModelKoinModule = module {
    includes(SearchComponentViewModelKoinModule)

    singleOf(::SearchPageViewModel)
}