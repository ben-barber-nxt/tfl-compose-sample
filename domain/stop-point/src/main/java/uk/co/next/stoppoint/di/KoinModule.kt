package uk.co.next.stoppoint.di

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import uk.co.next.stoppoint.repositories.StopPointRepository
import uk.co.next.stoppoint.repositories.StopPointRepositoryImpl
import uk.co.next.stoppoint.usecases.SubscribeToStopPointUseCase
import uk.co.next.stoppoint.usecases.SubscribeToStopPointUseCaseImpl
import uk.co.next.tflsample.api.ktor.di.KtorCoreKoinModule

val DomainStopPointKoinModule = module {
    includes(KtorCoreKoinModule)

    singleOf(::StopPointRepositoryImpl) bind StopPointRepository::class
    singleOf(::SubscribeToStopPointUseCaseImpl) bind SubscribeToStopPointUseCase::class
}