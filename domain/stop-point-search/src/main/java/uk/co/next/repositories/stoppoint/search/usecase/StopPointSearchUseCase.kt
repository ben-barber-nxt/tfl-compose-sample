package uk.co.next.repositories.stoppoint.search.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import uk.co.next.repositories.core.ApiResult
import uk.co.next.repositories.core.DomainResult
import uk.co.next.repositories.core.SubscribeUseCase
import uk.co.next.repositories.stoppoint.search.repository.StopPointSearchRepository

interface StopPointSearchUseCase : SubscribeUseCase<List<DomainStopPointSearchResult>, String>

internal class StopPointSearchUseCaseImpl(
    private val stopPointSearchRepository: StopPointSearchRepository,
) : StopPointSearchUseCase {

    override fun invoke(params: String): Flow<DomainResult<List<DomainStopPointSearchResult>>> =
        stopPointSearchRepository.search(params)
            .map { result ->
                when(result) {
                    is ApiResult.Loading -> DomainResult.Loading()
                    is ApiResult.Success -> DomainResult.Success(
                        result.data.matches.map {
                            DomainStopPointSearchResult(
                                id = it.id,
                                name = it.name,
                                modes = it.modes,
                                zone = it.zone,
                            )
                        }
                    )
                    is ApiResult.Error -> DomainResult.Error(result.error)
                }
            }
            .catch {
                emit(DomainResult.Error(it))
            }

}