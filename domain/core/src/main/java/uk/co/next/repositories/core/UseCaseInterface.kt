package uk.co.next.repositories.core

import kotlinx.coroutines.flow.Flow

interface SubscribeUseCase<Out, Params> {

    operator fun invoke(params: Params) : Flow<DomainResult<Out>>
}

