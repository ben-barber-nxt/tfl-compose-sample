package uk.co.next.repositories.stoppoint.search.di

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import uk.co.next.repositories.stoppoint.search.repository.StopPointSearchRepository
import uk.co.next.repositories.stoppoint.search.repository.StopPointSearchRepositoryImpl
import uk.co.next.repositories.stoppoint.search.usecase.StopPointSearchUseCase
import uk.co.next.repositories.stoppoint.search.usecase.StopPointSearchUseCaseImpl
import uk.co.next.tflsample.api.ktor.di.KtorCoreKoinModule

val StopPointSearchKoinModule = module {

    includes(KtorCoreKoinModule)

    singleOf(::StopPointSearchRepositoryImpl) bind StopPointSearchRepository::class
    singleOf(::StopPointSearchUseCaseImpl) bind StopPointSearchUseCase::class
}