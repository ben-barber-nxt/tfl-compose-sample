package uk.co.next.tflsample

import android.app.Application
import org.koin.core.context.startKoin
import uk.co.next.next.tflsample.viewmodels.page.search.di.SearchPageViewModelKoinModule
import uk.co.next.tflsample.viewmodels.page.stop_point.di.StopPointPageViewModelModule

class NextTFLSampleApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
//            logger(Logg
//            )
            modules(SearchPageViewModelKoinModule, StopPointPageViewModelModule)
        }
    }
}