package uk.co.next.tflsample.viewmodels.page.stop_point.di

import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import uk.co.next.tflsample.viewmodels.component.line_status.di.LineStatusComponentKoinModule
import uk.co.next.tflsample.viewmodels.component.stop_point.ui.di.StopPointDetailsComponentViewModelKoinModule
import uk.co.next.tflsample.viewmodels.page.stop_point.StopPointPageViewModel

val StopPointPageViewModelModule = module {
    includes(StopPointDetailsComponentViewModelKoinModule)
    includes(LineStatusComponentKoinModule)

    viewModelOf(::StopPointPageViewModel)
}