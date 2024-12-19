package uk.co.next.stoppoint.usecases

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import uk.co.next.repositories.core.ApiResult
import uk.co.next.repositories.core.DomainResult
import uk.co.next.repositories.core.SubscribeUseCase
import uk.co.next.stoppoint.repositories.StopPointRepository
import uk.co.next.tflsample.api.tfl.models.ApiStopPoint

interface SubscribeToStopPointUseCase : SubscribeUseCase<DomainStopPoint, String>

class SubscribeToStopPointUseCaseImpl(
    private val stopPointSearchRepository: StopPointRepository,
) : SubscribeToStopPointUseCase {
    override fun invoke(params: String): Flow<DomainResult<DomainStopPoint>> =
        stopPointSearchRepository.fetch(params)
            .map { result ->
                when(result) {
                    is ApiResult.Loading -> DomainResult.Loading()
                    is ApiResult.Success -> DomainResult.Success(processSuccessfulRepositoryResponse(result.data))
                    is ApiResult.Error -> DomainResult.Error(result.error)
                }
            }
            .catch {
                emit(DomainResult.Error(it))
            }

    private fun processSuccessfulRepositoryResponse(apiResponse: ApiStopPoint) =
        DomainStopPoint(
            id = apiResponse.id,
            name = apiResponse.commonName,
            modesAndLines = apiResponse.lineModeGroups.map {
                DomainModesAndLines(
                    modeId = it.modeName,
                    modeName = it.modeName.replace("-", " "),
                    lines = it.lineIdentifier.mapNotNull { line ->
                        apiResponse.lines.firstOrNull { line == it.id }?.let {
                            DomainLine(
                                lineName = it.name,
                                uri = it.uri,
                            )
                        }
                    },
                )
            },
            properties = apiResponse.additionalProperties.map {
                DomainAdditionalProperties(
                    category = it.category,
                    key = it.key,
                    value = it.value,
                )
            },
        )
}