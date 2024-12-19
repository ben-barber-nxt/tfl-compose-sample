package uk.co.next.repositories.stoppoint.search.repository

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import org.koin.core.component.KoinComponent
import uk.co.next.repositories.core.ApiResult
import uk.co.next.tflsample.api.tfl.models.StopPointSearchResults

interface StopPointSearchRepository : KoinComponent {
    fun search(query: String): Flow<ApiResult<StopPointSearchResults>>
}

internal class StopPointSearchRepositoryImpl(
    private val httpClient: HttpClient
): StopPointSearchRepository {

    override fun search(query: String): Flow<ApiResult<StopPointSearchResults>> = flow<ApiResult<StopPointSearchResults>> {
        emit(ApiResult.Loading())
        runCatching {
            emit(
                ApiResult.Success(
                    httpClient.get("https://api.tfl.gov.uk/StopPoint/Search/$query?includeHubs=false")
                        .body()
                )
            )
        }.onFailure {
            emit(ApiResult.Error(it))
        }
    }.flowOn(Dispatchers.IO)
}