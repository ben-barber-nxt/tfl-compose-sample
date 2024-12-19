package uk.co.next.stoppoint.repositories

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import org.koin.core.component.KoinComponent
import uk.co.next.repositories.core.ApiResult
import uk.co.next.tflsample.api.tfl.models.ApiStopPoint

interface StopPointRepository : KoinComponent {
    fun fetch(stopId: String): Flow<ApiResult<ApiStopPoint>>
}

internal class StopPointRepositoryImpl(
    private val httpClient: HttpClient,
): StopPointRepository {

    override fun fetch(stopId: String): Flow<ApiResult<ApiStopPoint>> = flow<ApiResult<ApiStopPoint>> {
        emit(ApiResult.Loading())
        runCatching {
            emit(
                ApiResult.Success(
                    httpClient.get("https://api.tfl.gov.uk/StopPoint/$stopId")
                        .body()
                )
            )
        }.onFailure {
            emit(ApiResult.Error(it))
        }
    }.flowOn(Dispatchers.IO)
}