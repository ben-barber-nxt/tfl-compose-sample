package uk.co.next.tflsample.viewmodels.component.stop_point.ui.di

import org.koin.dsl.module
import uk.co.next.stoppoint.di.DomainStopPointKoinModule
import uk.co.next.tflsample.viewmodels.component.stop_point.ui.StopPointDetailsComponentViewModel
import uk.co.next.tflsample.viewmodels.component.stop_point.ui.StopPointDetailsComponentViewModelImpl

val StopPointDetailsComponentViewModelKoinModule = module {

    includes(DomainStopPointKoinModule)

    factory<StopPointDetailsComponentViewModel> { params ->

        StopPointDetailsComponentViewModelImpl(
            subscribeToStopPointUseCase = get(),
            stopId = params.get()
        )
    }
}